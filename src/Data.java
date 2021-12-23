import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Data {

    public static final short HEADER_SIZE=3;
    private static ReentrantLock lock=new ReentrantLock();

    private static ArrayList<byte[]> parsefile(String filename, File directory){
        ArrayList<byte[]> bytes=new ArrayList<>();
        File file=new File(directory.getAbsolutePath()+"/"+filename);
        int read;
        try{
            FileInputStream input=new FileInputStream(file);
            byte[] buffer=new byte[FFSync.SIZE-HEADER_SIZE];
            while((read=input.read(buffer))>0){
                bytes.add(truncate(buffer,read));
            }
            return bytes;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private static byte[] truncate(byte[]array,int size){
        if(size> array.length)return array;
        byte[]novo=new byte[size];
        for(int i=0;i<size;i++){
            novo[i]=array[i];
        }
        return novo;
    }

    public static void sendFile(DatagramSocket socket, InetAddress address,int port, String filename, File directory){
        ArrayList<byte[]>bytes=parsefile(filename,directory);
        short BLOCK_NUM=1;
        for(;BLOCK_NUM<=bytes.size();BLOCK_NUM++){
            try{
                sendPacket(socket,address,port, bytes.get(BLOCK_NUM-1), BLOCK_NUM);
                Logger.envio(filename,address,port,BLOCK_NUM);
                socket.setSoTimeout(1000);
                short res= Ack.receive(socket);
            } catch (SocketException e) {
                Logger.erro("Timed out expecting ACK"+e.getMessage());
                BLOCK_NUM--;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try{
            Ack.send(socket,address,port,(short)0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket(DatagramSocket socket,InetAddress address,int port,byte[] fileData,short seq) throws IOException {
        ByteArrayOutputStream bao=new ByteArrayOutputStream();
        DataOutputStream output=new DataOutputStream(bao);
        output.write(FFSync.DATA);
        output.writeShort(seq);
        output.write(fileData);
        output.flush();
        DatagramPacket packet=new DatagramPacket(bao.toByteArray(),bao.size(),address,port);
        socket.send(packet);

    }

    public static void receiveFile(DatagramSocket socket,String filename,File directory){
        short BLOCK_NUM=1;
        boolean done=false;
        ArrayList<byte[]>bytes=new ArrayList<>();
        byte[] data=new byte[FFSync.SIZE];
        byte[] buffer=new byte[FFSync.SIZE-HEADER_SIZE];
        try{
            File file=new File(directory.getAbsolutePath()+"/"+filename);
            while(!done){
                DatagramPacket packet=new DatagramPacket(data,data.length);
                try{
                    socket.setSoTimeout(5000);
                    socket.receive(packet);
                } catch (SocketTimeoutException e){
                    Logger.erro("Timed out expecting packet: "+e.getMessage());
                    Ack.send(socket,packet.getAddress(),packet.getPort(),BLOCK_NUM);
                }
                ByteArrayInputStream bai=new ByteArrayInputStream(packet.getData());
                DataInputStream input=new DataInputStream(bai);
                byte flag=input.readByte();
                if(flag== FFSync.ERROR){
                    Error.errorHandler(input);
                }
                else if(flag!= FFSync.DATA){
                    Logger.erro("Expected DATA got something else");
                }
                short seq=input.readShort();
                Logger.rececao(filename,packet.getAddress(),packet.getPort(),seq);
                if(seq>=BLOCK_NUM){
                    input.read(buffer);
                    byte[]novo=unpad(buffer);
                    if(novo.length<(FFSync.SIZE-HEADER_SIZE))done=true;
                    bytes.add(novo);
                    BLOCK_NUM=seq;
                }
                Ack.send(socket,packet.getAddress(),packet.getPort(),BLOCK_NUM);
            }
            writeFile(bytes,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeFile(List<byte[]> bytes,File file) throws IOException {
        lock.lock();
        FileOutputStream outputStream=new FileOutputStream(file);
        for(byte[]array:bytes){
            outputStream.write(array);
        }
        outputStream.flush();
        outputStream.close();
        lock.unlock();
    }

    private static byte[] unpad(byte[]array){
        int i=0;
        int j=0;
        while (i< array.length && array[i] != 0) {
            j++;
            i++;
        }
        byte[]novo=new byte[j];
        for(i=0;i<j;i++){
            novo[i]=array[i];
        }
        return novo;
    }
}

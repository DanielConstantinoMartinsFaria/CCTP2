import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Data {

    public static final short HEADER_SIZE=3;

    private static ArrayList<byte[]> parsefile(String filename, File directory){
        ArrayList<byte[]> bytes=new ArrayList<>();
        File file=new File(directory.getAbsolutePath()+"/"+filename);
        int read;
        try{
            FileInputStream input=new FileInputStream(file);
            byte[] buffer=new byte[Peer.SIZE-HEADER_SIZE];
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
                //socket.setSoTimeout(1000);
                BLOCK_NUM=ACK.receive(socket);
            } catch (SocketException e) {
                BLOCK_NUM--;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendPacket(DatagramSocket socket,InetAddress address,int port,byte[] fileData,short seq) throws IOException {
        ByteArrayOutputStream bao=new ByteArrayOutputStream();
        DataOutputStream output=new DataOutputStream(bao);
        output.write((byte)5);
        output.writeShort(seq);
        output.write(fileData);
        output.flush();
        DatagramPacket packet=new DatagramPacket(bao.toByteArray(),bao.size(),address,port);
        socket.send(packet);
    }

    public static void receiveFile(DatagramSocket socket,String filename,File directory){
        short BLOCK_NUM=1;
        boolean done=false;
        int read;
        byte[] data=new byte[Peer.SIZE];
        byte[] buffer=new byte[Peer.SIZE-HEADER_SIZE];
        try{
            File file=new File(directory.getAbsolutePath()+"/"+filename);
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            while(!done){
                DatagramPacket packet=new DatagramPacket(data,data.length);
                //socket.setSoTimeout(5000);
                socket.receive(packet);
                ByteArrayInputStream bai=new ByteArrayInputStream(packet.getData());
                DataInputStream input=new DataInputStream(bai);
                if(input.readByte()!=(byte)5)done=true;
                short seq=input.readShort();
                if(seq>=BLOCK_NUM){
                    input.read(buffer);
                    byte[]novo=unpad(buffer);
                    if(novo.length<(Peer.SIZE-HEADER_SIZE))done=true;
                    fileOutputStream.write(novo,0,novo.length);
                    BLOCK_NUM=seq;
                }
                ACK.send(socket,packet.getAddress(),packet.getPort(),BLOCK_NUM);
            }
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (SocketException ignored) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] unpad(byte[]array){
        int i=0;
        int j=0;
        while (array[i]!=0) {
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

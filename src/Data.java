import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Data {

    private static ArrayList<byte[]> parsefile(String filename, File directory){
        ArrayList<byte[]> bytes=new ArrayList<byte[]>();
        File file = new File(directory.getAbsolutePath()+"/"+filename);
        try{
            FileInputStream input=new FileInputStream(file);
            byte[] buffer=new byte[Peer.SIZE-3];
            while(input.read(buffer)!=0){
                bytes.add(buffer);
            }
            return bytes;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static void sendFile(DatagramSocket socket, InetAddress address,int port, String filename, File directory){
        ArrayList<byte[]>bytes=parsefile(filename,directory);
        short BLOCK_NUM=1;
        for(;BLOCK_NUM<=bytes.size();BLOCK_NUM++){
            try{
                sendPacket(socket,address,port, bytes.get(BLOCK_NUM-1), BLOCK_NUM);
                socket.setSoTimeout(1000);
                BLOCK_NUM=ACK.receive(socket);
            } catch (SocketException e) {
                BLOCK_NUM--;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendPacket(DatagramSocket socket,InetAddress address,int port,byte[] data,short seq) throws IOException {
        DatagramPacket packet=new DatagramPacket(data, data.length,address,port);
        socket.send(packet);
    }
}

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class Data {

    private static ArrayList<byte[]> parsefile(String filename, File directory){
        ArrayList<byte[]> bytes=new ArrayList<byte[]>();
        File file = new File(directory.getAbsolutePath()+"/"+filename);
        try{
            FileInputStream input=new FileInputStream(file);
            byte[] buffer=new byte[Packet.SIZE-3];
            while(input.read(buffer)!=0){
                bytes.add(buffer);
            }
            return bytes;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static void sendFile(DatagramSocket socket, SocketAddress address,String filename,File directory){
        ArrayList<byte[]>bytes=parsefile(filename,directory);
        short BLOCK_NUM=1;
        for(;BLOCK_NUM<=bytes.size();BLOCK_NUM++){
            try{
                sendPacket(socket,address, bytes.get(BLOCK_NUM-1), BLOCK_NUM);
                socket.setSoTimeout(1000);
                BLOCK_NUM=ACK.receive(socket);
            } catch (SocketException e) {
                BLOCK_NUM--;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendPacket(DatagramSocket socket,SocketAddress address,byte[] data,short seq) throws IOException {
        DatagramPacket packet=new DatagramPacket(data, data.length,address);
        socket.send(packet);
    }
}

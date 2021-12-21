import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class Data {

    private ArrayList<byte[]> parsefile(String filename, File directory){
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

    public void sendFile(DatagramSocket socket, SocketAddress address,String filename){
        
    }
}

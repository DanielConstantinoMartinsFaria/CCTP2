import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Set;

public class Packet {

    public static final int SIZE=512;
    public static final int PORT=8989;

    private short BLOCK_NUM;

    public Packet(){
        BLOCK_NUM=1;
    }

    public void sendACK(DatagramSocket socket, SocketAddress address) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write((byte)6);
        output.writeShort(BLOCK_NUM);
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address);
        socket.send(packet);
    }

    public void sendGetFiles(DatagramSocket socket, SocketAddress address, Set<String> files) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write((byte)5);
        for(String filename:files){
            output.writeUTF(filename);
        }
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address);
        socket.send(packet);
    }
}

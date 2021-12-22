import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.Set;

public class GetFiles {

    public void sendGetFiles(DatagramSocket socket, SocketAddress address, Set<String> files) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(Peer.SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write((byte)3);
        for(String filename:files){
            output.writeUTF(filename);
            output.write((byte)0);
        }
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address);
        socket.send(packet);
    }

    public void sendSendFiles(DatagramSocket socket, SocketAddress address, Set<String> files) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(Peer.SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write((byte)4);
        for(String filename:files){
            output.writeUTF(filename);
            output.write((byte)0);
        }
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address);
        socket.send(packet);
    }

}

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;

public class Error {

    public void send(DatagramSocket socket, InetAddress address,int port, byte errorCode, String message) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(Peer.SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write((byte)7);
        output.write(errorCode);
        output.writeUTF(message);
        output.write((byte)0);
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address,port);
        socket.send(packet);
    }

    public short receive(DatagramSocket socket,SocketAddress address){
        return 0;
    }
}

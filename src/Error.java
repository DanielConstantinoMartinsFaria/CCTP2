import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class Error {

    public void send(DatagramSocket socket, SocketAddress address, byte errorCode, String message) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(Packet.SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write((byte)7);
        output.write(errorCode);
        output.writeUTF(message);
        output.write((byte)0);
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address);
        socket.send(packet);
    }
}

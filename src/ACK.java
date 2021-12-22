import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class ACK {

    public static void send(DatagramSocket socket, SocketAddress address,short BLOCK_NUM) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(Packet.SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write((byte)6);
        output.writeShort(BLOCK_NUM);
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address);
        socket.send(packet);
    }

    public static short receive(DatagramSocket socket) throws IOException {
        byte[] buffer=new byte[Packet.SIZE];
        DatagramPacket packet = new DatagramPacket(buffer,Packet.SIZE);
        //socket.setSoTimeout(1000);
        socket.receive(packet);

        ByteArrayInputStream bai=new ByteArrayInputStream(packet.getData());
        DataInputStream input=new DataInputStream(bai);

        byte flag= input.readByte();

        if(flag!=(byte)6){
            return 0;
        }
        else {
            return (short) (input.readShort()+1);
        }

    }
}

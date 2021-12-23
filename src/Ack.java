import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;

public class Ack {

    public static void send(DatagramSocket socket, InetAddress address, int port, short BLOCK_NUM) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(Peer.SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write(Peer.ACK);
        output.writeShort(BLOCK_NUM);
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address,port);
        socket.send(packet);
    }

    public static short receive(DatagramSocket socket) throws IOException {
        byte[] buffer=new byte[Peer.SIZE];
        DatagramPacket packet = new DatagramPacket(buffer,Peer.SIZE);
        //socket.setSoTimeout(1000);
        socket.receive(packet);

        ByteArrayInputStream bai=new ByteArrayInputStream(packet.getData());
        DataInputStream input=new DataInputStream(bai);

        byte flag= input.readByte();

        if(flag!=Peer.ACK){
            return 0;
        }
        else {
            short res=input.readShort();
            System.out.println(res);
            return res;
        }

    }
}

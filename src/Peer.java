import java.io.IOException;
import java.net.*;

public class Peer {


    public static void main(String[]args) throws IOException {
        DatagramSocket socket=new DatagramSocket();
        SocketAddress socketAddress=new InetSocketAddress(InetAddress.getLocalHost(),8989);
        PeerUDP peerUDP=new PeerUDP(socket,args,socketAddress);
        new Thread(peerUDP).start();
    }
}

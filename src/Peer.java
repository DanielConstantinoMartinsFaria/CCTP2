import java.io.IOException;
import java.net.*;

public class Peer {

    public static final int SIZE=512;
    public static final int PORT=8989;

    public static void main(String[]args) throws IOException, InterruptedException {
        DatagramSocket socket1=new DatagramSocket();
        DatagramSocket socket2=new DatagramSocket(PORT);
        PeerUDP peerUDP1=new PeerUDP(socket1,args,InetAddress.getLocalHost(),PORT);
        PeerUDP peerUDP2=new PeerUDP(socket2,args,null,0);
        Thread peer1=new Thread(peerUDP1);
        Thread peer2=new Thread(peerUDP2);

        peer1.start();
        peer2.start();

        peer1.join();
        peer2.join();
    }
}

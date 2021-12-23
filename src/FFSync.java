import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class FFSync {

    public static final int SIZE=512;
    public static final int PORT=8989;

    public static final byte FILESINFO=1;
    public static final byte GETFILES=2;
    public static final byte DATA=3;
    public static final byte ACK=4;
    public static final byte ERROR=5;

    public static void main(String[]args) throws IOException, InterruptedException {
        PortHandler portHandler=new PortHandler();
        Logger.start(args);
        DatagramSocket socket1=new DatagramSocket();
        DatagramSocket socket2=new DatagramSocket(PORT);
        PeerUDP peerUDP1=new PeerUDP(socket1,args,InetAddress.getByName(args[1]),PORT,portHandler);
        PeerUDP peerUDP2=new PeerUDP(socket2,args,null,0,portHandler);
        Thread peer1=new Thread(peerUDP1);
        Thread peer2=new Thread(peerUDP2);

        peer1.start();
        peer2.start();

        peer1.join();
        peer2.join();

        Logger.end();
    }

    /*
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
     */
}

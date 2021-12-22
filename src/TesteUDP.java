import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TesteUDP {

    public static void main(String[]args) throws IOException {

        DatagramSocket socket=new DatagramSocket(8989,InetAddress.getLocalHost());
        System.out.println(ACK.receive(socket));

    }
}

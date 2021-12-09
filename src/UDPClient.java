import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient {
    public static void main(String[] args) throws UnknownHostException, SocketException {
        String pasta="~/Documents/Random";
        InetAddress ip=InetAddress.getLocalHost();
        DatagramSocket socket=new DatagramSocket();
    }
}

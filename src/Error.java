import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;

public class Error {

    public static final byte UNKNOWN_ERROR=0;
    public static final byte FILE_ERROR=1;
    public static final byte ACCESS_ERROR=2;
    public static final byte PASSWORD_FAIL=3;

    public static void send(DatagramSocket socket, InetAddress address,int port, byte errorCode, String message) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(Peer.SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write(Peer.ERROR);
        output.write(errorCode);
        output.writeUTF(message);
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address,port);
        socket.send(packet);
    }

    public static void errorHandler(DataInputStream input){
        try{
            byte errorCode= input.readByte();
            if(errorCode==FILE_ERROR){
                //log
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Error {

    public static final byte UNKNOWN_ERROR=0;
    public static final byte FILE_ERROR=1;
    public static final byte ACCESS_ERROR=2;
    public static final byte PASSWORD_FAIL=3;

    public static void sendError(DatagramSocket socket, InetAddress address,int port, byte errorCode, String message) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(FFSync.SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write(FFSync.ERROR);
        output.write(errorCode);
        output.writeUTF(message);
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address,port);
        socket.send(packet);
    }

    public static void errorHandler(DataInputStream input){
        try{
            byte errorCode= input.readByte();

            String message="";
            switch (errorCode){
                case 0->message="";
                case 1->message="FILE NOT FOUND:";
                case 2->message="ACCESS VIOLATION";
                case 3->message="WRONG PASSWORD";
            }
            message+=input.readUTF();
            Logger.erro(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

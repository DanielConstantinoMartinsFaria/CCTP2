import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class Ack {

    public static void send(DatagramSocket socket, InetAddress address, int port, short BLOCK_NUM) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(FFSync.SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write(FFSync.ACK);
        output.writeShort(BLOCK_NUM);
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address,port);
        socket.send(packet);
    }

    public static short receive(DatagramSocket socket) throws IOException {
        byte[] buffer=new byte[FFSync.SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, FFSync.SIZE);
        try{
            socket.setSoTimeout(1000);
            socket.receive(packet);
        } catch (SocketTimeoutException e){
            Logger.erro("Timed out expecting packet: "+e.getMessage());
        }

        Logger.mensagem("ACK",packet.getAddress(), packet.getPort());

        ByteArrayInputStream bai=new ByteArrayInputStream(packet.getData());
        DataInputStream input=new DataInputStream(bai);

        byte flag= input.readByte();
        if(flag== FFSync.ERROR){
            Error.errorHandler(input);
            return 0;
        }
        else if(flag!= FFSync.ACK){
            return 0;
        }
        else {
            short res=input.readShort();
            System.out.println(res);
            return res;
        }

    }
}

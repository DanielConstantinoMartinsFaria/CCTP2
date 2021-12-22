import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Set;

public class Packet {

    public static final int SIZE=512;
    public static final int PORT=8989;

    private short BLOCK_NUM;

    public Packet(){
        BLOCK_NUM=1;
    }



    public void sendGetFiles(DatagramSocket socket, SocketAddress address, Set<String> files) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write((byte)3);
        for(String filename:files){
            output.writeUTF(filename);
            output.write((byte)0);
        }
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address);
        socket.send(packet);
    }

    public void sendSendFiles(DatagramSocket socket, SocketAddress address, Set<String> files) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write((byte)4);
        for(String filename:files){
            output.writeUTF(filename);
            output.write((byte)0);
        }
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address);
        socket.send(packet);
    }


    /*
    public void receive(DatagramSocket socket) throws IOException {
        byte[] buffer=new byte[SIZE];
        DatagramPacket packet = new DatagramPacket(buffer,SIZE);
        socket.receive(packet);

        ByteArrayInputStream bai=new ByteArrayInputStream(packet.getData());
        DataInputStream input=new DataInputStream(bai);

        byte flag=input.readByte();
        switch (flag){
            case (byte)1:
                ACK ack=new ACK();
                ack.receive(input);
            case (byte)2:
                //receiveSendFilesInfo
            case (byte)3:
                //receiveGetFiles
            case (byte)4:
                //receiveSendFiles
            case (byte)5:
                //receiveData
            case (byte)6:
                //receiveACK
            case (byte)7:
                //receiveError
            case (byte)8:
                //receiveSendFilesInfo
        }
    }
     */

}

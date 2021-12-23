import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.*;

public class GetFiles {

    public static void sendGetFiles(DatagramSocket socket, InetAddress address, int port, List<String> files, int[] ports) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(Peer.SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write(Peer.GETFILES);
        int i=0;
        output.writeInt(files.size());
        for(String filename:files){
            output.writeUTF(filename);
            output.writeInt(ports[i]);
            i++;
        }
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address,port);
        socket.send(packet);
    }

    public static ArrayList<ParFilePort> receiveGetFiles(DatagramSocket socket) throws IOException {
        byte[] buffer=new byte[Peer.SIZE];
        DatagramPacket packet = new DatagramPacket(buffer,Peer.SIZE);
        //socket.setSoTimeout(1000);
        socket.receive(packet);

        ByteArrayInputStream bai=new ByteArrayInputStream(packet.getData());
        DataInputStream input=new DataInputStream(bai);

        byte flag= input.readByte();

        if(flag==Peer.ERROR){
            Error.errorHandler(input);
            return null;
        }
        else if(flag!=Peer.GETFILES){
            return null;
        }
        else {
            ArrayList<ParFilePort> filePorts=new ArrayList<>();
            int num = input.readInt();
            String filename;
            int port;
            for(int i=0;i<num;i++){
                filename=input.readUTF();
                port=input.readInt();
                filePorts.add(new ParFilePort(filename,port));
            }
            return filePorts;
        }

    }
}

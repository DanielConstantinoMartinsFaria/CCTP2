import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GetFiles {

    public void sendGetFiles(DatagramSocket socket, SocketAddress address, Set<String> files,String[] ports) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(Peer.SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write(Peer.GETFILES);
        int i=0;
        output.writeInt(files.size());
        for(String filename:files){
            output.writeUTF(filename);
            output.writeUTF(ports[i]);
            i++;
        }
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address);
        socket.send(packet);
    }

    public Map<String,String> receiveGetFiles(DatagramSocket socket) throws IOException {
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

        }
        else {
            Map<String,String>filePorts=new HashMap<>();
            int num = input.readInt();
            String filename;
            String port;
            for(int i=0;i<num;i++){
                filename=input.readUTF();
                port=input.readUTF();
                filePorts.put(filename,port);
            }
            return filePorts;
        }

    }
}

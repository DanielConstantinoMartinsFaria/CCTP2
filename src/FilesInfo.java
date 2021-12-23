import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class FilesInfo {


    public static void sendFilesInfo(DatagramSocket socket, InetAddress address, ListaFicheiros listaFicheiros, int port) throws IOException {

        ByteArrayOutputStream baos=new ByteArrayOutputStream(Peer.SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write(Peer.FILESINFO);
        output.write(listaFicheiros.serialize());
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address,port);
        socket.send(packet);
    }

    public static DatagramPacket receiveFilesInfo(DatagramSocket socket, ListaFicheiros listaFicheiros, List<String> filesToGet) throws IOException {
        byte[] buffer=new byte[Peer.SIZE];
        DatagramPacket packet = new DatagramPacket(buffer,Peer.SIZE);
        //socket.setSoTimeout(1000);
        socket.receive(packet);

        ByteArrayInputStream bai=new ByteArrayInputStream(packet.getData());
        DataInputStream input=new DataInputStream(bai);

        byte flag= input.readByte();
        if(flag==Peer.ERROR){
            Error.errorHandler(input);
        }
        else if(flag==Peer.FILESINFO){
            ListaFicheiros newList=new ListaFicheiros();
            newList.deserialize(input);
            listaFicheiros.checkDiff(newList,filesToGet);
        }
        return packet;
    }

}

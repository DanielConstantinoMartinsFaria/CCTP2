import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.List;

public class FilesInfo {


    public static void sendFilesInfo(DatagramSocket socket, InetAddress address, ListaFicheiros listaFicheiros, int port,String pass) throws IOException {

        ByteArrayOutputStream baos=new ByteArrayOutputStream(FFSync.SIZE);
        DataOutputStream output = new DataOutputStream(baos);
        output.write(FFSync.FILESINFO);
        output.write(listaFicheiros.serialize());
        output.writeUTF(pass);
        output.flush();
        DatagramPacket packet=new DatagramPacket(baos.toByteArray(), baos.size(),address,port);
        socket.send(packet);
    }

    public static DatagramPacket receiveFilesInfo(DatagramSocket socket, ListaFicheiros listaFicheiros, List<String> filesToGet,String password) throws IOException {
        byte[] buffer=new byte[FFSync.SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, FFSync.SIZE);
        try{
            socket.setSoTimeout(1000);
            socket.receive(packet);
        } catch (SocketTimeoutException e){
            Logger.erro("Timed out expecting packet: "+e.getMessage());
        }

        ByteArrayInputStream bai=new ByteArrayInputStream(packet.getData());
        DataInputStream input=new DataInputStream(bai);

        byte flag= input.readByte();
        if(flag== FFSync.ERROR){
            Error.errorHandler(input);
        }
        else if(flag== FFSync.FILESINFO){
            ListaFicheiros newList=new ListaFicheiros();
            newList.deserialize(input);
            if(!password.equals(input.readUTF())){
                Error.sendError(socket,packet.getAddress(),packet.getPort(), (byte) 3,"Wrong password");
                Logger.erro("Peer sent a wrong password");
                return null;
            }
            listaFicheiros.checkDiff(newList,filesToGet);
        }
        return packet;
    }

}

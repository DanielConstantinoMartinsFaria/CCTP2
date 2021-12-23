import java.io.File;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class FileSender implements Runnable{

    DatagramSocket socket;
    InetAddress address;
    int port;
    String filename;
    File directory;

    boolean sender;

    public FileSender(InetAddress address, int port, String filename, File directory, DatagramSocket socket){
        this.address=address;
        this.port=port;
        this.filename=filename;
        this.directory=directory;
        if(socket==null)sender=true;
        else this.socket=socket;
    }

    public void sender(){
        try{
            DatagramSocket socket=new DatagramSocket();
            Data.sendFile(socket,address,port,filename,directory);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void receiver(){
        Data.receiveFile(socket,filename,directory);
    }

    @Override
    public void run() {
        if(sender)sender();
        else receiver();
    }
}

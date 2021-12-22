import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PeerUDP implements Runnable{
    private final String ROOT_DIR = System.getProperty("user.dir");

    private File directory;
    private DatagramSocket socket;
    private InetAddress destination;
    private int port;

    public PeerUDP(DatagramSocket socket, String[] args, InetAddress dest,int port){
        this.socket=socket;
        this.destination=dest;
        this.port=port;

        String path=ROOT_DIR + "/" + args[0];
        directory=new File(path);

        if(!directory.exists()&&!directory.mkdirs()) {
            System.out.println("ERROR");
        }
    }

    private void logging() throws IOException {
        PrintWriter writer=new PrintWriter("log.txt", StandardCharsets.UTF_8);

        writer.println("Directory:"+directory.getName());
        if(destination!=null)writer.println("Peer:"+destination.getHostName());
        else writer.println("Peer:Not determined yet");
        writer.println("Port:"+port);

        ListaFicheiros files=new ListaFicheiros();
        files.updateFiles(directory);
        writer.println(files.toString());
    }

    public void runSender() throws IOException {
        ACK.send(socket,destination,port, (short) 1);
    }

    public void runReceiver() throws IOException {
        System.out.println(ACK.receive(socket));
    }

    @Override
    public void run() {
        if(destination==null){
            try{
                runReceiver();
                logging();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try{
                runSender();
                logging();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

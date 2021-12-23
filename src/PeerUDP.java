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
        writer.flush();
        writer.close();
    }

    public void runSender() throws IOException {
        Data.sendFile(socket,destination,port,"test/aaaa.txt",directory);
    }

    public void runReceiver() throws IOException, InterruptedException {
        Data.receiveFile(socket,"teste/log.txt",directory);
    }

    @Override
    public void run() {
        if(destination==null){
            try{
                runReceiver();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        else{
            try{
                runSender();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

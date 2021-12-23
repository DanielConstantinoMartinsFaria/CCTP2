import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class PeerUDP implements Runnable{
    private final String ROOT_DIR = System.getProperty("user.dir");

    private File directory;
    private DatagramSocket socket;
    private InetAddress destination;
    private String[]args;
    private int port;
    private PortHandler portHandler;
    private ListaFicheiros ficheiros;

    public PeerUDP(DatagramSocket socket, String[] args, InetAddress dest,int port,PortHandler portHandler){
        this.socket=socket;
        this.destination=dest;
        this.port=port;
        this.args=args;

        String path;

        if(!args[0].equals("."))path=ROOT_DIR + "/" + args[0];
        else path=ROOT_DIR;
        directory=new File(path);

        if(!directory.exists()&&!directory.mkdirs()) {
            Logger.erro("Directory not Found");
        }
        ficheiros=new ListaFicheiros();
        ficheiros.updateFiles(directory);

        this.portHandler=portHandler;
    }

    public void runSender() throws IOException {
        FilesInfo.sendFilesInfo(socket,destination,ficheiros,port,args[2]);
        List<ParStringInt> filesAndPorts=GetFiles.receiveGetFiles(socket);
        for(ParStringInt parStringInt :filesAndPorts){
            new FileSender(destination, parStringInt.getSecond(), parStringInt.getFirst(),directory,null).sender();
        }
    }

    public void runReceiver() throws IOException, InterruptedException {
        List<String> files=new ArrayList<>();
        DatagramPacket packet= FilesInfo.receiveFilesInfo(socket,ficheiros,files,args[2]);
        if(packet==null) {
            return;
        }
        destination=packet.getAddress();
        port=packet.getPort();
        int[] myPorts=portHandler.getPorts(files.size());
        GetFiles.sendGetFiles(socket,destination,port,files,myPorts);

        for(int i=0;i< files.size();i++){   //Abrir socket para receber os diferentes ficheiros
            DatagramSocket ds=new DatagramSocket(myPorts[i]);
            new FileSender(null,0,files.get(i),directory,ds).receiver();
        }
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

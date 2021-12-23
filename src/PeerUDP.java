import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PeerUDP implements Runnable{
    private final String ROOT_DIR = System.getProperty("user.dir");

    private File directory;
    private DatagramSocket socket;
    private InetAddress destination;
    private int port;
    private PortHandler portHandler;
    private ListaFicheiros ficheiros;

    public PeerUDP(DatagramSocket socket, String[] args, InetAddress dest,int port,PortHandler portHandler){
        this.socket=socket;
        this.destination=dest;
        this.port=port;

        String path;

        if(args[0]!=".")path=ROOT_DIR + "/" + args[0];
        else path=ROOT_DIR;
        directory=new File(path);

        if(!directory.exists()&&!directory.mkdirs()) {
            System.out.println("ERROR");
        }
        ficheiros=new ListaFicheiros();
        ficheiros.updateFiles(directory);

        this.portHandler=portHandler;
    }

    public void runSender() throws IOException {
        String path=ROOT_DIR;
        directory=new File(path+"/test");
        ficheiros.updateFiles(directory);

        FilesInfo.sendFilesInfo(socket,destination,ficheiros,port);
        List<ParFilePort> filesAndPorts=GetFiles.receiveGetFiles(socket);
        for(ParFilePort parFilePort:filesAndPorts){
            new FileSender(destination, parFilePort.getPort(), parFilePort.getFilename(),directory,null).sender();
        }
    }

    public void runReceiver() throws IOException, InterruptedException {
        List<String> files=new ArrayList<>();
        DatagramPacket packet= FilesInfo.receiveFilesInfo(socket,ficheiros,files);
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

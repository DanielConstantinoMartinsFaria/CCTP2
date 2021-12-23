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
    private ListaFicheiros ficheiros;

    public PeerUDP(DatagramSocket socket, String[] args, InetAddress dest,int port){
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

    }

    public void runSender() throws IOException {
        List<ParStringInt> filesAndPorts=null;
        while(filesAndPorts==null){
            FilesInfo.sendFilesInfo(socket,destination,ficheiros,port,args[2]);
            filesAndPorts=GetFiles.receiveGetFiles(socket);
            Logger.erro("Timed out expecting packet");
        }
        for(ParStringInt parStringInt :filesAndPorts){
            Data.sendFile(socket,destination,port,parStringInt.getFirst(),directory);
        }
    }

    public void runReceiver() throws IOException, InterruptedException {
        List<String> files=new ArrayList<>();
        DatagramPacket packet=null;
        while(packet==null){
            packet=FilesInfo.receiveFilesInfo(socket,ficheiros,files,args[2],0);
            Logger.erro("Timed out expecting packet");
        }

        destination=packet.getAddress();
        System.out.println(destination.getHostName());
        port=packet.getPort();

        GetFiles.sendGetFiles(socket,destination,port,files);

        for(int i=0;i< files.size();i++){   //Abrir socket para receber os diferentes ficheiros
            Data.receiveFile(socket,files.get(i),directory);
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

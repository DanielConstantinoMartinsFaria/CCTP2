import java.io.*;
import java.lang.module.Configuration;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServer implements Runnable{

    public boolean run;
    public int port;
    public ServerSocket serverSocket;
    public String message;


    public static void server (String ip){

        int port = 80;
        boolean run = true;
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            //System.out.println("Server starting...");
            //System.out.println("Using Port: " + conf.getPort());
            System.err.println("Server running on port: "+ port);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        String message = "Inicializando";
    }

    public void newMesage(String message){}

    @Override
    public void run() {
        try{
            while (run) {

                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected");
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String s;
                while((s = in.readLine())!=null){
                    System.out.println(s);
                    if(s.isEmpty()){
                        break;
                    }
                }

                OutputStream clientOutput = clientSocket.getOutputStream();
                clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
                clientOutput.write("\r\n".getBytes());
                String m = "<b>" + message + "</b>";
                clientOutput.write(m.getBytes());
                clientOutput.write("\r\n\r\n".getBytes());
                clientOutput.flush();
                System.err.println("Client connection closed!");
                in.close();
                clientOutput.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}

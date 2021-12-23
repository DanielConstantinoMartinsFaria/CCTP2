import java.io.*;
import java.lang.module.Configuration;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServer {

    //private int port = peer.port;

    public static void main(String[] args) throws  IOException{

        int port = 8989;
        //System.out.println("Server starting...");
        //System.out.println("Using Port: " + conf.getPort());

        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Server running on port: "+ port);

        while(true){
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
            clientOutput.write("<b>FT-Rapid</b>".getBytes());
            clientOutput.write("\r\n\r\n".getBytes());
            clientOutput.flush();
            System.err.println("Client connection closed!");
            in.close();
            clientOutput.close();
        }



    }
}

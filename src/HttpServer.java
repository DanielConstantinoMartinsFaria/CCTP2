import java.io.*;
import java.lang.module.Configuration;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServer implements Runnable{

    private boolean run;
    private int port;
    private ServerSocket serverSocket;
    private String message;

    private static final String CRLF="\n\r";


    public HttpServer(){
        port = 8080;
        run = true;
        try{
            serverSocket = new ServerSocket(port,0, InetAddress.getByName(null));
            System.err.println("Server running on port: "+ port);
            System.err.println(serverSocket.getInetAddress());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        message = "<b>"+"Inicializando"+"<b>";
    }

    public void newMesage(String message){
        this.message="<b>"+message+"<b>";
    }

    public void turnOFF() throws IOException {
        run =false;
    }

    private void handleClient(Socket clientSocket) throws IOException {
        InputStream inputStream=clientSocket.getInputStream();
        OutputStream outputStream=clientSocket.getOutputStream();


        String response="HTTP/1.1 200 OK"+CRLF+
                "Content-Length: "+
                message.getBytes().length+CRLF+CRLF+
                message+CRLF+CRLF;

        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();


        inputStream.close();
        outputStream.close();
    }

    @Override
    public void run() {
        while (run) {
            try{
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
                serverSocket.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

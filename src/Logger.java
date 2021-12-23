import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger {

    private static final String LOG_NAME="log.txt";

    private static String[] args;
    private static List<String> msgs;
    private static int erros;
    private static int envios;
    private static int rececoes;
    private static FileWriter writer;
    private static boolean available;

    public static void start(String[]args) {
        try{
            Logger.args =args;
            Logger.msgs=new ArrayList<>();
            Logger.envios=0;
            Logger.rececoes=0;
            Logger.erros=0;
            Logger.writer=new FileWriter(args[0]+"/"+LOG_NAME);
            Logger.available=true;
            writer.write("LOG:"+LOG_NAME);
            writer.write("DIR:"+args[0]+"\n");
            writer.write("PEER:"+args[1]+"\n\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String date(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static void erro(String message){
        if(available){
            String msg= date()+"|"+"ERROR:"+": "+message;
            msgs.add(msg+"\n");
            try{
                writer.write(msg+"\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            erros++;
        }
    }

    public static void envio(String filename, InetAddress address, int port, short BLOCK_NUM){
        if(available){
            String msg= date()+"|"+"SENT TO: "+address.getHostName()+":"+port+" | File: "+filename+" Block: "+BLOCK_NUM;
            msgs.add(msg+"\n");
            try{
                writer.write(msg+"\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            envios++;
        }
    }

    public static void rececao(String filename, InetAddress address, int port, short BLOCK_NUM){
        if(available){
            String msg= date()+"|"+"RECEIVED FROM: "+address.getHostName()+":"+port+" | File: "+filename+" Block: "+BLOCK_NUM;
            msgs.add(msg+"\n");
            try{
                writer.write(msg+"\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            rececoes++;
        }
    }

    public static void mensagem(String tipo,InetAddress address, int port){
        String msg= date()+"|"+"RECEIVED FROM: "+address.getHostName()+":"+port+" | Type: "+tipo;
        msgs.add(msg+"\n");

        try{
            writer.write(msg+"\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void end(){
        if(available){
            try{
                available=false;
                for(String msg:msgs){
                    writer.write(msg);
                }
                writer.write("ERRORS:"+erros+"\n");
                writer.write("SENT:"+envios+"\n");
                writer.write("RECEIVED:"+rececoes+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

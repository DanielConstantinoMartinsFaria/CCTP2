import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger implements Runnable{

    private static final String LOG_NAME="log.txt";

    private String[] args;
    private ListaFicheiros listaFicheiros;

    public Logger(String[]args, ListaFicheiros listaFicheiros){
        this.args=args.clone();
        this.listaFicheiros=listaFicheiros.clone();
    }

    private String date(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public void error(String message){

    }

    @Override
    public void run() {
        try {
            FileWriter writer =new FileWriter(LOG_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

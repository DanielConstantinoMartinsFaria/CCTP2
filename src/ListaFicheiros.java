import java.io.DataInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ListaFicheiros {
    private Map<String,Long> files;

    public ListaFicheiros(){
        files=new HashMap<>();
    }

    public void updateFiles(File directory){
        File[] files=directory.listFiles();
        if(files!=null){
            for(File f:files){
                if(!f.isDirectory())this.files.put(f.getName(),f.length());
            }
        }
    }

    public void updateFiles(DataInputStream input){

    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,Long>entry:files.entrySet()){
            sb.append("\n").append(entry.getKey()).append(" - ").append(entry.getValue());
        }
        return sb.toString();
    }
}

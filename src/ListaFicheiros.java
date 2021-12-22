import java.io.DataInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ListaFicheiros {
    private Set<String> files;

    public ListaFicheiros(){
        files=new TreeSet<>();
    }

    public void updateFiles(File directory){
        File[] files=directory.listFiles();
        if(files!=null){
            for(File f:files){
                if(!f.isDirectory())this.files.add(f.getName());
            }
        }
    }

    public boolean checkDiff(ListaFicheiros list,Set<String>send,Set<String>request){
        send=files.stream().filter(f->!list.files.contains(f)).collect(Collectors.toSet());
        request=list.files.stream().filter(f->!files.contains(f)).collect(Collectors.toSet());
        return send.size()!=0 && request.size()!=0;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(String entry:files){
            sb.append("\n").append(entry);
        }
        return sb.toString();
    }
}

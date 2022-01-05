import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ListaFicheiros {
    private List<String> files;

    public ListaFicheiros(){
        files=new ArrayList<>();
    }

    public ListaFicheiros(List<String> files){
        this.files=new ArrayList<>();
        this.files.addAll(files);
    }

    public void updateFiles(File directory){
        this.files=new ArrayList<>();
        File[] files=directory.listFiles();
        if(files!=null){
            for(File f:files){
                if(!f.isDirectory()&&!f.getName().equals("log.txt"))this.files.add(f.getName());
            }
        }
    }

    public void updateFiles(List<String> files){
        this.files=new ArrayList<>();
        this.files.addAll(files);
    }

    public boolean checkDiff(ListaFicheiros list,List<String>request){
        request.addAll(list.files.stream().filter(f->!files.contains(f)).collect(Collectors.toList()));
        return request.size()!=0;
    }

    public ListaFicheiros clone(){
        ListaFicheiros nova=new ListaFicheiros(this.files);
        return nova;
    }

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream bao=new ByteArrayOutputStream();
        DataOutputStream output=new DataOutputStream(bao);
        output.writeInt(files.size());
        for(String filename:files){
            output.writeUTF(filename);
        }
        return bao.toByteArray();
    }

    public void deserialize(DataInputStream din) throws IOException {
        files=new ArrayList<>();
        int size=din.readInt();
        for(int i=0;i<size;i++){
            files.add(din.readUTF());
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(String entry:files){
            sb.append("\n").append(entry);
        }
        return sb.toString();
    }
}

import java.io.*;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileInfo{

    public static void receive(DataInputStream input,String directory) throws IOException {
        int nFiles=input.readInt();
        Map<String,Long> files=new HashMap<>();
        byte[] nome;
        short tamNome;
        Long tamanho;
        for(int i=0;i<nFiles;i++){
            tamanho=input.readLong();
            tamNome= input.readShort();
            nome=new byte[tamNome];
            input.readFully(nome,0,tamNome);
            files.put(Arrays.toString(nome),tamanho);
        }
        //Pedir em falta localmente e enviar os que faltam remotamente

    }

    public static void send(DataOutputStream output,String directory) throws IOException {
        Map<String,Long> files=new HashMap<>();
        for(File f : new File(directory).listFiles()){
            if(!f.isDirectory()){
                String name= f.getName();
                files.put(name,f.length());
            }
        }
        // Int:Numero de Files "| Long:Tamanho do ficheiro | Int:Numero de bytes do nome do file | UTF:Filename "| "Long:Tamanho do..."|...
        output.writeInt(files.size());
        for(Map.Entry<String,Long> entry: files.entrySet()){
            output.writeLong(entry.getValue());
            output.writeShort(entry.getKey().getBytes(StandardCharsets.UTF_8).length);
            output.writeUTF(entry.getKey());
        }
    }
}

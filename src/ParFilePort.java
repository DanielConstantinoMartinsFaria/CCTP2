public class ParFilePort {
    private String filename;
    private int port;

    public ParFilePort(String file,int port){
        this.filename=file;
        this.port=port;
    }

    public String getFilename(){
        return filename;
    }

    public int getPort(){
        return port;
    }
}

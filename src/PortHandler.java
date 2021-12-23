import java.util.concurrent.locks.ReentrantLock;

public class PortHandler{

    private static final int MAX=49000;
    private static final int MIN=1025;

    private int CURRENT;

    private ReentrantLock lock=new ReentrantLock();

    public PortHandler(){
        CURRENT=MAX;
    }

    public int[] getPorts(int num){
        int[] ports=new int[num];
        for(int i=0;i<num;i++){
            lock.lock();
            ports[i]=CURRENT;
            CURRENT--;
            lock.unlock();
        }
        return ports;
    }
}

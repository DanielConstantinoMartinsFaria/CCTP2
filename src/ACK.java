import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ACK{

    public static short receive(DataInputStream input,short expectedBlock) throws IOException {
        short block= input.readShort();
        if(block==expectedBlock)return (short) (block+1);
        else return block;
    }

    public static void send(DataOutputStream output,short block) throws IOException {
        output.write((byte)1);
        output.writeShort(block);
    }
}

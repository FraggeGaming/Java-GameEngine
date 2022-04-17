package EntityEngine.Network;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ByteParser {
    public ByteParser(){

    }

    public byte[] getBytes(DataInputStream input) throws IOException {

            int size = 0;
            byte[] b = new byte[size];
            // Read data into byte array

            input.readFully(b);

            return  b;




    }
    public String toString(byte[] bytes){
        if (bytes != null)
            return new String(bytes, StandardCharsets.UTF_8);
        return null;

    }
}

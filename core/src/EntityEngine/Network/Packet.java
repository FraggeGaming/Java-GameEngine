package EntityEngine.Network;

import EntityEngine.Components.Component;
import com.badlogic.gdx.utils.Array;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

//packet: size, content
public class Packet {
    int streamSize;
    byte[] content;
    public Packet(DataInputStream stream) throws IOException {
        streamSize = stream.readInt();
        content = new byte[streamSize];
        stream.readFully(content);

    }

    /*public Packet(File file) throws IOException {
        content = fileToBytes(file);
    }*/

    public Packet(String string){
        content = stringToByte(string);
        //System.out.println("size: " +streamSize);
    }



    public void sendPacket(OutputStream out) throws IOException {
        byte[] header =  ByteBuffer.allocate(4).putInt(streamSize).array();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

        outputStream.write(header);
        outputStream.write(content);
        out.write(outputStream.toByteArray());
    }

    /*private byte[] fileToBytes(File file) throws IOException {
        FileInputStream imgFile = new FileInputStream(file);
        byte[] b = imgFile.readAllBytes();
        streamSize = b.length;
        return b;
    }*/

    private byte[] stringToByte(String string){
        content = string.getBytes(StandardCharsets.UTF_8);
        streamSize = content.length;

        return content;
    }

    @Override
    public String toString(){
        if (content != null)
            return new String(content, StandardCharsets.UTF_8);
        return null;

    }
}

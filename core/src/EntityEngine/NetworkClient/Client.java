package EntityEngine.NetworkClient;

import EntityEngine.Network.Packet;
import EntityEngine.Network.Packet;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class Client implements Runnable{
    private static Socket server;
    private static DataOutputStream dataOutputStream;
    DataInputStream byteInput = null;
    OutputStreamWriter outputStreamWriter = null;
    BufferedReader bufferedReader = null;
    BufferedWriter bufferedWriter = null;

    BlockingQueue<Packet> queueOut;
    BlockingQueue<Packet> queueIn;
    public boolean isHost;


    Packet packet;
    public Client(String ip, int port, BlockingQueue<Packet> queueOut, BlockingQueue<Packet> queueIn, boolean isHost){
        this.isHost = isHost;
        this.queueOut = queueOut;
        this.queueIn = queueIn;
        try {
            server = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            openIO();
            while (server.isConnected()){
                readPacket();

                sendPacket();
            }

            close();
        }catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private void sendPacket() throws IOException, InterruptedException {
        //check if blockingqueu has packet to send
        //if true

        if (queueOut.isEmpty())
            return;

        Packet data = queueOut.take();


        packet = new Packet(data.toString());
        packet.sendPacket(dataOutputStream);

    }

    private void readPacket() throws IOException, InterruptedException {
        if (byteInput.available() == 0)
            return;

        packet = new Packet(byteInput);


        queueIn.put(packet);
        //System.out.println(packet.toString());
    }

    public void openIO(){
        try {

            byteInput = new DataInputStream(new BufferedInputStream(server.getInputStream()));
            dataOutputStream  = new DataOutputStream(server.getOutputStream());

            System.out.println("connection established to server");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        System.out.println("Closing connection");
        server.close();
        byteInput.close();
        outputStreamWriter.close();
        bufferedReader.close();
        bufferedWriter.close();
    }
}

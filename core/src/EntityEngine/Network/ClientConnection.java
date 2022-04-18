package EntityEngine.Network;

import EntityEngine.Components.Component;
import EntityEngine.Components.TextureComponent;
import EntityEngine.Components.TransformComponent;
import com.badlogic.gdx.utils.Array;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ClientConnection implements Runnable{
    Socket socket = null;
    DataInputStream byteInput = null;
    OutputStreamWriter outputStreamWriter = null;
    private static DataOutputStream dataOutputStream;

    BufferedReader bufferedReader = null;
    BufferedWriter bufferedWriter = null;
    Client client;
    List<Client> clientList;
    Packet packet;


    public ClientConnection(ServerSocket serverSocket, List<Client> clientList, boolean isHost){
        try {
            socket = serverSocket.accept();
            client = new Client(socket);
            client.isHost = isHost;
            clientList.add(client);
            this.clientList = clientList;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            System.out.println("running");
            openIO(); //Opens input and output streams
            while (socket.isConnected()){
                if (byteInput.available() != 0){

                    packet = new Packet(byteInput);
                    //if packet comes from host
                    /*if (client.isHost){
                        //sends pkt to all other clients
                        for (Client client : clientList) {

                            if (client.socket != socket)
                            packet.sendPacket(client.socket.getOutputStream());
                        }
                    }

                    else{
                        //if not from host, send it to the host
                        packet.sendPacket(socket.getOutputStream());
                    }*/

                    for (Client client : clientList) {
                        if (client.socket != socket)
                        packet.sendPacket(client.socket.getOutputStream());
                    }


                }
            }

            close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openIO(){
        try {

            byteInput = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dataOutputStream  = new DataOutputStream(socket.getOutputStream());

            System.out.println("Server online");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        System.out.println("Closing connection");
        socket.close();
        byteInput.close();
        outputStreamWriter.close();
        bufferedReader.close();
        bufferedWriter.close();
    }
}

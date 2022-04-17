package EntityEngine.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Host implements Runnable{
    int port;
    ExecutorService pool = Executors.newCachedThreadPool();
    ClientConnection clientServer;
    ServerSocket serverSocket = null;
    List<Client> clientList = new ArrayList<>();
    boolean isHost = true;
    public Host(int port){
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setUpConnection() throws IOException {

        while (true){
            clientServer = new ClientConnection(serverSocket, clientList, isHost);
            isHost = false;
            pool.execute(clientServer);

        }
    }

    @Override
    public void run() {
        try {
            setUpConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



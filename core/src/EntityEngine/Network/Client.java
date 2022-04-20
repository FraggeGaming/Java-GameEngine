package EntityEngine.Network;
import java.net.Socket;


public class Client {
    Socket socket;
    public boolean isHost;
    public Client(Socket socket){
        this.socket = socket;

    }

}

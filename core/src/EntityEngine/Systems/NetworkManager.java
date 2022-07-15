package EntityEngine.Systems;

import EntityEngine.Network.*;
import EntityEngine.NetworkClient.Client;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class NetworkManager extends System{

    Host host;
    Client client;
    String ip;
    int port;
    public boolean isHost;
    BlockingQueue<Packet> queueOut = new ArrayBlockingQueue<>(1024);
    BlockingQueue<Packet> queueIn = new ArrayBlockingQueue<>(1024);
    public boolean isOpen = false;
    ClientUpdate clientUpdate;
    public NetworkManager(){

    }

    public void addClientOnUpdate(ClientUpdate client){
        this.clientUpdate = client;
        clientUpdate.addEngine(engine);
        clientUpdate.addNetwork(this);
    }

    public void giveData(Packet data){

        try {
            queueOut.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void openNetwork(boolean isHost, String ip, int port){
        this.port = port;
        this.ip = ip;
        this.isHost = isHost;
        if (isHost) {
            host = new Host(port);
            engine.threadPool.submit(host);
        }


        client = new Client(ip, port, queueOut, queueIn, isHost);
        engine.threadPool.submit(client);

        isOpen = true;

    }

    @Override
    public void update(float dt) {
        if (isOpen){
            clientUpdate.update();
        }

    }

    private NetWorkData applyDataAsHost(NetWorkData fromJson) {

        return fromJson;
    }


    public Packet getData(){
        Packet p = null;
        try {
             while(!queueIn.isEmpty()){
                 p = queueIn.take();
            }

             if (p != null)
                 return p;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

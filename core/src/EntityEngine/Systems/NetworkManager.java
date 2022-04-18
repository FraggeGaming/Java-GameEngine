package EntityEngine.Systems;

import EntityEngine.Components.TransformComponent;
import EntityEngine.Entity;
import EntityEngine.Network.*;
import EntityEngine.NetworkClient.Client;
import TestFiles.scripts.Data;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.google.gson.Gson;

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
    boolean isOpen = false;


    ClientUpdate clientUpdate;
    NetWorkData data;
    Gson gson = new Gson();
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
            engine.pool.submit(host);
        }


        client = new Client(ip, port, queueOut, queueIn, isHost);
        engine.pool.submit(client);

        isOpen = true;

    }

    @Override
    public void update(float dt) {
        if (isOpen){
            /*if (isHost){
                Packet p = getData();
                if (p != null) {
                    giveData(p);
                }

                    /*}String s = p.toString();
                    if (s!=null){
                        NetWorkData data = gson.fromJson(s, Data.class);

                        //Handle data


                        s = gson.toJson(data, Data.class);
                        if (s != null)
                            giveData(new Packet(s));
                    }

                }
            }*/


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

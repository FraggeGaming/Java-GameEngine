package EntityEngine.Systems;

import EntityEngine.Components.TransformComponent;
import EntityEngine.Entity;
import EntityEngine.Network.Host;
import EntityEngine.Network.Packet;
import EntityEngine.NetworkClient.Client;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

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


    Entity e;
    public NetworkManager(){

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

    }

    @Override
    public void update(float dt) {
        if (isHost){
            //get request
            String s = getData().toString();
            if (s!=null)
                java.lang.System.out.println(s);

            //if can apply
            applyData(s);
            //apply the input request

            //send the correct and checked request to other players
            //giveData(new NetworkData(s));

            if (Gdx.input.isKeyJustPressed(Input.Keys.Q)){
                e = engine.getEntity("Player");
                TransformComponent c = (TransformComponent) e.getComponent(TransformComponent.class);
                giveData(new Packet(c.toString()));
            }
            //Send reply to all other clients
        }

        else {
            String s = getData().toString();
            if (s!=null)
                java.lang.System.out.println(s);
            applyData(s);
            //get demand from host and apply

            //Send new request to host
            if (Gdx.input.isKeyJustPressed(Input.Keys.Q)){
                e = engine.getEntity("Player");
                TransformComponent c = (TransformComponent) e.getComponent(TransformComponent.class);
                giveData(new Packet(c.toString()));
            }
        }
    }

    private void applyData(String s) {
        //pars string to json and do things
    }

    public Packet getData(){
        try {
            if (!queueIn.isEmpty()){
                return queueIn.take();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package EntityEngine.Network;

import EntityEngine.Engine;
import EntityEngine.Systems.NetworkManager;
import com.google.gson.Gson;

public class ClientUpdate {
    public NetworkManager network;
    public Gson gson = new Gson();
    public Engine engine;
    public ClientUpdate(){
    }

    public void update(){
    }

    public void addNetwork(NetworkManager network){
        this.network = network;
    }

    protected NetWorkData getData(Class<? extends NetWorkData> netWorkData){
        Packet p = network.getData();
        if (p != null){
            String s = p.toString();
            if (s!=null){
                return gson.fromJson(s , netWorkData);
            }

        }
        return null;
    }

    protected void giveData(NetWorkData data, Class<? extends NetWorkData> dataClass){
        String s = gson.toJson(data, dataClass);




        network.giveData(new Packet(s));
    }

    public void addEngine(Engine engine) {
        this.engine = engine;
    }

    public Class<?extends NetWorkData> getDataClass(){

        return NetWorkData.class;
    }
}



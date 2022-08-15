package TestFiles.scripts.Network;

import EntityEngine.Components.TransformComponent;
import EntityEngine.Utils.Entity;
import EntityEngine.Network.NetWorkData;

public class Data extends NetWorkData {
    public TransformComponent component;
    public int entityID;
    public String tag;
    public Data(){

    }

    public void addComponent(TransformComponent component){
       this.component = component;
    }

    public void setTag(String tag){
        this.tag = tag;
    }

    public void setId(int i){
        entityID = i;
    }

    public void addEntity(Entity e){

    }


}

package TestFiles.scripts.Network;

import EntityEngine.Components.Component;
import EntityEngine.Components.TransformComponent;
import EntityEngine.Components.VelocityComponent;
import EntityEngine.Entity;
import EntityEngine.Network.NetWorkData;
import com.badlogic.gdx.utils.Array;

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

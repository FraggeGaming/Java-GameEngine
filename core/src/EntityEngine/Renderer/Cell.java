package EntityEngine.Renderer;

import EntityEngine.Components.CollisionComponent;
import EntityEngine.Components.Component;
import EntityEngine.Components.TransformComponent;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;
import java.util.HashMap;

public class Cell {

    Array<TransformComponent> components = new Array<>();
    Array<CollisionComponent> collision = new Array<>();
    HashMap<Integer ,Array<TransformComponent>> layers = new HashMap<>(); //TODO mby delete

    String key;
    int cacheID;
    public float order = 0;

    public Cell(String key){
        this.key = key;
    }

    public void setComponents(Array<TransformComponent> components) {
        this.components = components;
    }

    public Array<TransformComponent> getComponents(){
        return components;
    }

    public HashMap<Integer ,Array<TransformComponent>> getLayers(){
        return layers;
    }

    public void addToCell(TransformComponent component){
        components.add(component);


        if (layers.get((int) component.getZ()) == null)
            layers.put((int)component.getZ(), new Array<TransformComponent>());

        else{
            layers.get((int) component.getZ()).add(component);
        }
    }

    public String getKey(){
        return key;
    }

    public void removeComponent(TransformComponent component){

        for (int i = 0; i < components.size; i++){
            if (components.get(i) != null && components.get(i).getId() == component.getId()){
                components.removeIndex(i);
                break;
            }

        }

        Array<TransformComponent> t = layers.get((int) component.getZ());
        if (t == null)
            return;
        for (int i = 0; i < t.size; i++){
            if (t.get(i) != null && t.get(i).getId() == component.getId()){
                t.removeIndex(i);
                break;
            }

        }

    }

    public void addToCell(CollisionComponent component){
        collision.add(component);

    }

    public Array<CollisionComponent> getCollisions(){
        return collision;
    }

    public void removeCollisionComponent(CollisionComponent component){
        for (int i = 0; i < collision.size; i++){
            if (collision.get(i) != null && collision.get(i).getId() == component.getId()){
                collision.removeIndex(i);
                break;
            }

        }
    }


    public void setCacheID(int id){
        this.cacheID = id;
    }

    public int getCacheID() {
        return cacheID;
    }
}

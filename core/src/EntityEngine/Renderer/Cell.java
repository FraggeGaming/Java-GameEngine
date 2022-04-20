package EntityEngine.Renderer;

import EntityEngine.Components.CollisionComponent;
import EntityEngine.Components.Component;
import EntityEngine.Components.TransformComponent;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

public class Cell {

    Array<TransformComponent> components = new Array<>();
    Array<CollisionComponent> collision = new Array<>();

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

    public void addToCell(TransformComponent component){
        components.add(component);
        sortComponents();

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
    }

    public void addToCell(CollisionComponent component){
        collision.add(component);

    }

    public  Array<CollisionComponent> getCollisions(){
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

    public void sortComponents(){
        components.sort(new TransformComparator());

    }

    public void setCacheID(int id){
        this.cacheID = id;
    }

    public int getCacheID() {
        return cacheID;
    }
}

package EntityEngine.Renderer;

import EntityEngine.Components.CollisionComponent;
import EntityEngine.Components.Component;
import EntityEngine.Components.TransformComponent;
import EntityEngine.Entity;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class Cell {

    Array<TransformComponent> components = new Array<>();
    Array<CollisionComponent> collision = new Array<>();
    HashMap<Integer ,Array<TransformComponent>> layers = new HashMap<>(); //TODO mby delete

    //TODO fix layered version
    Array<HashMap<Integer, Component>> parsedComponents = new Array<>();
    Array<Component> componentTypes = new Array<>();
    HashMap<Integer, Component> tempMap;

    String key;
    public float order = 0;

    boolean parseComponents;

    public Cell(String key, boolean threadedParsing){
        this.key = key;
        parseComponents = !threadedParsing;
    }

    public void setComponents(Array<TransformComponent> components) {
        this.components = components;
    }

    public Array<TransformComponent> getComponents(){
        return components;
    }

    public Component[] getComponents(Class<?extends Component> component){
        for (int i = 0; i < componentTypes.size; i++){
            if (componentTypes.get(i).getClass().equals(component)){
                return parsedComponents.get(i).values().toArray(new Component[0]);
            }
        }
        return null;
    }

    public HashMap<Integer ,Array<TransformComponent>> getLayers(){
        return layers;
    }

    public void addToCell(TransformComponent component, Entity entity){
        components.add(component);

        if (parseComponents)
            parseComponent(entity);
    }

    public void parseComponent(Entity e){
        for (int k = 0; k < e.components.size; k++){

            Component component = e.components.get(k);

            for(int j = 0; j < componentTypes.size; j++){
                if (componentTypes.get(j).getClass().equals(component.getClass())){
                    parsedComponents.get(j).put(component.getId(), component);
                    component = null;
                    break;
                }
            }

            if (component != null){
                tempMap = new HashMap<>();
                tempMap.put(component.getId(),component);
                parsedComponents.add(tempMap);
                componentTypes.add(component);
            }
        }
    }

    public void removeComponent(TransformComponent component, Entity entity){

        for (int i = 0; i < components.size; i++){
            if (components.get(i) != null && components.get(i).getId() == component.getId()){
                components.removeIndex(i);
                break;
            }
        }

        if (parseComponents){
            for (int k = 0; k < entity.components.size; k++){
                removeEntityFromMap(entity.components.get(k));
            }


        }
    }

    private void removeEntityFromMap(Component component){
        for (int i = 0; i < componentTypes.size; i++){
            if (componentTypes.get(i).getClass().equals(component.getClass())){
                parsedComponents.get(i).remove(component.getId());
                return;
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

}

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
    String key;
    public float order = 0;

    public Cell(String key){
        this.key = key;
    }

    public Array<? extends Component> getComponents(Class<?extends Component> componentClass){
        if (componentClass.equals(TransformComponent.class))
            return components;
        else if (componentClass.equals(CollisionComponent.class))
            return collision;

        return null;
    }

    public void addToCell(Component component){

        if (component.getClass().equals(TransformComponent.class))
            components.add((TransformComponent) component);
        else if (component.getClass().equals(CollisionComponent.class))
            collision.add((CollisionComponent) component);

    }

    public void removeComponent(Component component){


        if (component.getClass().equals(TransformComponent.class)){

            for (int i = 0; i < components.size; i++){
                if (components.get(i) != null && components.get(i).getId() == component.getId()){
                    components.removeIndex(i);
                    break;
                }
            }
        }

        else if (component.getClass().equals(CollisionComponent.class)){
            for (int i = 0; i < collision.size; i++){
                if (collision.get(i) != null && collision.get(i).getId() == component.getId()){
                    collision.removeIndex(i);
                    break;
                }

            }
        }

    }

}

package EntityEngine;

import EntityEngine.Components.Component;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class Entity {
    public Array<Component> components = new Array<>();
    public boolean flagForDelete = false;
    public int id;
    public String tag;
    private Array<String> tags = new Array<>();
    Component c;
    public Entity(){

    }

    public void addComponents(Component component){
        components.add(component);
        component.setId(id);
    }

    private void setComponentId(){
        for (int i = 0; i < components.size; i++){
            components.get(i).setId(id);
        }
    }

    public Component getComponent(Class<?extends Component> component){


        for (int i = 0; i < components.size; i++){
            c = components.get(i);
            if (c.getClass().equals(component))
                return c;
        }

        return null;
    }

    public void setTags(String tag){
        tags.add(tag);
    }

    public void setComponents(Array<Component> components) {
        this.components = components;
    }

    public void setId(int id, HashMap<Integer, Entity> componentMapper){
        this.id = id;
        setComponentId();

        componentMapper.put(id, this);
    }

    public void removeComponent(Class<?extends Component> component){
        for (int i = 0; i < components.size; i++){
            if (components.get(i).getClass() == component){
                components.removeIndex(i);
                return;

            }
        }
    }
}

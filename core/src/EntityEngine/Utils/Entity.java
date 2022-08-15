package EntityEngine.Utils;

import EntityEngine.Components.Component;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Objects;

public class Entity {
    public Array<Component> components = new Array<>();
    public HashMap<Class<?extends Component>, Component> componentHashMap = new HashMap<>(15, 0.7f);
    public boolean flagForDelete = false;
    public int id;
    public String name;
    private Tags tags;
    Component c;
    public Entity(){

    }

    public void addComponents(Component component){
        components.add(component);
        component.setId(id);
        componentHashMap.put(component.getClass(), component);
    }

    private void setComponentId(){
        for (int i = 0; i < components.size; i++){
            components.get(i).setId(id);
        }
    }

    public Component getComponent(Class<?extends Component> component){

        //return componentHashMap.get(component);

        for (int i = 0; i < components.size; i++){
            c = components.get(i);
            if (c.getClass().equals(component))
                return c;
        }

        return null;
    }

    public Tags getTags(){
        return tags;
    }

    public void setComponents(Array<Component> components) {
        this.components = components;
    }

    public void setId(int id){
        this.id = id;
        setComponentId();


    }

    public void removeComponent(Class<?extends Component> component){
        for (int i = 0; i < components.size; i++){
            if (components.get(i).getClass() == component){
                components.removeIndex(i);
                return;

            }
        }

        componentHashMap.remove(component);
    }

    public void dispose() {
        for (int i = 0; i < components.size; i++){
            components.get(i).dispose();
        }

        //componentHashMap = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;
        Entity entity = (Entity) o;
        return id == entity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

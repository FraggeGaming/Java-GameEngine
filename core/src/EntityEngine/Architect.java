package EntityEngine;

import EntityEngine.Components.Component;
import EntityEngine.Components.TextureComponent;
import EntityEngine.Components.TransformComponent;
import com.badlogic.gdx.utils.Array;

public class Architect {
    byte id;
    int size = 2;
    Array<Array<Component>> components = new Array<>();
    public Array<TransformComponent> transforms = new Array<>();
    public Array<TextureComponent> textures = new Array<>();
    Array<Class<?extends Component>> types;

    boolean sync = false;

    public Architect(byte id , Array<Class<?extends Component>> types){
        this.types = types;
        this.id = id;
        size = types.size;
        for (int i = 0; i < types.size; i++){
           components.add(new Array<Component>());
        }

    }

    public void getComponents(Entity entity){
        for (int i = 0; i < types.size; i++){
            if (entity.getComponent(types.get(i)) == null)
                return;
        }

        for (int i = 0; i < entity.components.size; i++){
            Component c = entity.components.get(i);

            for (int j = 0; j < types.size; j++){
                if (c.getClass().equals(types.get(j))){
                    c.setArchitectMapper(id, components.get(j).size);
                    components.get(j).add(c);
                }
            }
        }
    }

    public Array<Component> getComponents(Class<?extends Component> type){
        for (int i = 0; i < components.size; i++){
            if (!components.get(i).isEmpty() && components.get(i).get(0).getClass() == type)
                return components.get(i);
        }

        return null;
    }

    public void removeEntity(Entity entity){
        for (int i = 0; i < entity.components.size; i++){
            Component c = entity.components.get(i);

            for (int j = 0; j < types.size; j++){
                if (c.getClass().equals(types.get(j))){
                    int cId = c.getArchitectID(id);
                    components.get(j).removeIndex(cId);
                    sync = true;
                }
            }
        }

        syncArchitect();
    }

    public void syncArchitect(){
        if (sync){
            for (int i = 0 ; i < components.size; i++){
                updateComponentValues(components.get(i));
            }

            sync = false;
        }


    }

    private void updateComponentValues(Array<Component> components) {

        for (int i = 0; i < components.size; i++){
            components.get(i).updateArchitectId(id, i);
        }
    }
}

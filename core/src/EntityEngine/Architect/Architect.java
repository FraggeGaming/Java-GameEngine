package EntityEngine.Architect;

import EntityEngine.Components.Component;
import EntityEngine.Entity;
import com.badlogic.gdx.utils.Array;

public class Architect {
    public byte id;
    Array<Array<Component>> components = new Array<>();
    Type type;

    boolean sync = false;

    public Architect(byte id , Type type){
        this.type = type;
        this.id = id;

        for (int i = 0; i < type.types.size; i++){
           components.add(new Array<Component>());
        }

    }

    public void getComponents(Entity entity){
        for (int i = 0; i <  type.getSize(); i++){
            if (entity.getComponent( type.getType(i)) == null)
                return;
        }

        for (int i = 0; i < entity.components.size; i++){
            Component c = entity.components.get(i);

            for (int j = 0; j < type.getSize(); j++){
                if (c.getClass().equals( type.getType(j))){
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

            for (int j = 0; j <  type.getSize(); j++){
                if (c.getClass().equals( type.getType(j))){
                    int cId = c.getArchitectID(id);
                    if (cId > -1){
                        components.get(j).removeIndex(cId);
                        sync = true;

                    }
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

    //TODO optimize this
    private void updateComponentValues(Array<Component> components) {

        for (int i = 0; i < components.size; i++){
            components.get(i).updateArchitectId(id, i);
        }
    }
}

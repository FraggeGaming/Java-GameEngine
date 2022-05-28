package EntityEngine;
import EntityEngine.Components.Component;
import com.badlogic.gdx.utils.Array;

public class ArchitectHandler {

    Array<Architect> architects = new Array<>();
    byte id = 0x1;

    public void createArchitect(Array<Class<?extends Component>> types){
        Architect architect = new Architect(id, types);
        architects.add(architect);

        id++;
    }

    public void addToArchitect(Entity entity){
        for (Architect a : architects){
            a.getComponents(entity);
        }
    }

    public Architect getArchitect(byte b){
        for (Architect architect : architects){
            if (architect.id == b){
                return architect;
            }
        }
        return null;
    }

    public void removeEntity(Entity entity){
        for (int i = 0; i < architects.size; i++){
            architects.get(i).removeEntity(entity);
        }
    }
}

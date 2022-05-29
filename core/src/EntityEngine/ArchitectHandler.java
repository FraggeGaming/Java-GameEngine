package EntityEngine;
import com.badlogic.gdx.utils.Array;

public class ArchitectHandler {

    Array<Architect> architects = new Array<>();
    byte id = 0x1;

    public void createArchitect(Type type){
        Architect architect = new Architect(id, type);
        architects.add(architect);

        id++;
    }

    public void addToArchitect(Entity entity){
        for (Architect a : architects){
            a.getComponents(entity);
        }
    }

    public Architect getArchitect(Type type){
        for (Architect architect : architects){
            if (architect.type.getSize() == type.getSize()){
                if (architect.type.equals(type)){
                    return architect;
                }

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

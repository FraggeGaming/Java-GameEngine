package EntityEngine.Systems;

import EntityEngine.Components.Component;
import EntityEngine.Components.TransformComponent;
import EntityEngine.Entity;
import EntityEngine.Renderer.Cell;
import EntityEngine.Renderer.TransformComparator;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ComponentManagerSystem extends System {
    Array<Cell> loadedCells;
    private int loadedCellsID = 0;
    Array<Array<Component>> comp = new Array<>();
    Component c;
    Future< Array<Array<Component>>> components;
    @Override
    public void update(float dt) {


        try{
            if (components != null){
                if (components.isDone()){
                    comp = components.get();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }


        if (engine.getSpatialHashGrid().getLoadedCellID() != loadedCellsID){
            loadedCells = engine.getCellsFromCameraCenter();
            components = engine.pool.submit(new ComponentCalculation(loadedCells, engine.componentMap));
            loadedCellsID = engine.getSpatialHashGrid().getLoadedCellID();
        }
    }

    public Array<Component> getLoadedComponents(Class<?extends Component> component){
        for (int i = 0; i < comp.size; i++){
            if (!comp.get(i).isEmpty()){
                c = comp.get(i).get(0);
                if (c.getClass().equals(component)){
                   return comp.get(i);
                }
            }
        }

        return null;
    }
}

class ComponentCalculation implements Callable {
    Array<Array<Component>> comp = new Array<>();
    Array<Cell> loadedCells;
    Array<TransformComponent> temp = new Array<>();
    Array<Component> tempArray;
    Component c;
    Entity e;
    HashMap<Integer, Entity> componentMap;

    private HashSet<Integer> renderedIDs = new HashSet<>();
    Array<Integer> toRemove = new Array<>();
    public ComponentCalculation(Array<Cell> loadedCells, HashMap<Integer, Entity> entities){
        this.loadedCells = loadedCells;
        this.componentMap = entities;

        for (int i = 0; i < loadedCells.size; i++){
            temp.addAll(loadedCells.get(i).getComponents());
        }

        parseCells();

    }

    private void checkForDublicates(Class<?extends Component> component){
        tempArray = getLoadedComponents(component);
        if (tempArray == null)
            return;

        for (int i = 0; i < tempArray.size; i++){
            if(!renderedIDs.add(tempArray.get(i).getId())){
                toRemove.add(i);
            }
        }

        for (int i = 0; i < toRemove.size; i++){
            tempArray.removeIndex(toRemove.get(i) - i);
        }

        renderedIDs.clear();
        toRemove.clear();

        for (int j = 0; j < comp.size; j++){
            if (!comp.get(j).isEmpty()){
                c = comp.get(j).get(0);
                if (c.getClass().equals(component)){
                    comp.set(j, tempArray);
                }
            }
        }
    }

    private Array<Component> getLoadedComponents(Class<?extends Component> component){
        for (int i = 0; i < comp.size; i++){
            if (!comp.get(i).isEmpty()){
                c = comp.get(i).get(0);
                if (c.getClass().equals(component)){
                    return comp.get(i);
                }
            }
        }

        return null;
    }

    private void parseCells() {

        temp.sort(new TransformComparator());

        for (int i = 0; i < temp.size; i++){
            e = componentMap.get(temp.get(i).getId());
            for (int j = 0; j < e.components.size; j++){
                parseComponents(e.components.get(j));
            }
        }

        temp.clear();
    }

    public void parseComponents(Component component){
        for (int i = 0; i < comp.size; i++){
            if (!comp.get(i).isEmpty()){
                c = comp.get(i).get(0);
                if (c.getClass() == component.getClass()){
                    comp.get(i).add(component);
                    return;
                }
            }
        }


        tempArray = new Array<>();
        tempArray.add(component);
        comp.add(tempArray);

    }

    @Override
    public Array<Array<Component>> call() throws Exception {
        return comp;
    }
}

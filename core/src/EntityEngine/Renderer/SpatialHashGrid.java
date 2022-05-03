package EntityEngine.Renderer;

import EntityEngine.Components.CollisionComponent;
import EntityEngine.Components.Component;
import EntityEngine.Components.TransformComponent;
import EntityEngine.Entity;
import EntityEngine.Systems.ComponentManagerSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;

public class SpatialHashGrid  {

    private final HashMap<String, Cell> hashGrid = new HashMap();
    private Cell cell;
    TransformComponent center = new TransformComponent(0, 0, 0, 0, 0);

    private Array<Cell> loadedCells = new Array<>();
    private Array<Cell> loadedCellsTemp = new Array<>();
    boolean addCell = false;

    int cellSize = 50; // 100 / tile size = number off sprites in a cell, ex: texture size = 15, then cellSize/textureSize = 10 wide and high cell
    int offsetX = 0; //make this changeable in GUI
    int offsetY = 3;
    int radiusY;
    int radiusX;
    public boolean update = false;



    public SpatialHashGrid(){
    }

    public void setData(int height){
        radiusY = height/cellSize + offsetY;
        radiusX = radiusY*16/9 + offsetX;
    }



    public Array<Cell> getNeighbours(){

        return loadedCellsTemp;
    }

    public void calculateSpatialGrid(TransformComponent component){
            loadedCells.clear();
            getSurroundingCells(component);
            sortLoadedCells();
            center.setVec(component.getVector());
            update = true;

            loadedCellsTemp.clear();
            loadedCellsTemp.addAll(loadedCells);
    }


    private void sortLoadedCells() {
        loadedCells.sort(new CellComparator());
    }

    private void processCell(Cell cell){
        if (cell != null){
            if (loadedCells.isEmpty()){
                addCell = true;
            }

            else{
                addCell = !loadedCells.contains(cell, false);
            }

            if (addCell){
                loadedCells.add(cell);
                addCell = false;
            }

        }
    }

    private void getSurroundingCells(TransformComponent component){
        for (int i = 0; i < radiusX; i++){
            for (int j = 0; j < radiusY; j++){
                cell = findCell(component.getOriginX()/cellSize - radiusX/2f +i, component.getOriginY()/cellSize- radiusY/2f +j );
                processCell(cell);
            }
        }
    }

    private Cell findCell(float x, float y){
        return hashGrid.get((int) x + ", " + (int) y);
    }

    public Array<Cell> getCells(TransformComponent component, int radius){
        Array<Cell> cells = new Array<>();
        for (int i = 0; i < radius; i++){
            for (int j = 0; j < radius; j++){
                cell = findCell(component.getOriginX()/cellSize -  radius/2f +i, component.getOriginY()/cellSize - radius/2f +j );
                if (cell != null)
                    cells.add(cell);
            }
        }
        return cells;
    }

    public Cell getCell(TransformComponent component){

        return cell = findCell(component.getOriginX()/cellSize, component.getOriginY()/cellSize );


    }

    public void addEntity(Entity entity) {
        Component x = entity.getComponent(TransformComponent.class);
        if (x != null){
            addToGrid((TransformComponent) x);
        }

        Component y = entity.getComponent(CollisionComponent.class);
        if (y != null){
            addCollision((CollisionComponent) y);
        }
    }

    private void addCollision(CollisionComponent x) {
        Array<String> keys = getAllKeys(x);
        for (int i = 0; i < keys.size; i++){
            cell = hashGrid.get(keys.get(i));
            if (cell == null){
                cell = new Cell(keys.get(i));
            }

            cell.addToCell(x);

            hashGrid.put(keys.get(i),cell);

        }

    }

    private void addToGrid(TransformComponent component){

        cell = hashGrid.get(getKey(component));
        if (cell == null){
            cell = new Cell(getKey(component));
        }

        cell.addToCell(component);

        if (cell != null && component.getZ() > cell.order){
            cell.order = component.getZ();
        }


        hashGrid.put(getKey(component),cell);
        //update = true;



    }

    public void removeEntity(Entity entity){

        Component x = entity.getComponent(TransformComponent.class);

        if (x != null){
            if (hashGrid.containsKey(getKey((TransformComponent) x))){
                hashGrid.get(getKey((TransformComponent) x)).removeComponent((TransformComponent) x);

            }
        }

        Component y = entity.getComponent(CollisionComponent.class);
        if (y != null){
            Array<String> keys = getAllKeys((CollisionComponent) y);
            for (int i = 0; i < keys.size; i++){
                if (hashGrid.containsKey(keys.get(i))){
                    hashGrid.get(keys.get(i)).removeCollisionComponent((CollisionComponent) y);
                }
            }
        }
    }


    private String getKey(TransformComponent component){
        return  (int)(component.getOriginX() / cellSize) + ", " + (int)(component.getOriginY() / cellSize);
    }

    private String getStringKey(float x, float y){
        return  (int)(x / cellSize) + ", " + (int)(y / cellSize);
    }
    //use this for collision
    private Array<String> getAllKeys(CollisionComponent component){
        Array<String> keys = new Array<>();
        String s;

        for (int i = 0; i < component.verticies.size; i += 2){
            s = getStringKey(component.verticies.get(i), component.verticies.get(i+1));

            if (!keys.contains(s, false)){
                keys.add(s);
            }
        }

        return keys;


    }


    private double distance(Vector3 u, Vector3 v){
        return Math.sqrt((u.x - v.x)*(u.x - v.x) + (u.y - v.y)*(u.y - v.y));
    }

    //Returns false if greater than max
    private boolean greaterThan(int x1, int x2, int maxDistance){
        return Math.abs(x1 - x2) > maxDistance;
    }
}

package EntityEngine.Systems;

import EntityEngine.Components.CollisionComponent;
import EntityEngine.Renderer.Cell;
import com.badlogic.gdx.utils.Array;

import java.util.HashSet;
import java.util.concurrent.*;


public class CollisionDetectionSystem extends System{

    Array<Cell> loadedCells;
    public HashSet<CollisionComponent> collisions = new HashSet<>();
    CollisionComponent c1;
    CollisionComponent c2;
    float stepValue = 60;
    float timeStep = 0;
    int id1, id2;
    int collidableComponents = 0;
    int detectedCollisions = 0;
    int collisionDebugValue = 0;
    int collidableComponentsDebug = 0;
    CollisionContainer container;
    Future<CollisionContainer> computedCollisions;

    boolean calculateCollisions = true;

    Array<Cell> loadedCellsTemp = new Array<>();
    public CollisionDetectionSystem(){


    }

    @Override
    public void update(float dt) {

        //collisionMultiThread(dt);

        collisionNonThreaded(dt);


    }

    private void collisionNonThreaded(float dt) {
        collisions = new HashSet<>();

        loadedCells = engine.getCellsFromCameraCenter();
        collidableComponents = 0;
        detectedCollisions = 0;

        for (int j = 0; j < loadedCells.size; j++){
            calculateCollisions(loadedCells.get(j));
        }

        for (CollisionComponent c : collisions){
            c.setNewCollisions();
        }


    }

    private void collisionMultiThread(float dt){


        try{
            if (computedCollisions != null){
                if (computedCollisions.isDone()){
                    container = computedCollisions.get();


                    collisionDebugValue = container.collisionDebugValue;
                    collidableComponentsDebug = container.collidableComponentsDebug;
                    collisions = container.collisions;

                    for (CollisionComponent c : collisions){
                        c.setNewCollisions();
                    }

                    calculateCollisions = true;

                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        if (calculateCollisions){


            loadedCells = engine.getCellsFromCameraCenter();


            computedCollisions = engine.pool.submit(new CollisionCalculation(loadedCells));
            calculateCollisions = false;
        }

    }

    private void calculateCollisions(Cell cell) {


        for (int i = 0; i < cell.getComponents(CollisionComponent.class).size; i++){

            c1 = (CollisionComponent) cell.getComponents(CollisionComponent.class).get(i);
            if (c1 != null && !c1.isSleeping()){
                collidableComponents++;

                for (int j = 0; j < cell.getComponents(CollisionComponent.class).size; j++){

                    if (i != j){

                        c2 = (CollisionComponent) cell.getComponents(CollisionComponent.class).get(j);

                        //TODO fix exeption bugg
                        //TODO add velocity vector to collision component and use that to check if next frame collisions will jump over a box.
                        if (c2 != null && !c2.isSleeping() && !isStatic(c1, c2) && !c2.newCollisions.contains(c1)){

                            if (canCollide(c1, c2) && c1.overlaps(c2)){
                                c1.addCollision(c2);
                                c2.addCollision(c1);
                                collisions.add(c1);
                                collisions.add(c2);

                                detectedCollisions++;
                            }
                        }
                    }
                }
            }
        }


        collidableComponentsDebug =+ collidableComponents;
        collisionDebugValue =+ detectedCollisions;
    }

    private boolean canCollide(CollisionComponent c1, CollisionComponent c2){
        //if their filter are compatible, then collide
        return true;
    }

    private boolean isStatic(CollisionComponent c1, CollisionComponent c2){
        return c1.isStatic && c2.isStatic;
    }

    public int getNumOfCollisions(){
        return collisionDebugValue;
    }

    public int getCollidableComponentsDebug() {
        return collidableComponentsDebug;
    }

    public boolean CollisionBetweenComponents(CollisionComponent c1, CollisionComponent c2){
        if (collisions.contains(c1)){
            if (c1.collisions.contains(c2)){
                return true;
            }
        }

        return false;
    }

    public boolean CollisionWithID(CollisionComponent c, String id){
        if (collisions.contains(c)){
            for (CollisionComponent c1 : c.collisions){

                if (c1.id != null && c1.id.equals(id)){
                    return true;
                }
            }
        }

        return false;
    }

    public Array<CollisionComponent> getCollisision(CollisionComponent c, String id){
        Array<CollisionComponent> comp = new Array<>();
        if (collisions.contains(c)){
            for (CollisionComponent c1 : c.collisions){

                if (c1.id != null && c1.id.equals(id)){
                   comp.add(c1);
                }
            }
        }

        return comp;
    }

    public CollisionComponent getCollisisionFirst(CollisionComponent c, String id){

        if (collisions.contains(c)){
            for (CollisionComponent c1 : c.collisions){

                if (c1.id.equals(id)){
                    return c1;
                }
            }
        }

        return null;
    }

}

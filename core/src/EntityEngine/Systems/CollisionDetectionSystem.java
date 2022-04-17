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

    private int loadedCellsID = 0;
    public CollisionDetectionSystem(){


    }

    @Override
    public void update(float dt) {

        collisionMultiThread(dt);

        //collisionNonThreaded(dt);


    }

    private void collisionMultiThread(float dt){
        loadedCells = engine.getCellsFromCameraCenter();

        //gett loaded collision cells


        timeStep += dt;


        if (timeStep >= 1/stepValue){


            lookForNewCollisions();

            loadedCells = engine.getCellsFromCameraCenter();
            computedCollisions = engine.pool.submit(new CollisionCalculation(loadedCells, engine.componentMap));


            timeStep = 0;




        }

        try{
            if (computedCollisions != null){
                if (computedCollisions.isDone()){
                    container = computedCollisions.get();

                    collisionDebugValue = container.collisionDebugValue;
                    collidableComponentsDebug = container.collidableComponentsDebug;
                    collisions = container.collisions;

                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void collisionNonThreaded(float dt){
        timeStep += dt;

        if (timeStep >= 1/stepValue){

            lookForNewCollisions();
            timeStep = 0;
        }

        if (timeStep >= 1/stepValue/2){
            collidableComponents = 0;
            loadedCells = engine.getCellsFromCameraCenter();
            for (int i = 0; i < loadedCells.size; i++){
                calculateCollisions(loadedCells.get(i));
            }
        }
    }

    private void calculateCollisions(Cell cell) {
        for (int i = 0; i < cell.getComponents().size; i++){
            id1 = cell.getComponents().get(i).getId();
            c1 = (CollisionComponent) engine.getEntityComponent(id1, CollisionComponent.class);
            if (c1 != null){
                collidableComponents++;

                for (int j = 0; j < cell.getComponents().size; j++){

                    if (i != j){
                        id2 = cell.getComponents().get(j).getId();
                        c2 = (CollisionComponent) engine.getEntityComponent(id2, CollisionComponent.class);

                        if (c2 != null && !isStatic(c1, c2) && !c2.collisions.contains(c1)){

                            if (c1.overlaps(c2)){
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


        collidableComponentsDebug = collidableComponents;
        collisionDebugValue = detectedCollisions;
    }

    private boolean isStatic(CollisionComponent c1, CollisionComponent c2){
        return c1.isStatic && c2.isStatic;
    }

    private void lookForNewCollisions(){
        for (CollisionComponent c : collisions){
            c.clearCollisionData();
        }

        detectedCollisions = 0;
        collisions.clear();
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

                if (c1.id.equals(id)){
                    return true;
                }
            }
        }

        return false;
    }
}

package EntityEngine.Systems;

import EntityEngine.Components.CollisionComponent;
import EntityEngine.Components.Component;
import EntityEngine.Engine;
import EntityEngine.Entity;
import EntityEngine.Renderer.Cell;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import javax.swing.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class CollisionCalculation implements Callable {
    Array<Cell> loadedCells;
    public HashSet<CollisionComponent> collisions = new HashSet<>();
    CollisionComponent c1;
    CollisionComponent c2;
    int detectedCollisions = 0;
    int collisionDebugValue = 0;
    int collidableComponents = 0;
    int collidableComponentsDebug = 0;


    public CollisionCalculation(Array<Cell> cells){
        this.loadedCells = cells;

    }


    public void run() {

        for (int j = 0; j < loadedCells.size; j++){
            try {
                calculateCollisions(loadedCells.get(j));
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }


    private void calculateCollisions(Cell cell) throws ExecutionException {
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

        collidableComponentsDebug = collidableComponents;
        collisionDebugValue = detectedCollisions;

    }

    private boolean isStatic(CollisionComponent c1, CollisionComponent c2){
        return c1.isStatic && c2.isStatic;
    }

    private boolean canCollide(CollisionComponent c1, CollisionComponent c2){
        //if their filter are compatible, then collide
        return true;
    }

    @Override
    public CollisionContainer call() throws Exception {

        run();
        return new CollisionContainer(collisions, collisionDebugValue, collidableComponentsDebug);
    }
}

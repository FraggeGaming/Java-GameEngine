package EntityEngine.Systems;

import EntityEngine.Components.CollisionComponent;

import java.util.HashSet;

public class CollisionContainer {

    public HashSet<CollisionComponent> collisions;
    public int collisionDebugValue;
    public int collidableComponentsDebug;
    public CollisionContainer(HashSet<CollisionComponent> collisions, int collisionDebugValue,int collidableComponentsDebug){
        this.collisions = collisions;
        this.collidableComponentsDebug = collidableComponentsDebug;
        this.collisionDebugValue = collisionDebugValue;
    }
}

package EntityEngine.Components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.HashSet;

public class CollisionComponent extends Component{

    public Rectangle boundingBox;
    boolean sleeping = false;
    public boolean flag = false;
    public boolean isStatic = false;
    public HashSet<CollisionComponent> collisions = new HashSet<>();

    public HashSet<CollisionComponent> newCollisions = new HashSet<>();
    public Array<Float> verticies = new Array<>(7);

    public String id;

    //Filter class?
    //Component has a filter that user can assign filter to
    //This components collision id (collisionID)
    //What this component can collide with (collideGroup)


    public CollisionComponent(TransformComponent t){
        boundingBox = new Rectangle(t.getX(), t.getY(), t.getWidth(), t.getHeight());
        verticies.add(0f, 0f, 0f, 0f);
        verticies.add(0f, 0f, 0f, 0f);

        updateVerticies();

        seperate = true;
    }

    private void updateVerticies(){
        verticies.set(0, boundingBox.x);
        verticies.set(1, boundingBox.y);


        verticies.set(2, boundingBox.x);
        verticies.set(3, boundingBox.y + boundingBox.getHeight());

        verticies.set(4, boundingBox.x + boundingBox.getWidth());
        verticies.set(5,boundingBox.y + boundingBox.getHeight());

        verticies.set(6, boundingBox.x + boundingBox.getWidth());
        verticies.set(7, boundingBox.y);

    }
    public CollisionComponent(float x, float y, float width, float height){
        boundingBox = new Rectangle(x, y, width, height);
        verticies.add(0f, 0f, 0f, 0f);
        verticies.add(0f, 0f, 0f, 0f);
        updateVerticies();

        seperate = true;
    }

    public void followTransform(TransformComponent t){
        boundingBox.setPosition(t.getX(), t.getY());

        updateVerticies();

    }

    public boolean overlaps(CollisionComponent component){
        return boundingBox.overlaps(component.boundingBox);
    }

    public void setSleeping(boolean sleeping){
        this.sleeping = sleeping;
    }

    public boolean isSleeping(){
        return sleeping;
    }

    public void addCollision(CollisionComponent id){
        newCollisions.add(id);

    }

    public void setNewCollisions(){
        if (newCollisions.isEmpty())
            return;
        collisions.clear();
        collisions.addAll(newCollisions);
        newCollisions.clear();
    }

    public void clearCollisionData(){
        collisions.clear();

    }

}

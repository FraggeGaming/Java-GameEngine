package EntityEngine.Components;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class TransformComponent extends Component {

    Vector3 vec = new Vector3();
    Vector3 dim = new Vector3();
    public float rotation = 0;

    public TransformComponent(float x, float y, float z, float width, float height){

        dim.x = width;
        dim.y = height;
        vec.set(x, y, z);

    }

    public TransformComponent(float x, float y, float z, float width, float height, float rotation){

        dim.x = width;
        dim.y = height;
        vec.set(x, y, z);

        this.rotation = rotation;

    }


    public float getX(){
        return vec.x;
    }

    public float getY(){
        return vec.y;
    }

    public float getZ(){
        return vec.z;
    }

    public float getWidth(){
        return dim.x;
    }

    public float getHeight(){
        return dim.y;
    }

    public Vector3 getVector(){
        return vec;
    }

    public void setVec(Vector3 vec){
        this.vec.set(vec);

    }

    public void setPosition(float x, float y){
        vec.x = x;
        vec.y = y;


    }

    public void translate(float x, float y){
        vec.x += x;
        vec.y += y;


    }



    public void addVelocity(VelocityComponent v){
        vec.add(v.vel);


    }

    public void addVelocity(Vector3 v){
        vec.add(v);


    }
    public float getOriginX(){
        return (vec.x + dim.x/2);
    }

    public float getOriginY(){

        return (vec.y + dim.y/2);
    }

    public boolean equals(TransformComponent transformComponent){
        return transformComponent.vec.equals(vec);
    }

    public String toString(){
        return vec.toString();
    }


    public float getRotation() {
        return rotation;
    }
}

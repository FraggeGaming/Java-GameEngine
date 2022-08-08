package EntityEngine.Components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class UIElement extends Component{

    Vector3 vec = new Vector3();
    Vector3 dim = new Vector3();
    Vector2 scale = new Vector2();
    public float rotation = 0;

    public UIElement(float x, float y, float z, float width, float height){

        dim.x = width;
        dim.y = height;
        vec.set(x, y, z);
        scale.set(1, 1);

    }

    public UIElement(float x, float y, float z, float width, float height, float rotation){

        dim.x = width;
        dim.y = height;
        vec.set(x, y, z);

        this.rotation = rotation;

    }

    public void mirror(boolean x, boolean y){
        if (x)
            scale.x *= -1;
        if (y)
            scale.y *= -1;

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

    public float getScaleX(){
        return scale.x;
    }

    public float getScaleY(){
        return scale.y;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
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

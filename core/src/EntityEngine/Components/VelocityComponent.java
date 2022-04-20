package EntityEngine.Components;

import com.badlogic.gdx.math.Vector3;

public class VelocityComponent extends Component{
    public Vector3 vel = new Vector3(0, 0, 0);
    public VelocityComponent(){

    }

    public void setVelocity(float x, float y, float z){
        vel.set(x,y,z);
    }

    public void addVelocity(float x, float y, float z){
        vel.add(x, y, z);
    }
    public void scaleVelocity(float scale){
        vel.scl(scale);
    }
}

package EntityEngine.Components;

import box2dLight.PositionalLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;

public class Light extends Component{
    PositionalLight light;
    boolean giveLight = true;
    public Light(PositionalLight light){
        seperate = true;
        this.light = light;
        light.setActive(false);
    }

    public void colorAndSoftnessLength(Color newColor, float softShadow){
        if (newColor != null)
            light.setColor(newColor);
        if (softShadow != -1)
            light.setSoftnessLength(softShadow);
    }

    public void setActiveByHandler(boolean active){
        if (giveLight == active)
            light.setActive(active);
    }

    public void activate(boolean state){
        giveLight = state;
    }

    public void setStatic(boolean staticLight){
        light.setStaticLight(staticLight);
    }

    public boolean isStatic(){
        return light.isStaticLight();
    }

    public boolean isActive(){
        return light.isActive();
    }

    public void attachToBody(Body body, boolean ignoreBody){
        light.attachToBody(body);
        light.setIgnoreAttachedBody(ignoreBody);

    }

    public void setXray(boolean xray){
        light.setXray(xray);

    }

    @Override
    public void dispose() {
        light.remove();
    }
}

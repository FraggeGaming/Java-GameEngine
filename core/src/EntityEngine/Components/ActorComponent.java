package EntityEngine.Components;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorComponent extends Component{
    public Actor actor;
    public boolean isSpatial = true;


    public ActorComponent(float x, float y, float width, float height) {
        actor = new Actor();
        actor.setBounds(x,y,width,height);
    }
}

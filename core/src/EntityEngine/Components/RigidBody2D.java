package EntityEngine.Components;

import com.badlogic.gdx.physics.box2d.*;

public class RigidBody2D extends Component{

    Body body;
    PolygonShape shape;
    FixtureDef fixtureDef;
    BodyDef bodyDef;
    public boolean destroyed = false;

    public RigidBody2D(float x, float y, float width, float height, int staticBody){
        seperate = true;
        bodyDef = new BodyDef();

        if (staticBody == 0){
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }

        if (staticBody == 1)
            bodyDef.type = BodyDef.BodyType.StaticBody;



        bodyDef.position.set(x , y);

        shape = new PolygonShape();
        shape.setAsBox(width, height);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0.5f;
    }

    public void addToWorld(World world){
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void setActive(boolean active){
        body.setActive(active);
    }

    public Body getBody(){
        return body;
    }
}

package EntityEngine.Components;

import com.badlogic.gdx.physics.box2d.*;

public class RigidBody2D extends Component{

    Body body;
    Shape shape;
    FixtureDef fixtureDef;
    BodyDef bodyDef;
    public boolean destroyed = false;

    PolygonShape polygonShape;
    CircleShape circleShape;


    public RigidBody2D(float x, float y, float width, float height, int staticBody){
        seperate = true;
        bodyDef = new BodyDef();

        if (staticBody == 0){
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }

        if (staticBody == 1)
            bodyDef.type = BodyDef.BodyType.StaticBody;



        bodyDef.position.set(x , y);

        polygonShape = new PolygonShape();
        polygonShape.setAsBox(width, height);


        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0.5f;

    }

    public RigidBody2D(float x, float y, float radius, int staticBody){
        seperate = true;
        bodyDef = new BodyDef();

        if (staticBody == 0){
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }

        if (staticBody == 1)
            bodyDef.type = BodyDef.BodyType.StaticBody;



        bodyDef.position.set(x , y);

        shape = new CircleShape();
        shape.setRadius(radius);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0.5f;


    }

    public void addToWorld(World world){
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        if (shape != null)
        shape.dispose();

        if (polygonShape != null)
            polygonShape.dispose();
    }

    public void setActive(boolean active){
        body.setActive(active);
    }

    public Body getBody(){
        return body;
    }
}

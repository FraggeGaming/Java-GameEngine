package EntityEngine.Systems;

import EntityEngine.Components.CollisionComponent;
import EntityEngine.Components.TransformComponent;
import EntityEngine.Components.VelocityComponent;
import EntityEngine.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class MovementSystem extends System {

    TransformComponent t;
    CollisionComponent c;
    VelocityComponent v;
    public MovementSystem(){

    }

    @Override
    public void update(float dt){
        float x = 0, y = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            //change vertical direction
            x -= 1;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            //change vertical direction
            x += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            //change vertical direction
            y += 1;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            //change vertical direction
            y -= 1;
        }

        if (x != 0 || y != 0){
            engine.getCamera().translate(x * 300 * dt , y * 300 * dt);

            addVelocity(engine.getEntity("Player"), x*300*dt, y*300*dt);


        }


        engine.getCamera().update();
    }

    public void addVelocity(Entity e, float x, float y){

        t = (TransformComponent) e.getComponent( TransformComponent.class);
        c = (CollisionComponent) e.getComponent(CollisionComponent.class);
        v = (VelocityComponent) e.getComponent(VelocityComponent.class);


        engine.getSpatialHashGrid().removeEntity(e);

        v.setVelocity(x, y, 0);
        t.addVelocity(v);
        c.followTransform(t);

        engine.getSpatialHashGrid().addEntity(e);
    }
}

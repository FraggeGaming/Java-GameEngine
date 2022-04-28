package TestFiles.scripts;

import EntityEngine.Components.CollisionComponent;
import EntityEngine.Components.TransformComponent;
import EntityEngine.Components.VelocityComponent;
import EntityEngine.Entity;
import EntityEngine.Systems.ComponentManagerSystem;
import EntityEngine.Systems.NetworkManager;
import EntityEngine.Systems.PhysicsSystem;
import EntityEngine.Systems.System;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class MovementSystem extends System {

    TransformComponent t;
    CollisionComponent c;
    VelocityComponent v;
    NetworkManager network;
    boolean fetchNetwork = false;

    PhysicsSystem physicsSystem;
    boolean hasPhysicsSystem = false;
    public MovementSystem(){

    }

    @Override
    public void update(float dt){

        if (!fetchNetwork){
            network = (NetworkManager) engine.getSystem(NetworkManager.class);
            if (network != null)
                fetchNetwork = true;
        }

        if (network == null || !network.isOpen){
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
                addVelocity(engine.getEntity(engine.user), x*300*dt, y*300*dt);
            }


            engine.getCamera().update();
        }

    }

    private void fetchPhysicsSystem(){
        if (!hasPhysicsSystem){
            physicsSystem = (PhysicsSystem) engine.getSystem(PhysicsSystem.class);
            if (physicsSystem != null)
                hasPhysicsSystem = true;
        }

    }

    public void addVelocity(Entity e, float x, float y){

        if (e == null)
            return;
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

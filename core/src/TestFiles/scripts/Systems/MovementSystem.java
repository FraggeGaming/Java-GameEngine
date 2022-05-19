package TestFiles.scripts.Systems;

import EntityEngine.Components.*;
import EntityEngine.Entity;
import EntityEngine.Systems.NetworkManager;
import EntityEngine.Systems.System;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class MovementSystem extends System {

    TransformComponent t;
    CollisionComponent c;
    VelocityComponent v;
    RigidBody2D rigidBody2D;
    NetworkManager network;
    boolean fetchNetwork = false;

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

            addVelocity(engine.getEntity(engine.user), x*150 * dt, y*150 * dt);
            if (x != 0 || y != 0){

                AnimationComponent a = (AnimationComponent) engine.getEntity(engine.user).getComponent(AnimationComponent.class);
                if (!a.isAlive()){
                    a.setAlive(true);
                }
            }

            else{
                AnimationComponent a = (AnimationComponent) engine.getEntity(engine.user).getComponent(AnimationComponent.class);
                if (a.isAlive()){
                    a.resetAnimation();
                    a.setAlive(false);

                    TextureComponent t = (TextureComponent) engine.getEntity(engine.user).getComponent(TextureComponent.class);

                    t.setTexture(a.getCurrentFrame());


                }
            }


            engine.getCamera().update();
        }

    }


    public void addVelocity(Entity e, float x, float y){

        if (e == null)
            return;
        t = (TransformComponent) e.getComponent( TransformComponent.class);
        c = (CollisionComponent) e.getComponent(CollisionComponent.class);
        v = (VelocityComponent) e.getComponent(VelocityComponent.class);
        rigidBody2D = (RigidBody2D) e.getComponent(RigidBody2D.class);

        engine.getSpatialHashGrid().removeEntity(e);


        rigidBody2D.getBody().setLinearVelocity(x*100,y*100);
        engine.getCamera().position.set(rigidBody2D.getBody().getPosition().x, rigidBody2D.getBody().getPosition().y, 0);


        t.setPosition(rigidBody2D.getBody().getPosition().x - 16, rigidBody2D.getBody().getPosition().y - 16 );
        c.followTransform(t);

        engine.getSpatialHashGrid().addEntity(e);
    }
}

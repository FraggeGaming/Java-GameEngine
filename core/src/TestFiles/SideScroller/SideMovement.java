package TestFiles.SideScroller;

import EntityEngine.Components.*;
import EntityEngine.Entity;
import EntityEngine.Systems.NetworkManager;
import EntityEngine.Systems.System;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class SideMovement extends System{

    TransformComponent t;
    CollisionComponent c;
    VelocityComponent v;
    RigidBody2D rigidBody2D;
    NetworkManager network;


    @Override
    public void onCreate() {
        network = (NetworkManager) engine.getSystem(NetworkManager.class);
    }

    @Override
    public void update(float dt){

        if (network != null && network.isOpen)
            return;


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
            addVelocity(engine.getEntity("Player"), x*80 * dt, y*80 * dt);
            AnimationComponent a = (AnimationComponent) engine.getEntity("Player").getComponent(AnimationComponent.class);
            if (!a.isAlive()){
                a.setAlive(true);
            }
        }

        else{
            AnimationComponent a = (AnimationComponent) engine.getEntity("Player").getComponent(AnimationComponent.class);
            if (a.isAlive()){
                a.resetAnimation();
                a.setAlive(false);

                TextureComponent t = (TextureComponent) engine.getEntity("Player").getComponent(TextureComponent.class);

                t.setTexture(a.getCurrentFrame());


            }
        }

        syncWithPhysics(engine.getEntity("Player"));


        engine.getCamera().update();


    }


    public void addVelocity(Entity e, float x, float y){

        if (e == null)
            return;
        t = (TransformComponent) e.getComponent( TransformComponent.class);
        c = (CollisionComponent) e.getComponent(CollisionComponent.class);
        rigidBody2D = (RigidBody2D) e.getComponent(RigidBody2D.class);



        rigidBody2D.getBody().setLinearVelocity(x*100,y*100);





        //rigidBody2D.getBody().applyLinearImpulse(new Vector2(x*20, y*20), rigidBody2D.getBody().getWorldCenter(), true);

    }

    public void syncWithPhysics(Entity entity){
        if (entity == null)
            return;
        t = (TransformComponent) entity.getComponent( TransformComponent.class);
        c = (CollisionComponent) entity.getComponent(CollisionComponent.class);
        rigidBody2D = (RigidBody2D) entity.getComponent(RigidBody2D.class);

        engine.getSpatialHashGrid().removeEntity(entity);

        engine.getCamera().position.set(rigidBody2D.getBody().getPosition().x, rigidBody2D.getBody().getPosition().y, 0);


        t.setPosition(rigidBody2D.getBody().getPosition().x - 16, rigidBody2D.getBody().getPosition().y - 16 );
        c.followTransform(t);

        engine.getSpatialHashGrid().addEntity(entity);

    }
}



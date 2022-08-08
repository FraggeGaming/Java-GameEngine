package TestFiles.scripts.Systems;

import EntityEngine.Components.*;
import EntityEngine.Entity;
import EntityEngine.Systems.NetworkManager;
import EntityEngine.Systems.System;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class MovementSystem extends System {

    TransformComponent t;
    CollisionComponent c;
    VelocityComponent v;
    RigidBody2D rigidBody2D;
    NetworkManager network;



    public MovementSystem(){

    }

    @Override
    public void onCreate() {
        network = (NetworkManager) engine.getSystem(NetworkManager.class);
    }

    public void movePlayer(float x, float y){
        if (engine.getEntity("Player") == null)
            return;

        if (network != null && network.isOpen)
            return;


        addVelocity(engine.getEntity("Player"), x, y);
        if (x != 0 || y != 0){

            AnimationComponent a = (AnimationComponent) engine.getEntity("Player").getComponent(AnimationComponent.class);
            if (a != null && !a.isAlive()){
                a.setAlive(true);
            }
        }

        else{
            AnimationComponent a = (AnimationComponent) engine.getEntity("Player").getComponent(AnimationComponent.class);
            if (a != null && a.isAlive()){
                a.resetAnimation();
                a.setAlive(false);

                TextureComponent t = (TextureComponent) engine.getEntity("Player").getComponent(TextureComponent.class);

                t.setTexture(a.getCurrentFrame());


            }
        }


        engine.getCamera().update();
    }

    @Override
    public void update(float dt){







    }


    public void addVelocity(Entity e, float x, float y){

        if (e == null)
            return;
        t = (TransformComponent) e.getComponent( TransformComponent.class);
        c = (CollisionComponent) e.getComponent(CollisionComponent.class);
        rigidBody2D = (RigidBody2D) e.getComponent(RigidBody2D.class);
        if (x > 0 && t.getScaleX() > 0)
            t.mirror(true, false);

        else if (x < 0 && t.getScaleX() < 0)
            t.mirror(true, false);


        rigidBody2D.getBody().setLinearVelocity(x,y);


        syncWithPhysics(engine.getEntity("Player"));

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

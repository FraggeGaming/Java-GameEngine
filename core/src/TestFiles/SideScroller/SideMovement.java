package TestFiles.SideScroller;

import EntityEngine.Components.*;
import EntityEngine.Utils.Entity;
import EntityEngine.Systems.NetworkManager;
import EntityEngine.Systems.System;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class SideMovement extends System{

    TransformComponent t;
    CollisionComponent c;
    RigidBody2D rigidBody2D;
    NetworkManager network;

    float maxSpeed = 5;
    float speed = 5;
    float jumpHight = 2000;


    @Override
    public void onCreate() {
    }

    @Override
    public void update(float dt){

        if (network != null && network.isOpen)
            return;


        float x = 0, y = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            //change vertical direction
            x -= speed;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            //change vertical direction
            x += speed;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            y = jumpHight;
        }



        if (x != 0 || y != 0){
            if (Math.abs(x) <= maxSpeed){
                addVelocity(engine.getEntity("Player"), x , y );
                AnimationComponent a = (AnimationComponent) engine.getEntity("Player").getComponent(AnimationComponent.class);
                if (!a.isAlive()){
                    a.setAlive(true);
                }
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
        if (x > 0 && t.getScaleX() > 0)
            t.mirror(true, false);

        else if (x < 0 && t.getScaleX() < 0)
            t.mirror(true, false);

        rigidBody2D.getBody().applyLinearImpulse(new Vector2(x, y), rigidBody2D.getBody().getWorldCenter(), true);

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
        //c.followTransform(t);

        engine.getSpatialHashGrid().addEntity(entity);

    }
}



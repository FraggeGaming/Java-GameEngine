package TestFiles.scripts;

import EntityEngine.Components.*;
import EntityEngine.Entity;
import EntityEngine.Systems.System;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class BulletSystem extends System {
    Array<Component> bullets;
    TransformComponent t;
    CollisionComponent c;
    VelocityComponent v;
    TextureAtlas atlas;
    Entity e;
    public BulletSystem(TextureAtlas atlas){
        this.atlas = atlas;


    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            Entity p = engine.getEntity("Player");
            TransformComponent t = (TransformComponent) p.getComponent(TransformComponent.class);
            Entity e = new Entity();
            e.addComponents(new TextureComponent(new TextureRegion(atlas.findRegion("Spore"))));
            e.addComponents(new TransformComponent(t.getOriginX(), t.getOriginY(), 5, 20, 20));
            e.addComponents(new CollisionComponent(t.getOriginX(), t.getOriginY(),20, 20));
            e.addComponents(new VelocityComponent(90, 0, 1));
            e.addComponents(new LifeCount(3));
            e.addComponents(new BulletComponent());

            engine.addEntity(e);

        }
        bulletLogic(dt);

    }

    private void bulletLogic(float dt){
        bullets = engine.getloadedComponents(BulletComponent.class);
        if (bullets == null)
            return;

        for (int i = 0; i < bullets.size; i++){
            e = engine.getEntity(bullets.get(i).getId());
            if (e != null && !e.flagForDelete){

                addVelocity(e, dt);
                LifeCount l = (LifeCount) e.getComponent(LifeCount.class);
                l.live(dt);
                if (l.isDead()){
                    engine.removeEntity(e);
                }
            }
        }
    }

    public void addVelocity(Entity e, float dt){

        if (e == null)
            return;
        t = (TransformComponent) e.getComponent( TransformComponent.class);
        c = (CollisionComponent) e.getComponent(CollisionComponent.class);
        v = (VelocityComponent) e.getComponent(VelocityComponent.class);


        engine.getSpatialHashGrid().removeEntity(e);

        t.addVelocity(new Vector3(v.vel.x * dt, v.vel.y * dt, 0));
        c.followTransform(t);

        engine.getSpatialHashGrid().addEntity(e);
    }


}

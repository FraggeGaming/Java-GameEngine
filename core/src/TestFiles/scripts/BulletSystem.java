package TestFiles.scripts;

import EntityEngine.Components.*;
import EntityEngine.Entity;
import EntityEngine.Systems.CollisionDetectionSystem;
import EntityEngine.Systems.System;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class BulletSystem extends System {
    Array<Component> bullets;
    TransformComponent t;
    CollisionComponent c;
    VelocityComponent v;
    TextureAtlas atlas;
    Entity e;

    float fireTimer = 0;
    float fireRate = 50;

    Vector3 mouseVector = new Vector3();
    Vector2 bullet = new Vector2();

    Entity player;
    TransformComponent playerTransform;
    CollisionDetectionSystem col;
    LifeCount l;


    public BulletSystem(TextureAtlas atlas){
        this.atlas = atlas;


    }

    @Override
    public void update(float dt) {

        fireBullet(dt);
        bulletLogic(dt);

    }

    private void fireBullet(float dt){
        if (player == null){
            player = engine.getEntity("Player");
            playerTransform = (TransformComponent) player.getComponent(TransformComponent.class);
        }


        fireTimer += dt;

        if (fireTimer >= 1/fireRate){

            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){

                Projectile proj = new Projectile(2, 200,playerTransform, 20, 20, new TextureRegion(atlas.findRegion("Spore")), getbulletVector());
                engine.addEntity(proj.getProjectile());

                fireTimer = 0;
            }

            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){

                Projectile proj = new Projectile(0.5f, 700,playerTransform, 15, 15, new TextureRegion(atlas.findRegion("SpiderEgg")), getbulletVector());
                engine.addEntity(proj.getProjectile());

                fireTimer = 0;
            }
        }
    }

    private Vector2 getbulletVector(){
        mouseVector.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        mouseVector = engine.getCamera().unproject(mouseVector);

        bullet.set(playerTransform.getOriginX() - mouseVector.x, playerTransform.getOriginY() - mouseVector.y);
        bullet.nor();
        bullet.scl(-1);

        return bullet;
    }

    private void bulletLogic(float dt){
        bullets = engine.getloadedComponents(BulletComponent.class);
        if (bullets == null)
            return;

        for (int i = 0; i < bullets.size; i++){
            e = engine.getEntity(bullets.get(i).getId());
            if (e != null && !e.flagForDelete){

                addVelocity(e, dt);
                l = (LifeCount) e.getComponent(LifeCount.class);
                l.live(dt);
                if (l.isDead()){
                    engine.removeEntity(e);
                }

                else {

                    if (col == null)
                        col = (CollisionDetectionSystem) engine.getSystem(CollisionDetectionSystem.class);

                    c = (CollisionComponent) e.getComponent(CollisionComponent.class);
                    if (col.CollisionWithID(c, "Sand")){
                        engine.removeEntity(e);
                    }


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

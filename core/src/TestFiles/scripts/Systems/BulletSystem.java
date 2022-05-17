package TestFiles.scripts.Systems;

import EntityEngine.Components.*;
import EntityEngine.Entity;
import EntityEngine.GameClasses.Animation;
import EntityEngine.Renderer.Cell;
import EntityEngine.Systems.CollisionDetectionSystem;
import EntityEngine.Systems.System;
import TestFiles.scripts.Components.BulletComponent;
import TestFiles.scripts.Components.LifeCount;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class BulletSystem extends System {
    TransformComponent t;
    CollisionComponent c;
    VelocityComponent v;
    TextureAtlas atlas;
    Entity e;

    float fireTimer = 0;
    float fireRate = 100;

    Vector3 mouseVector = new Vector3();
    Vector2 bullet = new Vector2();

    Entity player;
    TransformComponent playerTransform;
    CollisionDetectionSystem col;
    LifeCount l;
    TextureAtlas fireAtlas;

    Array<Entity> firedbullets = new Array<>();
    Array<Entity> firedbulletsToRemove = new Array<>();

    public BulletSystem(){


    }

    @Override
    public void onCreate() {
        atlas = engine.assetManager.get("atlas/TP.atlas");
        fireAtlas = engine.assetManager.get("atlas/Fire.atlas");
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

                e = createProjectile(1, 200,playerTransform, 20, 20, new TextureRegion(atlas.findRegion("Spore")), getbulletVector());
                engine.addEntity(e);
                firedbullets.add(e);
                fireTimer = 0;
            }

            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){

                e = createProjectile(0.5f, 300,playerTransform, 15, 15, new TextureRegion(atlas.findRegion("SpiderEgg")), getbulletVector());
                engine.addEntity(e);

                firedbullets.add(e);

                fireTimer = 0;
            }
        }
    }

    private Entity createProjectile(float life, float speed, TransformComponent position, float width, float height, TextureRegion textureRegion, Vector2 bulletVector){
        e = new Entity();

        e.addComponents(new TextureComponent(textureRegion));
        e.addComponents(new TransformComponent(position.getX() , position.getY()+ 10, 5, width, height));
        e.addComponents(new CollisionComponent(position.getX(), position.getY()+ 10,width, height));
        e.addComponents(new VelocityComponent(bulletVector.x * speed, bulletVector.y * speed, 1));
        e.addComponents(new LifeCount(life));
        e.addComponents(new BulletComponent());

        return e;
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


        firedbullets.removeAll(firedbulletsToRemove, true);
        firedbulletsToRemove.clear();



        for (int i = 0; i < firedbullets.size; i++){
            e = firedbullets.get(i);
            if (e != null && !e.flagForDelete){

                addVelocity(e, dt);
                l = (LifeCount) e.getComponent(LifeCount.class);
                l.live(dt);
                if (l.isDead()){
                    firedbulletsToRemove.add(e);
                    engine.removeEntity(e);
                }

                else {

                    if (col == null)
                        col = (CollisionDetectionSystem) engine.getSystem(CollisionDetectionSystem.class);

                    c = (CollisionComponent) e.getComponent(CollisionComponent.class);
                    if (col.CollisionWithID(c, "Water") || col.CollisionWithID(c, "Sand")){

                        engine.removeEntity(e);
                    }

                    if (col.CollisionWithID(c, "Wall")){
                        t = (TransformComponent) e.getComponent(TransformComponent.class);

                        exploadCell();
                        firedbulletsToRemove.add(e);
                        engine.removeEntity(e);

                    }
                }
            }

        }




    }

    private void exploadCell(){
        Array<CollisionComponent> toExpload = col.getCollisision(c, "Wall");
        for (int k = 0; k < toExpload.size; k++){

            Entity temp = engine.getEntity(toExpload.get(k).getId());
            TextureComponent newTex = (TextureComponent) temp.getComponent(TextureComponent.class);
            t = (TransformComponent) temp.getComponent(TransformComponent.class);

            newTex.setTexture(new TextureRegion(atlas.findRegion("ForrestTile1")));

            engine.getSpatialHashGrid().removeEntity(temp);
            temp.removeComponent(CollisionComponent.class);
            engine.getSpatialHashGrid().addEntity(temp);


            createFireAnimationTest(t.getOriginX(), t.getOriginY(), fireAtlas);
        }
    }

    public void createFireAnimationTest(float x, float y, TextureAtlas fireAtlas){
        Entity i = new Entity();
        i.addComponents(new TextureComponent(new TextureRegion(fireAtlas.findRegion("fire", 0))));
        i.addComponents(new TransformComponent(x -50, y -25, 2, 100, 100));

        Animation animation = new Animation(fireAtlas, "fire", 109);
        AnimationComponent a = new AnimationComponent(0.02f, false, animation.getFrames(), true);
        i.addComponents(a);

        engine.addEntity(i);


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

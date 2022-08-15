package TestFiles.scripts.Systems;

import EntityEngine.Components.*;
import EntityEngine.Utils.Entity;
import EntityEngine.Utils.Animation;
import EntityEngine.Systems.CollisionDetectionSystem;
import EntityEngine.Systems.NavMesh;
import EntityEngine.Systems.System;
import TestFiles.scripts.Components.BulletComponent;
import TestFiles.scripts.Components.TimerComponent;
import TestFiles.scripts.sim.TileSimManager;
import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
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
    float fireRate = 5;

    Vector3 mouseVector = new Vector3();
    Vector2 bullet = new Vector2();

    Entity player;
    TransformComponent playerTransform;
    CollisionDetectionSystem col;
    TextureAtlas fireAtlas;

    TimerSystem timerSystem;
    NavMesh navMesh;
    TileSimManager tileSimManager;

    @Override
    public void onCreate() {
        atlas = engine.assetManager.get("atlas/TP.atlas");

        fireAtlas = engine.assetManager.get("atlas/Fire.atlas");
        timerSystem = (TimerSystem) engine.getSystem(TimerSystem.class);
        navMesh = (NavMesh) engine.getSystem(NavMesh.class);
        tileSimManager = (TileSimManager) engine.getSystem(TileSimManager.class);
    }

    @Override
    public void update(float dt) {

        fireBullet(dt);
        bulletLogic(dt);

    }

    private void fireBullet(float dt){



        fireTimer += dt;

        if (fireTimer >= 1/fireRate){
            player = engine.getEntity("Player");
            if (player == null)
                return;

            playerTransform = (TransformComponent) player.getComponent(TransformComponent.class);
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){

                e = createProjectile(1, 200,playerTransform, 20, 20, new TextureRegion(atlas.findRegion("Spore")), getbulletVector());
                engine.addEntity(e);
                fireTimer = 0;
            }

            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){

                e = createProjectile(0.5f, 300,playerTransform, 15, 15, new TextureRegion(atlas.findRegion("SpiderEgg")), getbulletVector());
                engine.addEntity(e);
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
        e.addComponents(new TimerComponent(life));
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

        for (int i = 0; i < timerSystem.entities().size; i++){
            e = timerSystem.entities().get(i);
            if (e.getComponent(BulletComponent.class) != null) {
                addVelocity(e, dt);

                if (col == null) {
                    col = (CollisionDetectionSystem) engine.getSystem(CollisionDetectionSystem.class);
                }

                c = (CollisionComponent) e.getComponent(CollisionComponent.class);
                if (col.CollisionWithID(c, "Water") || col.CollisionWithID(c, "Sand")){

                    engine.removeEntity(e);
                }

                if (col.CollisionWithID(c, "Wall")){

                    exploadCell();

                    engine.removeEntity(e);

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
            navMesh.freeNode(t.getX(),t.getY());
            tileSimManager.freeTile(t.getX(),t.getY());
            newTex.setTexture(new TextureRegion(atlas.findRegion("ForrestTile1")));

            engine.getSpatialHashGrid().removeEntity(temp);
            temp.removeComponent(CollisionComponent.class);
            engine.getSpatialHashGrid().addEntity(temp);

            engine.deleteRigidBody(temp);


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

        i.addComponents(new TimerComponent(4));
        Light light = new Light(new PointLight(engine.lightning, 20, null, 40, x,y));
        light.colorAndSoftnessLength(new Color(0.7f,0.4f,0.4f,0.8f), 2f);
        light.setStatic(true);
        light.setXray(true);
        i.addComponents(light);

        engine.addEntity(i);
    }

    public void addVelocity(Entity e, float dt){

        if (e == null || e.flagForDelete)
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

package TestFiles.SideScroller;

import EntityEngine.Components.*;
import EntityEngine.Entity;
import EntityEngine.GameClasses.Animation;
import EntityEngine.GameClasses.TDCamera;
import EntityEngine.Noise.OpenSimplexNoise;
import EntityEngine.Systems.CollisionDetectionSystem;
import EntityEngine.Systems.System;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class WorldGen extends System {
    OpenSimplexNoise noise;
    TextureAtlas atlas;
    TextureAtlas fireAtlas;
    TextureAtlas sC;
    TextureAtlas larvMovement;
    CollisionDetectionSystem col;

    @Override
    public void onCreate() {
        engine.lightning.setAmbientLight(0.2f);

        col = (CollisionDetectionSystem) engine.getSystem(CollisionDetectionSystem.class);
        sC = engine.assetManager.get("atlas/StoneCrab.atlas");
        atlas = engine.assetManager.get("atlas/TP.atlas");
        fireAtlas = engine.assetManager.get("atlas/Fire.atlas");
        larvMovement = engine.assetManager.get("atlas/LarvMovement.atlas");

        engine.world.setGravity(new Vector2(0, -180f));

        createPlayers(engine.getCamera());
        int x = 10;
        int y = 90;
        for (int i = 1; i < 100; i++){
            float xCord = x + 16*i;
            float yCord = y;
            createTile(xCord, yCord, "StoneWallTop", "Wall");
        }

    }

    public void createTile(float x, float y, String name, String id){
        Entity e = new Entity();
        e.addComponents(new TextureComponent(new TextureRegion(atlas.findRegion(name))));
        e.addComponents(new TransformComponent(x, y, 1, 16, 16));

        if (id.equals("Wall")){
            CollisionComponent c = new CollisionComponent(x, y, 16, 16);
            c.id = id;
            c.isStatic = true;
            e.addComponents(c);

            RigidBody2D box2d = new RigidBody2D(x + 8, y + 8, 8, 8, 1);
            box2d.addToWorld(engine.world);
            e.addComponents(box2d);

        }

        else if (id.equals("Light")){
            Light light = new Light(new PointLight(engine.lightning, 15, null, 40, x + 8, y + 8));
            light.colorAndSoftnessLength(new Color(0.1f,0.3f,0.4f,0.8f), 2f);
            light.setStatic(true);
            e.addComponents(light);
        }




        engine.addEntity(e);
    }

    public void createPlayers(TDCamera camera){
        engine.user = "Player"; //TODO change this later to less shit way

        Entity player = new Entity();
        player.addComponents(new TextureComponent(new TextureRegion(larvMovement.findRegion("CaterpillarGun"))));
        player.addComponents(new TransformComponent(camera.viewportWidth / 2, camera.viewportHeight / 2, 5, 32, 32));

        player.addComponents(new VelocityComponent());
        player.name = "Player";

        Animation animation = new Animation(larvMovement, "CaterpillarGun", 6);
        AnimationComponent a = new AnimationComponent(0.07f, false, animation.getFrames(), false);
        a.setAlive(false);
        player.addComponents(a);

        RigidBody2D box2d = new RigidBody2D(camera.viewportWidth / 2, camera.viewportHeight / 2, 10, 0);
        box2d.addToWorld(engine.world);
        box2d.getBody().setFixedRotation(true);
        box2d.getBody().setLinearDamping(1f);


        player.addComponents(box2d);



        Light light = new Light(new PointLight(engine.lightning, 20, Color.WHITE, 80, 300, 300));
        light.attachToBody(box2d.getBody(), true);
        light.colorAndSoftnessLength(new Color(0.8f,0.4f,0.7f,0.8f), 10f);
        light.activate(true);
        player.addComponents(light);

        engine.addEntity(player);
    }
}

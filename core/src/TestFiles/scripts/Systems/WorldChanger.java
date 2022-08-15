package TestFiles.scripts.Systems;

import EntityEngine.Components.*;
import EntityEngine.Utils.Entity;
import EntityEngine.Utils.Animation;
import EntityEngine.Utils.TDCamera;
import EntityEngine.Systems.System;
import EntityEngine.Systems.TileMapRenderer;
import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WorldChanger extends System {

    boolean changeWorld = false;

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Y)){

            engine.resetECS();
            //fix tilemap reset
            engine.getSystem(TileMapRenderer.class).reset();

            changeWorld = true;
        }


    }

    @Override
    public void lastUpdate(float dt) {
        if (changeWorld){
            engine.addEntity(player(engine.camera, (TextureAtlas) engine.assetManager.get("atlas/LarvMovement.atlas")));
            changeWorld = false;
        }
    }

    public Entity player(TDCamera camera, TextureAtlas larvMovement){
        engine.user = "Player"; //TODO change this later to less shit way

        Entity player = new Entity();
        player.addComponents(new TextureComponent(new TextureRegion(larvMovement.findRegion("CaterpillarGun"))));
        player.addComponents(new TransformComponent(camera.viewportWidth / 2, camera.viewportHeight / 2, 5, 32, 32));
        CollisionComponent c = new CollisionComponent(camera.viewportWidth / 2, camera.viewportHeight / 2, 32, 32);
        c.id = "Player";
        player.addComponents(c);
        player.addComponents(new VelocityComponent());
        player.name = "Player";

        Animation animation = new Animation(larvMovement, "CaterpillarGun", 6);
        AnimationComponent a = new AnimationComponent(0.07f, false, animation.getFrames(), false);
        a.setAlive(false);
        player.addComponents(a);

        RigidBody2D box2d = new RigidBody2D(camera.viewportWidth / 2, camera.viewportHeight / 2, 14, 10, 0);
        box2d.addToWorld(engine.world);
        box2d.getBody().setFixedRotation(true);
        player.addComponents(box2d);

        Light light = new Light(new PointLight(engine.lightning, 20, Color.WHITE, 80, 300, 300));
        light.attachToBody(box2d.getBody(), true);
        light.colorAndSoftnessLength(new Color(0.8f,0.4f,0.7f,0.8f), 10f);
        light.activate(true);
        player.addComponents(light);

        return player;
    }
}

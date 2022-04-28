package TestFiles.scripts;

import EntityEngine.Components.*;
import EntityEngine.Entity;
import EntityEngine.GameClasses.Animation;
import EntityEngine.GameClasses.TDCamera;
import EntityEngine.Noise.OpenSimplexNoise;
import EntityEngine.Systems.System;
import EntityEngine.Tile;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class WorldSystem extends System {
    int tileMapRenderIndexX = 0;
    int tileMapRenderIndexY = 0;
    double scale = 0.04f;
    float z = 1f;
    int mapSize = 1000;
    int mapSizeIndex = 0;
    OpenSimplexNoise noise;
    TextureAtlas atlas;
    Entity player;
    public WorldSystem(TextureAtlas atlas){
        noise = new OpenSimplexNoise(); //for tilemap generation
        this.atlas = atlas;
    }

    @Override
    public void onCreate() {
        TextureAtlas fireAtlas = new TextureAtlas("atlas/Fire.atlas");
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++)
                createFireAnimationTest(30 * i, 30*j, fireAtlas );
        }

        createPlayers(engine.camera);
    }

    public void createPlayers(TDCamera camera){
        engine.user = "Player"; //TODO change this later to less shit way

        player = new Entity();
        player.addComponents(new TextureComponent(new TextureRegion(atlas.findRegion("RedAnt"))));
        player.addComponents(new TransformComponent(camera.viewportWidth / 2, camera.viewportHeight / 2, 5, 30, 30));
        CollisionComponent c = new CollisionComponent(camera.viewportWidth / 2, camera.viewportHeight / 2, 30, 30);
        c.id = "Player2";
        player.addComponents(c);
        player.addComponents(new VelocityComponent());
        player.tag = "Player2";
        engine.addEntity(player);

        player = new Entity();
        player.addComponents(new TextureComponent(new TextureRegion(atlas.findRegion("BlueAnt"))));
        player.addComponents(new TransformComponent(camera.viewportWidth / 2, camera.viewportHeight / 2, 5, 30, 30));
        c = new CollisionComponent(camera.viewportWidth / 2, camera.viewportHeight / 2, 30, 30);
        c.id = "Player";
        player.addComponents(c);
        player.addComponents(new VelocityComponent());
        player.tag = "Player";
        engine.addEntity(player);
    }

    public void createFireAnimationTest(int x, int y, TextureAtlas fireAtlas){
        Entity e = new Entity();
        e.addComponents(new TextureComponent(new TextureRegion(fireAtlas.findRegion("fire", 0))));
        e.addComponents(new TransformComponent(x, y, 1, 120, 120));
        CollisionComponent c = new CollisionComponent(x + 20, y + 20, 40, 40);
        c.id = "Fire";
        c.isStatic = true;
        e.addComponents(c);
        Animation animation = new Animation(fireAtlas, "fire", 109);
        AnimationComponent a = new AnimationComponent(0.02f, true, animation.getFrames());
        a.addAnimation(0.3f, true, animation.getFrames());
        e.addComponents(a);

        engine.addEntity(e);

    }

    @Override
    public void update(float dt) {
        createTileMap();
    }

    public void createTileMap(){
        Tile t;

        if (mapSizeIndex > mapSize*mapSize)
            return;

        for (int i = 0; i < 100; i++){
            if (tileMapRenderIndexX < mapSize){
                t = new Tile(atlas, noise.eval(tileMapRenderIndexX*scale, tileMapRenderIndexY*scale, z), 15*tileMapRenderIndexX, 15*tileMapRenderIndexY, 0, 15, 15);
                engine.addEntity(t.getEntity());
                tileMapRenderIndexX++;
            }

            else {
                tileMapRenderIndexY++;
                tileMapRenderIndexX = 0;
            }

            mapSizeIndex++;
        }
    }
}

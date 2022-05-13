package TestFiles.scripts.Systems;

import EntityEngine.Components.*;
import EntityEngine.Entity;
import EntityEngine.GameClasses.Animation;
import EntityEngine.GameClasses.TDCamera;
import EntityEngine.Noise.OpenSimplexNoise;
import EntityEngine.Systems.System;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import imgui.ImGui;

import java.util.HashMap;


public class WorldSystem extends System {
    int tileMapRenderIndexX = 0;
    int tileMapRenderIndexY = 0;
    double scale = 0.04f;
    float z = 1f;
    int mapSize = 1000;
    int mapSizeIndex = 0;
    OpenSimplexNoise noise;
    TextureAtlas atlas;
    TextureAtlas fireAtlas;
    Entity player;
    Entity e;

    public WorldSystem(){
        noise = new OpenSimplexNoise(); //for tilemap generation


        Pixmap cursorPm = new Pixmap(Gdx.files.internal("tempcursor.png"));
        int xHotSpot = cursorPm.getWidth() ;
        int yHotSpot = cursorPm.getHeight();
        Cursor cursor = Gdx.graphics.newCursor(cursorPm, xHotSpot /2, yHotSpot /2);
        Gdx.graphics.setCursor(cursor);
        cursorPm.dispose();
    }

    @Override
    public void onCreate() {

        atlas = engine.assetManager.get("atlas/TexturePack.atlas");
        fireAtlas = engine.assetManager.get("atlas/Fire.atlas");

        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++)
                createFireAnimationTest(30 * i, 30*j, fireAtlas );
        }

        createPlayers(engine.camera);

        createHouse(300, 200);

        createHouse(300+15*14, 300);

        createHouse(300+15*7, 500);

        createTile(300 , 400, "MushroomBig", 60, 140, 10);

        createTile(530 , 200, "Branch", 140, 20, 1);
    }

    private void createTileWithNoise(float x, float y, double noise){
        e = new Entity();
        e.addComponents(new TextureComponent(generateWithNoise(noise)));
        e.addComponents(new TransformComponent(x, y, 0, 15, 15));

        engine.addEntity(e);

    }

    private TextureRegion generateWithNoise(double value){
        //System.out.println(value);
        if (value < -0.3f){
            return new TextureRegion(atlas.findRegion("DeepWater"));
        }

        if (value < -0.2f){
            return new TextureRegion(atlas.findRegion("TransitionWater"));
        }

        if (value < 0f){

            return new TextureRegion(atlas.findRegion("Water"));
        }

        else if (value < 0.1f){

            return new TextureRegion(atlas.findRegion("Sand"));
        }

        else if (value < 0.2f){
            //components.add(c);
            //c.id = "Podsol";
            return new TextureRegion(atlas.findRegion("Podsol"));
        }

        else if (value < 0.4f){
            //components.add(c);
            //c.id = "DirtTile";
            return new TextureRegion(atlas.findRegion("DirtTile"));
        }

        else if (value < 0.70f){

            return new TextureRegion(atlas.findRegion("RockyTile"));
        }
        else
            return new TextureRegion(atlas.findRegion("RockyTile"));

    }

    private void createHouse(float x, float y){

        //floor
        for (int i = 0; i < 11; i++){
            float xCord = x+15*i;
            float yCord = y;
            createTile(xCord, yCord, "StoneWallBottom1", "Wall");

        }

        //right wall
        for (int i = 1; i < 10; i++){
            float xCord = x;
            float yCord = y +15*i;
            createTile(xCord, yCord, "StoneWallSideLeft", "Wall");
        }

        //Left wall
        for (int i = 1; i < 10; i++){
            float xCord = x + 15*10;
            float yCord = y +15*i;
            createTile(xCord, yCord, "StoneWallSideRight", "Wall");
        }



        //Roof
        for (int i = 1; i < 10; i++){
            float xCord = x + 15*i;
            float yCord = y + 15*10;
            createTile(xCord, yCord, "StoneWallTop", "Wall");
        }

        for (int i = 1; i < 10; i++){
            float xCord = x + 15*i;

            for (int j = 1; j < 10; j++){
                float yCord = y + 15*j;
                createTile(xCord, yCord, "StoneTile", "Floor");

            }
        }

        //corners
        createTile(x, y + 15*10, "StoneCornerUpperLeft", "Wall");
        createTile(x + 15*10, y + 15*10, "StoneCornerUpperRight", "Wall");


        //Random stuff
        createTile(x + 5*15, y+15, "CrystalTile", "Crystal");

        createTile(x + 2*15, y+7*15, "CrystalTile", "Crystal");

        createTile(x + 6*15, y+3*15, "BlueTorch", "Torch");

        createTile(x + 9*15, y+8*15, "BlueTorch", "Torch");

        createTile(x + 6*15, y+7*15, "Quartz", "Quartz");

    }

    public void createTile(float x, float y, String name, String id){
        e = new Entity();
        e.addComponents(new TextureComponent(new TextureRegion(atlas.findRegion(name))));
        e.addComponents(new TransformComponent(x, y, 1, 15, 15));

        if (id.equals("Wall")){
            CollisionComponent c = new CollisionComponent(x, y, 15, 15);
            c.id = id;
            c.isStatic = true;
            e.addComponents(c);
        }


        engine.addEntity(e);
    }

    public void createTile(float x, float y, String name, float width, float height, float z){
        e = new Entity();
        e.addComponents(new TextureComponent(new TextureRegion(atlas.findRegion(name))));
        e.addComponents(new TransformComponent(x, y, z, width, height));


        engine.addEntity(e);
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
        player.addComponents(new TextureComponent(new TextureRegion(atlas.findRegion("CaterpillarGun"))));
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
        e.addComponents(new TransformComponent(x, y, 2, 120, 120));
        CollisionComponent c = new CollisionComponent(x + 20, y + 20, 40, 40);
        c.id = "Fire";
        c.isStatic = true;
        e.addComponents(c);
        Animation animation = new Animation(fireAtlas, "fire", 109);
        AnimationComponent a = new AnimationComponent(0.03f, true, animation.getFrames());
        e.addComponents(a);

        engine.addEntity(e);

    }

    @Override
    public void update(float dt) {
        createTileMap();
    }

    public void createTileMap(){


        if (mapSizeIndex > mapSize*mapSize)
            return;

        for (int i = 0; i < 100; i++){
            if (tileMapRenderIndexX < mapSize){
                createTileWithNoise(15*tileMapRenderIndexX, 15*tileMapRenderIndexY,noise.eval( tileMapRenderIndexX*scale, tileMapRenderIndexY*scale, z) );
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

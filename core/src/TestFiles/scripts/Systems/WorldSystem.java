package TestFiles.scripts.Systems;

import EntityEngine.Components.*;
import EntityEngine.Entity;
import EntityEngine.GameClasses.Animation;
import EntityEngine.GameClasses.TDCamera;
import EntityEngine.Noise.OpenSimplexNoise;
import EntityEngine.Systems.CollisionDetectionSystem;
import EntityEngine.Systems.System;
import EntityEngine.Systems.TileMapRenderer;
import TestFiles.scripts.Components.StoneCrabLogic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;



public class WorldSystem extends System {
    double scale = 0.06f;
    float z = 1f;
    int mapSize = 500;
    OpenSimplexNoise noise;
    TextureAtlas atlas;
    TextureAtlas fireAtlas;
    TextureAtlas sC;
    TextureAtlas larvMovement;
    Entity player;
    Entity e;

    TiledMap tileMap;
    TiledMapTileLayer.Cell cell;
    TiledMapTileLayer layer;
    Entity StoneCrab;
    CollisionDetectionSystem col;
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
        col = (CollisionDetectionSystem) engine.getSystem(CollisionDetectionSystem.class);
        sC = engine.assetManager.get("atlas/StoneCrab.atlas");
        atlas = engine.assetManager.get("atlas/TP.atlas");
        fireAtlas = engine.assetManager.get("atlas/Fire.atlas");
        larvMovement = engine.assetManager.get("atlas/LarvMovement.atlas");

        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++)
                createFireAnimationTest(30 * i, 30*j, fireAtlas );
        }

        StoneCrab = new Entity();
        StoneCrab.addComponents(new TextureComponent(new TextureRegion(sC.findRegion("Stonepile", 0))));
        StoneCrab.addComponents(new TransformComponent(200, 250, 2, 34, 34));
        CollisionComponent c = new CollisionComponent(200 - 16, 250 - 16, 64, 64);
        c.id = "StoneCrab";
        StoneCrab.addComponents(c);
        Animation animation = new Animation(sC, "Stonepile", 13);
        AnimationComponent a = new AnimationComponent(0.04f, false, animation.getFrames(), false);
        a.setAlive(false);
        StoneCrab.addComponents(a);
        StoneCrab.addComponents(new StoneCrabLogic());
        engine.addEntity(StoneCrab);


        createPlayers(engine.camera);

        createHouse(300, 200);

        createHouse(300+15*14, 300);

        createHouse(300+15*7, 500);

        createHouse(500+15*14, 500);

        createHouse(500+15*7, 700);

        createHouse(800, 800);




        createTile(300 , 400, "MushroomBig", 60, 140, 10);

        createTile(530 , 200, "Branch", 140, 20, 1);

        createTile(830 , 1200, "WaterTower", 64, 75, 1);




        createTileMapOnCreate();


    }

    private void createTileWithNoise(float x, float y, double noise){
        cell = new TiledMapTileLayer.Cell();
        cell.setTile(new StaticTiledMapTile(generateWithNoise(noise)));
        layer.setCell((int)(x/16), (int)(y/16), cell);
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
            return new TextureRegion(atlas.findRegion("Podsol"));
        }

        else if (value < 0.4f){
            return new TextureRegion(atlas.findRegion("DirtTile"));
        }

        else if (value < 0.70f){

            return new TextureRegion(atlas.findRegion("GrassSmooth"));
        }
        else
            return new TextureRegion(atlas.findRegion("RockyTile"));

    }

    private void createHouse(float x, float y){

        //floor
        for (int i = 0; i < 11; i++){
            float xCord = x+16*i;
            createTile(xCord, y, "StoneWallBottom1", "Wall");

        }

        //right wall
        for (int i = 1; i < 10; i++){
            float yCord = y +16*i;
            createTile(x, yCord, "StoneWallSideLeft", "Wall");
        }

        //Left wall
        for (int i = 1; i < 10; i++){
            float xCord = x + 16*10;
            float yCord = y +16*i;
            createTile(xCord, yCord, "StoneWallSideRight", "Wall");
        }



        //Roof
        for (int i = 1; i < 10; i++){
            float xCord = x + 16*i;
            float yCord = y + 16*10;
            createTile(xCord, yCord, "StoneWallTop", "Wall");
        }

        for (int i = 1; i < 10; i++){
            float xCord = x + 16*i;

            for (int j = 1; j < 10; j++){
                float yCord = y + 16*j;
                createTile(xCord, yCord, "StoneTile", "Floor");

            }
        }

        //corners
        createTile(x, y + 16*10, "StoneCornerUpperLeft", "Wall");
        createTile(x + 16*10, y + 16*10, "StoneCornerUpperRight", "Wall");


        //Random stuff
        createTile(x + 6*16, y+3*16, "BlueTorch", "Torch");

        createTile(x + 9*16, y+8*16, "BlueTorch", "Torch");

        createTile(x + 6*16, y+7*16, "Quartz", "Quartz");

        createTile(x + 4*16, y+5*16, "Table", 32,29, 2);
        createTile(x + 6*16, y+5*16, "ChairRight", 16,32, 2);
        createTile(x + 3*16, y+5*16, "ChairLeft",16,32, 2);


    }

    public void createTile(float x, float y, String name, String id){
        e = new Entity();
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
        player.addComponents(new TextureComponent(new TextureRegion(larvMovement.findRegion("CaterpillarGun"))));
        player.addComponents(new TransformComponent(camera.viewportWidth / 2, camera.viewportHeight / 2, 5, 32, 32));
        CollisionComponent c = new CollisionComponent(camera.viewportWidth / 2, camera.viewportHeight / 2, 32, 32);
        c.id = "Player";
        player.addComponents(c);
        player.addComponents(new VelocityComponent());
        player.tag = "Player";

        Animation animation = new Animation(larvMovement, "CaterpillarGun", 6);
        AnimationComponent a = new AnimationComponent(0.07f, false, animation.getFrames(), false);
        a.setAlive(false);
        player.addComponents(a);

        RigidBody2D box2d = new RigidBody2D(camera.viewportWidth / 2, camera.viewportHeight / 2, 16, 16, 0);
        box2d.addToWorld(engine.world);
        box2d.getBody().setFixedRotation(true);
        player.addComponents(box2d);

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

        e = engine.getEntity("Player");
        CollisionComponent StoneCol = (CollisionComponent) StoneCrab.getComponent(CollisionComponent.class);
        AnimationComponent a = (AnimationComponent) StoneCrab.getComponent(AnimationComponent.class);
        StoneCrabLogic logic = (StoneCrabLogic) StoneCrab.getComponent(StoneCrabLogic.class);

        if (col.CollisionWithID(StoneCol, "Player")){

            if (!logic.isScared){
                a.setAlive(true);
                logic.setScared(true);
            }
        }

        if (!a.isAlive() && logic.isScared){
            TextureComponent t = (TextureComponent) StoneCrab.getComponent(TextureComponent.class);
            t.setTexture(new TextureRegion(sC.findRegion("Stonepile", 12)));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)){
            logic.setScared(false);

            TextureComponent t = (TextureComponent) StoneCrab.getComponent(TextureComponent.class);
            t.setTexture(new TextureRegion(sC.findRegion("Stonepile")));

        }

    }

    private void createTileMapOnCreate(){
        tileMap = new TiledMap();
        MapLayers layers = tileMap.getLayers();
        layer = new TiledMapTileLayer(mapSize, mapSize, 16, 16);
        layer.setName("map");


        for (int i = 0; i < mapSize; i++){
            for (int j = 0; j < mapSize; j++){
                createTileWithNoise(16*i, 16*j,noise.eval( i*scale, j*scale, z) );
            }
        }

        layers.add(layer);
        TileMapRenderer renderer = (TileMapRenderer) engine.getSystem(TileMapRenderer.class);
        renderer.addTilemap(tileMap);
    }
}

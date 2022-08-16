package TestFiles.scripts.Systems;

import EntityEngine.Components.*;
import EntityEngine.Utils.Entity;
import EntityEngine.Utils.Animation;
import EntityEngine.Components.Node;
import EntityEngine.Utils.TDCamera;
import EntityEngine.Utils.OpenSimplexNoise;
import EntityEngine.Systems.*;
import EntityEngine.Systems.System;
import TestFiles.scripts.Components.ActorComponent;
import TestFiles.scripts.Components.StoneCrabLogic;
import TestFiles.scripts.sim.Element;
import TestFiles.scripts.sim.ElementState;
import TestFiles.scripts.sim.TileSim;
import TestFiles.scripts.sim.TileSimManager;
import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class WorldSystem extends System {
    double scale = 0.06f;
    float z = 1f;
    int mapSize = 30;
    OpenSimplexNoise noise;
    TextureAtlas atlas;
    TextureAtlas fireAtlas;
    TextureAtlas sC;
    TextureAtlas larvMovement;

    CollisionDetectionSystem col;
    NavMesh navMesh;
    TileSimManager tileSimManager;


    SpriteBatch batch;


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


        engine.lightning.setAmbientLight(0.2f);

        col = (CollisionDetectionSystem) engine.getSystem(CollisionDetectionSystem.class);
        sC = engine.assetManager.get("atlas/StoneCrab.atlas");
        atlas = engine.assetManager.get("atlas/TP.atlas");
        fireAtlas = engine.assetManager.get("atlas/Fire.atlas");
        larvMovement = engine.assetManager.get("atlas/LarvMovement.atlas");

        navMesh = (NavMesh) engine.getSystem(NavMesh.class);
        navMesh.setNodeSize(16);

        tileSimManager = (TileSimManager) engine.getSystem(TileSimManager.class);
        tileSimManager.setTileSize(16);
        batch = new SpriteBatch();

        createTileMap();

        engine.addEntity(player(engine.camera));

        engine.addEntity(stoneCrab(10, 10));
        engine.addEntity(stoneCrab(19, 20));

        createHouse(7*16, 5*16);

        createHouse(600, 300);

        engine.addEntity(createTile(30*16, 10*16, "MushroomBig", 60, 140, 10));
        engine.addEntity(createTile(530 , 200, "Branch", 140, 20, 1));
        engine.addEntity(createTile(830 , 1200, "WaterTower", 64, 75, 1));


        Entity e = new Entity();
        TextureComponent textureComponent = new TextureComponent(new TextureRegion(atlas.findRegion("Branch")));

        e.addComponents(textureComponent);
        e.addComponents(new UIElement(0, 0, 1, 500, 100));

        ActorComponent actorComponent = new ActorComponent(0, 0, 500, 100);
        actorComponent.actor.setDebug(true);
        actorComponent.actor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                java.lang.System.out.println("Test");
            }
        });

        actorComponent.isSpatial = false;
        e.addComponents(actorComponent);

        engine.addEntity(e);


    }



    @Override
    public void dispose() {

    }

    private void createTileWithNoise(float x, float y, double noise, TiledMapTileLayer layer){
        Entity e = new Entity();
        Node node = new Node();
        node.setPos(new Vector2(x, y));
        e.addComponents(node);

        TileSim tileSim = new TileSim(x,y);
        while (engine.getRandomInteger(10) > 2){
            tileSim.addElement(new Element(1, 5, 1, ElementState.GAS));
        }
        e.addComponents(tileSim);

        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(new StaticTiledMapTile(generateWithNoise(noise)));
        layer.setCell((int)(x/16), (int)(y/16), cell);





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
        createTile(x + 6*16, y+3*16, "BlueTorch", "Light");

        createTile(x + 9*16, y+8*16, "BlueTorch", "Light");

        createTile(x + 6*16, y+7*16, "Quartz", "Quartz");

        createTile(x + 4*16, y+5*16, "Table", 32,29, 3);
        createTile(x + 6*16, y+5*16, "ChairRight", 16,32, 3);
        createTile(x + 3*16, y+5*16, "ChairLeft",16,32, 3);

        createTile(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, "ChairLeft",64,64, 3);



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

            navMesh.setNodeBlocked(x, y);
            tileSimManager.setBlocked(x,y);
            TileSim tile = tileSimManager.getTile(x,y);
            if (tile != null)
                tile.clearElements();
        }

        else if (id.equals("Light")){
            Light light = new Light(new PointLight(engine.lightning, 15, null, 40, x + 8, y + 8));
            light.colorAndSoftnessLength(new Color(0.1f,0.3f,0.4f,0.8f), 2f);
            light.setStatic(true);
            e.addComponents(light);
        }

        engine.addEntity(e);
    }

    public Entity createTile(float x, float y, String name, float width, float height, float z){
        Entity e = new Entity();
        e.addComponents(new TextureComponent(new TextureRegion(atlas.findRegion(name))));
        e.addComponents(new TransformComponent(x, y, z, width, height));

        return e;
    }

    public Entity player(TDCamera camera){
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

    private Entity stoneCrab(int tileX, int tileY){
        Entity StoneCrab = new Entity();
        StoneCrab.addComponents(new TextureComponent(new TextureRegion(sC.findRegion("Stonepile", 0))));
        StoneCrab.addComponents(new TransformComponent(tileX * 16, tileY * 16, 2, 34, 34));
        CollisionComponent c = new CollisionComponent(tileX* 16 - 16, tileY*16 - 16, 64, 64);
        c.id = "StoneCrab";
        StoneCrab.addComponents(c);
        Animation animation = new Animation(sC, "Stonepile", 13);
        AnimationComponent a = new AnimationComponent(0.04f, false, animation.getFrames(), false);
        a.setAlive(false);
        StoneCrab.addComponents(a);
        StoneCrab.addComponents(new StoneCrabLogic());

        return StoneCrab;
    }

    private void createTileMap(){
        TileMapRenderer renderer = (TileMapRenderer) engine.getSystem(TileMapRenderer.class);
        TiledMapTileLayer layer = renderer.createLayer(mapSize, mapSize, 16, 16);

        for (int i = 0; i < mapSize; i++){
            for (int j = 0; j < mapSize; j++){
                createTileWithNoise(16*i, 16*j,noise.eval( i*scale, j*scale, z), layer );
            }
        }
    }
}

package TestFiles.scripts;

import EntityEngine.Components.*;
import EntityEngine.Entity;
import EntityEngine.GameClasses.Animation;
import EntityEngine.GameClasses.TDCamera;
import EntityEngine.Noise.OpenSimplexNoise;
import EntityEngine.Systems.System;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;


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

    Entity e;

    Array<Array<Entity>> home;
    public WorldSystem(TextureAtlas atlas){
        noise = new OpenSimplexNoise(); //for tilemap generation
        this.atlas = atlas;
    }

    @Override
    public void onCreate() {

        Pixmap cursorPm = new Pixmap(Gdx.files.internal("tempcursor.png"));
        int xHotSpot = cursorPm.getWidth() ;
        int yHotSpot = cursorPm.getHeight();
        Cursor cursor = Gdx.graphics.newCursor(cursorPm, xHotSpot - 1, yHotSpot -1);

        Gdx.graphics.setCursor(cursor);
        cursorPm.dispose();


        TextureAtlas fireAtlas = new TextureAtlas("atlas/Fire.atlas");
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++)
                createFireAnimationTest(30 * i, 30*j, fireAtlas );
        }

        createPlayers(engine.camera);

        createHouse(300, 200);

        createHouse(300+15*14, 300);

        createHouse(300+15*7, 500);

        createTile(300 , 400, "MushroomBig", 60, 140, 10);
    }

    private void createHouse(float x, float y){
        //floor
        for (int i = 1; i < 10; i++){
            float xCord = x+15*i;
            float yCord = y;
            createTile(xCord, yCord, "Grassfloor", "Wall");

        }

        //right wall
        for (int i = 0; i < 10; i++){
            float xCord = x;
            float yCord = y +15*i;
            createTile(xCord, yCord, "Grassfloor", "Wall");
        }

        //Left wall
        for (int i = 0; i < 10; i++){
            float xCord = x + 15*10;
            float yCord = y +15*i;
            createTile(xCord, yCord, "Grassfloor", "Wall");
        }

        //Roof
        for (int i = 0; i < 11; i++){
            float xCord = x + 15*i;
            float yCord = y + 15*10;
            createTile(xCord, yCord, "Grassfloor", "Wall");
        }

        for (int i = 1; i < 10; i++){
            float xCord = x + 15*i;

            for (int j = 1; j < 10; j++){
                float yCord = y + 15*j;
                createTile(xCord, yCord, "MushroomFloor", "Floor");

            }
        }

        createTile(x + 5*15, y+15, "CrystalTile", "Crystal");

        createTile(x + 2*15, y+7*15, "CrystalTile", "Crystal");

        createTile(x + 6*15, y+3*15, "BlueTorch", "Torch");

        createTile(x + 9*15, y+8*15, "BlueTorch", "Torch");

    }

    public void createTile(float x, float y, String name, String id){
        e = new Entity();
        e.addComponents(new TextureComponent(new TextureRegion(atlas.findRegion(name))));
        e.addComponents(new TransformComponent(x, y, 1, 15, 15));
        CollisionComponent c = new CollisionComponent(x, y, 15, 15);
        c.id = id;
        c.isStatic = true;
        e.addComponents(c);

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

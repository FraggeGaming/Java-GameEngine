package TestFiles.scripts;

import EntityEngine.Noise.OpenSimplexNoise;
import EntityEngine.Systems.System;
import EntityEngine.Tile;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class WorldSystem extends System {
    int tileMapRenderIndexX = 0;
    int tileMapRenderIndexY = 0;
    double scale = 0.04f;
    float z = 1f;
    int mapSize = 1000;
    int mapSizeIndex = 0;
    OpenSimplexNoise noise;
    TextureAtlas atlas;
    public WorldSystem(TextureAtlas atlas){
        noise = new OpenSimplexNoise(); //for tilemap generation
        this.atlas = atlas;
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

package TestFiles.scripts.sim;


import EntityEngine.Systems.System;
import EntityEngine.Utils.Entity;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.List;

public class TileSimManager extends System {

    TileSim tileSim;
    public List<List<TileSim>> tileMap;
    public int tileSize = 16;

    ShapeRenderer shapeRenderer = new ShapeRenderer();


    @Override
    public void onCreate() {
        shapeRenderer.setAutoShapeType(true);
        tileMap = new ArrayList<>();

    }

    @Override
    public void addEntity(Entity entity) {

        if (entity.getComponent(TileSim.class) != null){
            tileSim = (TileSim) entity.getComponent(TileSim.class);

            while (tileMap.size() <= tileSim.x/tileSize){
                tileMap.add(new ArrayList<>());
            }
            tileMap.get((int) tileSim.x/tileSize ).add((int)tileSim.y/tileSize, tileSim);
        }
    }

    public TileSim getTile(int x, int y){
        return tileMap.get(x).get(y);
    }

    public void freeTile(float x, float y){
        getTile(x,y).blocked = false;
    }

    public void setBlocked(float x, float y){
        getTile(x,y).blocked = true;
    }


    public TileSim getTile(float x, float y) {
        return tileMap.get((int)x/tileSize).get((int)y/tileSize);
    }

    @Override
    public void update(float dt) {

    }


    public void setTileSize(int i) {
        tileSize = i;
    }
}

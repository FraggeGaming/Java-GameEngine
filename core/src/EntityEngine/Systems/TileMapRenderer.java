package EntityEngine.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

public class TileMapRenderer extends System{
    TiledMapRenderer tiledMapRenderer;
    Array<TiledMapTileLayer> layers;
    TiledMap map;
    MapLayers mapLayer;

    @Override
    public void onCreate() {
        map = new TiledMap();

        addTilemap(map);
        layers = new Array<>();
    }


    public TiledMapTileLayer getLayer(int i){
        return layers.get(i);
    }

    public TiledMapTileLayer createLayer(int width, int height, int tileWidth, int tileHeight){
        mapLayer = map.getLayers();
        TiledMapTileLayer layer = new TiledMapTileLayer(width, height, tileWidth, tileHeight);
        mapLayer.add(layer);
        layers.add(layer);

        return layer;
    }

    @Override
    public void reset() {

        dispose();



    }

    @Override
    public void preRender(float dt) {
        if (tiledMapRenderer == null)
            return;


        tiledMapRenderer.setView(engine.camera);
        tiledMapRenderer.render();


    }

    @Override
    public void dispose() {
        layers.clear();

        //set the map in your renderer
        //tiledMapRenderer.dispose();
    }

    public void addTilemap(TiledMap tiledMap){
        tiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap);
    }

    public void setCell(int layer,int x, int y, TiledMapTileLayer.Cell cell){
        layers.get(layer).setCell(x, y, cell);
    }

}

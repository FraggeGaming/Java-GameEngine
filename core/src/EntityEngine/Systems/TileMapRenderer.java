package EntityEngine.Systems;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TileMapRenderer extends System{
    TiledMapRenderer tiledMapRenderer;

    @Override
    public void update(float dt) {
        if (tiledMapRenderer == null)
            return;

        tiledMapRenderer.setView(engine.camera);
        tiledMapRenderer.render();
    }

    public void addTilemap(TiledMap tiledMap){
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

}

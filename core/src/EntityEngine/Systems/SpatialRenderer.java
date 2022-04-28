package EntityEngine.Systems;

import EntityEngine.Components.*;
import EntityEngine.Entity;
import EntityEngine.Renderer.Cell;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class SpatialRenderer extends System {

    SpriteCache cache = new SpriteCache();
    Vector2 worldSize = new Vector2(0, 0);
    public int drawnEntities;
    TextureComponent t;
    TransformComponent transform;
    Component c;
    Component x;

    Array<Component> comp;
    public SpatialRenderer(){

    }

    @Override
    public void update(float dt){
        renderWithCMS();
    }

    private void renderWithCMS() {

        comp = engine.getloadedComponents(TextureComponent.class);
        if (comp != null){
            drawnEntities = 0;
            engine.getBatch().begin();

            for (int i = 0; i < comp.size; i++){
                t = (TextureComponent) comp.get(i);
                if (engine.getEntity(t.getId()) != null){
                    transform = (TransformComponent) engine.getEntityComponent(t.getId(), TransformComponent.class);
                    drawTexture(t, transform);
                }


            }

            engine.getBatch().end();
        }

    }

    private void drawTexture(TextureComponent texture, TransformComponent transform){
        if (texture != null){
            engine.getBatch().draw(texture.getRegion(), transform.getX(), transform.getY(),
                    transform.getWidth()/2, transform.getHeight()/2,

                    transform.getWidth(), transform.getHeight() ,
                    1, 1,   0);
            drawnEntities++;
        }
    }


    @Override
    public void addEntity(Entity entity){

        c = entity.getComponent(TextureComponent.class);
        x = entity.getComponent(TransformComponent.class);

        if (c != null && x != null){
            setWorldSize((TransformComponent)x);
        }

    }

    private void setWorldSize(TransformComponent component){
        if (component.getX() > worldSize.x){
            worldSize.x = component.getX();
        }

        if (component.getY() > worldSize.y){
            worldSize.y = component.getY();
        }

    }


}

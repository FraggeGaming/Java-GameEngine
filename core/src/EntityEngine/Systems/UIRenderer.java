package EntityEngine.Systems;

import EntityEngine.Architect.Architect;
import EntityEngine.Architect.Type;
import EntityEngine.Components.Component;
import EntityEngine.Components.TextureComponent;
import EntityEngine.Components.TransformComponent;
import EntityEngine.Components.UIElement;
import EntityEngine.Renderer.Cell;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class UIRenderer extends System{


    TextureComponent t;
    UIElement transform;


    Architect architect;

    Array<Integer> ints;
    Array<Component> transformArray;
    Array<Component> textureArray;

    SpriteBatch batch;

    @Override
    public void onCreate() {
        architect = engine.architectHandler.getArchitect(new Type(TextureComponent.class, UIElement.class));
        batch = new SpriteBatch();
    }

    @Override
    public void reset() {
        architect = engine.architectHandler.getArchitect(new Type(TextureComponent.class, UIElement.class));

    }

    @Override
    public void update(float dt){

        transformArray = architect.getComponents(UIElement.class);
        textureArray = architect.getComponents(TextureComponent.class);
    }

    @Override
    public void render(float dt) {

    }

    @Override
    public void UIRender(float dt) {

        if (transformArray == null)
            return;

        batch.begin();

        for (int i = 0; i < transformArray.size; i++){

            transform = (UIElement) transformArray.get(i);
            t = (TextureComponent) textureArray.get(i);

            drawTexture(t, transform);
        }

        batch.end();
    }

    private void drawTexture(TextureComponent texture, UIElement transform){
        if (texture != null){
            batch.draw(texture.getRegion(), transform.getX(), transform.getY(),
                    transform.getWidth()/2, transform.getHeight()/2,

                    transform.getWidth(), transform.getHeight() ,
                    transform.getScaleX(), transform.getScaleY(),   transform.getRotation());
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}




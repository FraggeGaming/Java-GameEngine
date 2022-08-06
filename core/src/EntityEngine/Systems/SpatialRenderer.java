package EntityEngine.Systems;

import EntityEngine.Architect.Architect;
import EntityEngine.Components.*;
import EntityEngine.Entity;
import EntityEngine.Renderer.Cell;
import EntityEngine.Architect.Type;
import TestFiles.scripts.Components.WaterComponent;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.crashinvaders.vfx.effects.WaterDistortionEffect;

public class SpatialRenderer extends System {

    Vector2 worldSize = new Vector2(0, 0);
    public int drawnEntities;
    TextureComponent t;
    TransformComponent transform;
    Component c;
    Component x;

    Architect architect;
    Array<Integer> ints;
    Array<Component> transformArray;
    Array<Component> textureArray;




    public WaterDistortionEffect waterDistortionEffect = new WaterDistortionEffect(3, .5f);
    public SpatialRenderer(){

    }

    @Override
    public void onCreate() {
        architect = engine.architectHandler.getArchitect(new Type(TransformComponent.class, TextureComponent.class));
    }

    @Override
    public void update(float dt){
        drawnEntities = 0;
        ints = engine.getSpatialArc(architect);
        transformArray = architect.getComponents(TransformComponent.class);
        textureArray = architect.getComponents(TextureComponent.class);
    }

    @Override
    public void render(float dt) {


        if (ints != null && !ints.isEmpty()){
            for (int i = 0; i < ints.size; i++){

                transform = (TransformComponent) transformArray.get(ints.get(i));
                t = (TextureComponent) textureArray.get(ints.get(i));

                if (t.draw)
                    drawTexture(t, transform);
            }
        }

    }

    /*@Override
    public void postRender(float dt) {
        for (int i = 0; i < engine.effects.size; i++){
            engine.vfxManager.removeEffect(engine.effects.get(i));
        }

        engine.vfxManager.cleanUpBuffers();
        // Begin render to an off-screen buffer.
        engine.vfxManager.addEffect(waterDistortionEffect);
        engine.vfxManager.beginInputCapture();
        engine.batch.begin();

        if (ints != null && !ints.isEmpty()){
            for (int i = 0; i < ints.size; i++){

                transform = (TransformComponent) transformArray.get(ints.get(i));
                t = (TextureComponent) textureArray.get(ints.get(i));

                if (engine.getEntityComponent(t.getId(), WaterComponent.class) != null)
                    drawTexture(t, transform);
            }
        }

        engine.batch.end();
        engine.vfxManager.endInputCapture();
        // Apply the effects chain to the captured frame.
        engine.vfxManager.update(dt);

        engine.vfxManager.applyEffects();
        // Render result to the screen.
        engine.vfxManager.renderToScreen();

        engine.vfxManager.removeEffect(waterDistortionEffect);

        for (int i = 0; i < engine.effects.size; i++){
            engine.vfxManager.addEffect(engine.effects.get(i));
        }

    }*/


    private void drawTexture(TextureComponent texture, TransformComponent transform){
        if (texture != null){
            engine.getBatch().draw(texture.getRegion(), transform.getX(), transform.getY(),
                    transform.getWidth()/2, transform.getHeight()/2,

                    transform.getWidth(), transform.getHeight() ,
                    transform.getScaleX(), transform.getScaleY(),   transform.getRotation());
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

package EntityEngine.Systems;

import EntityEngine.Architect;
import EntityEngine.Components.*;
import EntityEngine.Entity;
import EntityEngine.Renderer.Cell;
import EntityEngine.Renderer.TransformComparator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class SpatialRenderer extends System {

    Vector2 worldSize = new Vector2(0, 0);
    public int drawnEntities;
    TextureComponent t;
    TransformComponent transform;
    Component c;
    Component x;

    Array<Component> comp;

    Array<Cell> cells;
    Array<TransformComponent> compt = new Array<>();

    public SpatialRenderer(){

    }

    @Override
    public void update(float dt){
        drawnEntities = 0;
        arc();
    }

    private void renderWiothoutCMS(){


        cells = engine.getCellsFromCameraCenter();
        for (int i = 0; i < cells.size; i++){
            compt.addAll((Array<? extends TransformComponent>) cells.get(i).getComponents(TransformComponent.class));
        }
        compt.sort(new TransformComparator());



        engine.getBatch().begin();
        for (int i = 0; i < compt.size; i++){

            transform = compt.get(i);
            t = (TextureComponent) engine.getEntityComponent(transform.getId(), TextureComponent.class);
            drawTexture(t, transform);

        }
        engine.getBatch().end();

        compt.clear();
    }

    private void arc() {

        Array<Integer> ints = engine.NearbyComponentsFromArc((byte) 0x1);
        Architect architect = engine.architectHandler.getArchitect((byte) 0x1);
        Array<Component> transformArray = architect.getComponents(TransformComponent.class);
        Array<Component> textureArray = architect.getComponents(TextureComponent.class);

        if (!ints.isEmpty()){

            engine.getBatch().begin();

            for (int i = 0; i < ints.size; i++){

                transform = (TransformComponent) transformArray.get(ints.get(i));
                t = (TextureComponent) textureArray.get(ints.get(i));

                drawTexture(t, transform);
            }

            engine.getBatch().end();
        }
    }


    private void renderWithCMS() {

        comp = engine.getloadedComponents(TextureComponent.class);
        if (comp != null){

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

    private void renderArray(Array<TransformComponent> comp){
        if (comp != null){
            for (int i = 0; i < comp.size; i++){

                transform = comp.get(i);
                t = (TextureComponent) engine.getEntityComponent(transform.getId(), TextureComponent.class);
                drawTexture(t, transform);

            }
        }
    }

    private void drawTexture(TextureComponent texture, TransformComponent transform){
        if (texture != null){
            engine.getBatch().draw(texture.getRegion(), transform.getX(), transform.getY(),
                    transform.getWidth()/2, transform.getHeight()/2,

                    transform.getWidth(), transform.getHeight() ,
                    1, 1,   transform.getRotation());
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

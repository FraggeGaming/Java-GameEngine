package EntityEngine;

import EntityEngine.Components.CollisionComponent;
import EntityEngine.Components.Component;
import EntityEngine.Components.TextureComponent;
import EntityEngine.Components.TransformComponent;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Tile {

    private final Array<Component> components = new Array<>();
    private final TextureAtlas atlas;
    CollisionComponent c;
    TransformComponent transform;
    Entity e;
    public Tile(TextureAtlas atlas, double value, float x, float y, float i, float width, float height){
        this.atlas = atlas;
        c = new CollisionComponent(x, y, width, height);
        transform = new TransformComponent(x, y, i, width, height);
        components.add(transform);
        components.add(new TextureComponent(getTexture(value)));

        e = new Entity();
        e.setComponents(components);

        CollisionComponent c = (CollisionComponent) e.getComponent(CollisionComponent.class);
        if (c != null){
            c.isStatic = true;

        }

    }

    public TextureRegion getTexture(double value){
        //System.out.println(value);
        if (value < -0.3f){
            return new TextureRegion(atlas.findRegion("DeepWater"));
        }

        if (value < -0.2f){
            return new TextureRegion(atlas.findRegion("TransitionWater"));
        }

        if (value < 0f){
            //components.add(c);
            //c.id = "Water";
            return new TextureRegion(atlas.findRegion("Water"));
        }

        else if (value < 0.1f){
            components.add(c);
            c.id = "Wall";
            return new TextureRegion(atlas.findRegion("Sand"));
        }

        else if (value < 0.2f){
            //components.add(c);
            //c.id = "Podsol";
            return new TextureRegion(atlas.findRegion("Podsol"));
        }

        else if (value < 0.4f){
            //components.add(c);
            //c.id = "DirtTile";
            return new TextureRegion(atlas.findRegion("DirtTile"));
        }

        else if (value < 0.70f){

            return new TextureRegion(atlas.findRegion("RockyTile"));
        }
        else
            return new TextureRegion(atlas.findRegion("RockyTile"));
    }

    public Entity getEntity() {
        return e;
    }
}

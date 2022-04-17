package EntityEngine.Systems;

import EntityEngine.Components.CollisionComponent;
import EntityEngine.Components.Component;
import EntityEngine.Components.TextureComponent;
import EntityEngine.Components.TransformComponent;
import EntityEngine.Renderer.Cell;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.HashSet;

public class Debugger extends System {
    ShapeRenderer shapeRenderer;
    private Array<Cell> loadedCells;
    private Array<Component> comp;
    TransformComponent t;
    CollisionComponent c;
    public boolean debugBox2D = false;
    public boolean debug = false;
    CollisionDetectionSystem cSystem;
    float timeStep = 0;
    float stepValue = 20;
    public HashSet<CollisionComponent> collisionsDebug = new HashSet<>();
    public Debugger(){
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
    }

    @Override
    public void update(float dt) {

        if (!debug)
            return;

        if (timeStep >= 1/stepValue){
            clearCollisions();
            timeStep = 0;
        }
        findCollisions();

        shapeRenderer.setProjectionMatrix(engine.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        drawCells();
        drawBox2D();

        shapeRenderer.end();

        timeStep += dt;


    }

    private void drawCells() {

        if (debugBox2D)
            return;

        comp = engine.getloadedComponents(TransformComponent.class);
        if (comp != null){

            for (int i = 0; i < comp.size; i++){
              t = (TransformComponent) comp.get(i);

                if (t.getZ() < 1)
                    shapeRenderer.setColor(Color.GREEN);
                else{
                    shapeRenderer.setColor(Color.BLUE);
                }
                shapeRenderer.rect(t.getX(), t.getY(), t.getWidth(), t.getHeight());
            }
        }

    }

    private void findCollisions(){

        if (!debugBox2D)
           return;

        if (cSystem == null)
            cSystem = (CollisionDetectionSystem) engine.getSystem(CollisionDetectionSystem.class);

        collisionsDebug.addAll(cSystem.collisions);
    }

    private void clearCollisions(){
        if (!debugBox2D)
           return;


        if (cSystem == null)
            cSystem = (CollisionDetectionSystem) engine.getSystem(CollisionDetectionSystem.class);

        collisionsDebug.clear();
    }

    private void drawBox2D(){
        if (!debugBox2D)
            return;

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        for (CollisionComponent c : collisionsDebug){
            shapeRenderer.rect(c.boundingBox.x, c.boundingBox.y, c.boundingBox.width, c.boundingBox.height);
        }
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);


        comp = engine.getloadedComponents(CollisionComponent.class);
        if (comp != null){

            for (int i = 0; i < comp.size; i++){
               c = (CollisionComponent) comp.get(i);

                if (!collisionsDebug.contains(c)){

                    shapeRenderer.setColor(Color.BLUE);

                    shapeRenderer.rect(c.boundingBox.x, c.boundingBox.y, c.boundingBox.width, c.boundingBox.height);
                }

            }
        }

    }
}

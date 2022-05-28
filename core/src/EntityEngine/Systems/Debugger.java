package EntityEngine.Systems;

import EntityEngine.Components.CollisionComponent;
import EntityEngine.Components.Component;
import EntityEngine.Components.TextureComponent;
import EntityEngine.Components.TransformComponent;
import EntityEngine.Renderer.Cell;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
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
    public HashSet<CollisionComponent> collisionsDebug = new HashSet<>();
    Box2DDebugRenderer debugRenderer;
    public Debugger(){
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void update(float dt) {

        if (!debug)
            return;

        if (debugBox2D)
            collisionsDebug = findCollisions();

        shapeRenderer.setProjectionMatrix(engine.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        drawCells();
        drawBox2D();

        shapeRenderer.end();

        if (debugBox2D)
            debugRenderer.render(engine.world, engine.camera.combined);
    }

    private void drawCells() {

        if (debugBox2D)
            return;

        loadedCells = engine.getSpatialHashGrid().getNeighbours();
        if (loadedCells != null){

            for (int i = 0; i < loadedCells.size; i++){
                for (int j = 0; j < loadedCells.get(i).getComponents(TransformComponent.class).size; j++){
                    t = (TransformComponent) loadedCells.get(i).getComponents(TransformComponent.class).get(j);

                    if (t.getZ() < 1)
                        shapeRenderer.setColor(Color.GREEN);
                    else{
                        shapeRenderer.setColor(Color.BLUE);
                    }
                    shapeRenderer.rect(t.getX(), t.getY(), t.getWidth(), t.getHeight());
                }

            }
        }

    }

    private HashSet<CollisionComponent> findCollisions(){

        if (cSystem == null)
            cSystem = (CollisionDetectionSystem) engine.getSystem(CollisionDetectionSystem.class);

        return cSystem.collisions;
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

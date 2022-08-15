package EntityEngine.Systems;

import EntityEngine.Components.*;
import EntityEngine.Utils.Tags;
import EntityEngine.Renderer.Cell;
import TestFiles.scripts.sim.TileSim;
import TestFiles.scripts.sim.TileSimManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
    public boolean debugNavmesh = false;
    public boolean debugTileData = false;
    CollisionDetectionSystem cSystem;
    public HashSet<CollisionComponent> collisionsDebug = new HashSet<>();
    Box2DDebugRenderer debugRenderer;
    NavMesh navMesh;
    TileSimManager tileSimManager;

    Tags tags;
    public Debugger(){
        tags = new Tags();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void onCreate() {
        navMesh = (NavMesh) engine.getSystem(NavMesh.class);
        tileSimManager = (TileSimManager) engine.getSystem(TileSimManager.class);
    }

    @Override
    public void postRender(float dt) {

        if (debugBox2D)
            collisionsDebug = findCollisions();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(engine.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        drawCells();
        drawBox2D();
        renderNavMesh();
        renderTileData();
        shapeRenderer.end();

        if (debugBox2D)
            debugRenderer.render(engine.world, engine.camera.combined);
    }

    @Override
    public void update(float dt) {


    }

    private void renderTileData(){
        if (!debugTileData)
            return;


        for (int i = 0; i < tileSimManager.tileMap.size(); i++){
            for (int j = 0; j < tileSimManager.tileMap.get(i).size(); j++){
                shapeRenderer.set(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.BLACK);
                TileSim tileSim = tileSimManager.tileMap.get(i).get(j);

                if (tileSim.blocked){
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.setColor(Color.RED);
                }
                else if (tileSim.elements.size > 0){
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    if (tileSim.elements.size == 1)
                        shapeRenderer.setColor(Color.GREEN);
                    else if (tileSim.elements.size == 2)
                        shapeRenderer.setColor(Color.YELLOW);
                    else if (tileSim.elements.size == 3)
                        shapeRenderer.setColor(Color.ORANGE);
                    else if (tileSim.elements.size == 4)
                        shapeRenderer.setColor(Color.PURPLE);
                    else if (tileSim.elements.size > 4)
                        shapeRenderer.setColor(Color.BLUE);
                }

                shapeRenderer.rect(tileSim.x , tileSim.y , tileSimManager.tileSize, tileSimManager.tileSize);

            }
        }
    }

    private void renderNavMesh(){
        if (debugNavmesh){

            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            for (int i = 0; i < navMesh.nodeMap.size(); i++){
                for (int j = 0; j < navMesh.nodeMap.get(i).size(); j++){
                    Node node = navMesh.nodeMap.get(i).get(j);

                    if (node.isBlocked){
                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.rect(node.getPos().x, node.getPos().y, navMesh.nodeSize,  navMesh.nodeSize);
                    }

                    else if (node.debug){
                        shapeRenderer.setColor(Color.GREEN);
                        shapeRenderer.rect(node.getPos().x, node.getPos().y, navMesh.nodeSize,  navMesh.nodeSize);
                    }


                }
            }
        }
    }

    private void drawCells() {

        if (!debug)
            return;

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

        if (!debug)
            return;

        if (!debugBox2D)
            return;

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        for (CollisionComponent c : collisionsDebug){
            shapeRenderer.rect(c.boundingBox.x, c.boundingBox.y, c.boundingBox.width, c.boundingBox.height);
        }
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);


    }
}

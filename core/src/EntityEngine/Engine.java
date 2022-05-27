package EntityEngine;

import EntityEngine.Components.Component;
import EntityEngine.Components.RigidBody2D;
import EntityEngine.Components.TransformComponent;
import EntityEngine.GameClasses.TDCamera;
import EntityEngine.Renderer.Cell;
import EntityEngine.Renderer.SpatialHashGrid;
import EntityEngine.Renderer.TransformComparator;
import EntityEngine.Systems.*;
import EntityEngine.Systems.System;
import TestFiles.scripts.Systems.TimerSystem;
import box2dLight.RayHandler;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Engine {

    public final HashMap<Integer, Entity> componentMap = new HashMap<>();
    public final HashMap<String, Entity> entityMapper = new HashMap<>();
    private final Array<System> systems = new Array<>();
    private final SpatialHashGrid spatialHashGrid;
    public ExecutorService pool;

    public TDCamera camera;
    public Batch batch;
    int entityId = 0;
    public List<Entity> flagedForDelete = new ArrayList<>();
    public List<Entity> flagedRigidBodyforDelete = new ArrayList<>();

    public String user; // for networking //TODO change this later for more modular implementation

    boolean running = false;

    public AssetManager assetManager;
    public World world;
    public RayHandler lightning;

    public boolean threadedParsing = true;

    public ArchitectHandler architectHandler = new ArchitectHandler();


    public Engine(float width, float height, Script scriptLoader){

        camera = new TDCamera(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        batch = new SpriteBatch();
        spatialHashGrid = new SpatialHashGrid(this);
        spatialHashGrid.setData((int) camera.viewportHeight);
        pool = Executors.newCachedThreadPool();

        assetManager = new AssetManager();
        world = new World(new Vector2(0, 0f),true);
        lightning = new RayHandler(world);

        initSetup();

        scriptLoader.addEngine(this);
        scriptLoader.loadAssets();
        scriptLoader.onCreate();

        buildSystems();
    }

    public void initSetup(){
        addSystem(new TileMapRenderer());
        addSystem(new SpatialRenderer());
        addSystem(new NetworkManager());
        addSystem(new AnimationSystem());
        addSystem(new CollisionDetectionSystem());
        addSystem(new ComponentManagerSystem());
        addSystem(new Debugger());
        addSystem(new PhysicsSystem());
        addSystem(new LightningSystem());
        addSystem(new TimerSystem());
    }

    public void update(float dt){

        getSpatialHashGrid().calculateSpatialGrid(camera.getCameraTransform());
        ScreenUtils.clear(1, 1, 1, 0.7f);
        batch.setProjectionMatrix(camera.combined);

        for (int i = 0; i < systems.size; i++){
            systems.get(i).startTimer();
            systems.get(i).update(dt);
            systems.get(i).endTimer();
        }



        deleteFlaged();
    }

    public void setCameraBounds(float width, float height){
        camera.viewportHeight = height;
        camera.viewportWidth = width;

        spatialHashGrid.setData((int) height);
    }

    public void buildSystems(){

        //TODO loadingscreen
        assetManager.finishLoading();

        for (int i = 0; i < systems.size; i++){
            systems.get(i).onCreate();
        }

        running = true;
    }

    public void addSystem(System system){
        systems.add(system);
        system.addEngine(this);

        //TODO: sort system by priority and drawable
        //TODO: write comparable interface for that
    }

    public void addEntity(Entity entity){
        entity.setId(entityId++); // and add to component map
        componentMap.put(entity.id, entity);
        spatialHashGrid.addEntity(entity);

        architectHandler.addToArchitect(entity);

        for (int i = 0; i < systems.size; i++){
            systems.get(i).addEntity(entity);
        }


        if (entity.tag != null){
            entityMapper.put(entity.tag, entity);
        }

    }

    public void addCamera(TDCamera camera){
        this.camera = camera;
    }

    public void removeEntity(Entity entity){
        flagedForDelete.add(entity);
        entity.flagForDelete = true;

        if (entity.getComponent(RigidBody2D.class) != null){
            deleteRigidBody(entity);
        }
    }



    public long getSystemFunctionTime(Class<?extends System> System){
        if (getSystem(System) != null)
        return getSystem(System).getFunctionDuration();

        return -1;
    }

    public Component getEntityComponent(int id, Class<?extends Component> component){
        if (componentMap.get(id) != null)
            return componentMap.get(id).getComponent(component);

        return null;
    }

    public Entity getEntity(int id){
        return componentMap.get(id);
    }

    public Entity getEntity(String entityTag){
        return entityMapper.get(entityTag);
    }

    public System getSystem(Class<?extends System> System){

        for (int i = 0; i < systems.size; i++){
            if(systems.get(i).getClass().equals(System))
                return systems.get(i);
        }

        return null;
    }

    public Array<Component> getloadedComponents(Class<?extends Component> c){
        if (threadedParsing){
            for (int i = 0; i < systems.size; i++){
                if (systems.get(i).getClass().equals(ComponentManagerSystem.class))
                    return ((ComponentManagerSystem) systems.get(i)).getLoadedComponents(c);
            }
        }
        return null;
    }

    public SpatialHashGrid getSpatialHashGrid(){
        return spatialHashGrid;
    }

    public Array<Cell> getCellsFromCameraCenter(){
        return getSpatialHashGrid().getNeighbours();
    }

    public Array<Integer> NearbyComponentsFromArc(Byte b){
        Array<Cell> loaded =  getSpatialHashGrid().getNeighbours();
        Array<Integer> ints = new Array<>();
        Array<TransformComponent> transforms = new Array<>();
        for (int i = 0; i < loaded.size; i++){
            transforms.addAll((Array<TransformComponent>) loaded.get(i).getComponents(TransformComponent.class));
        }

        transforms.sort(new TransformComparator());
        for (int j = 0; j < transforms.size; j++){
            TransformComponent t = transforms.get(j);
            ints.add(t.getArchitectID(b));
        }

        return ints;
    }

    public TDCamera getCamera(){
        return camera;
    }

    public Batch getBatch(){
        return batch;
    }

    public void deleteFlaged() {

        //TODO delete flaged enteties once every x frames, before threads start with inacurate data.
        if (!world.isLocked()){
            //Destroy Box2D
            for (Entity e : flagedRigidBodyforDelete){
                RigidBody2D rigidBody2D = (RigidBody2D) e.getComponent(RigidBody2D.class);
                if (rigidBody2D != null && !rigidBody2D.destroyed){
                    rigidBody2D.destroyed = true;
                    world.destroyBody(rigidBody2D.getBody());
                }
            }
            flagedRigidBodyforDelete.clear();
        }

        for (Entity e : flagedForDelete){

            e.dispose();
            componentMap.remove(e.id);
            spatialHashGrid.removeEntity(e);
            architectHandler.removeEntity(e);

        }
        flagedForDelete.clear();
    }

    public void deleteRigidBody(Entity temp) {
        flagedRigidBodyforDelete.add(temp);
    }

    public void dispose() {
        for (int i = 0; i < systems.size; i++){
            systems.get(i).dispose();
        }

        batch.dispose();
        assetManager.dispose();
        world.dispose();
        lightning.dispose();
    }

    public void addAsset(AssetDescriptor<TextureAtlas> asset) {
        assetManager.load(asset);
    }



    //remove from scope
    public int getDrawnEntities(){
        if (getSystem(SpatialRenderer.class) != null){
            SpatialRenderer s = (SpatialRenderer) getSystem(SpatialRenderer.class);
            return s.drawnEntities;
        }

        return -1;
    }

    public int getAnimations(){
        if (getSystem(AnimationSystem.class) != null){
            AnimationSystem s = (AnimationSystem) getSystem(AnimationSystem.class);
            return s.numOfAnimations;
        }

        return -1;
    }

    public int getCollisions(){
        if(getSystem(CollisionDetectionSystem.class) != null){
            CollisionDetectionSystem s = (CollisionDetectionSystem) getSystem(CollisionDetectionSystem.class);

            return s.getNumOfCollisions();
        }

        return -1;
    }

    public int getCollidableObjectsInRange(){
        if(getSystem(CollisionDetectionSystem.class) != null){
            CollisionDetectionSystem s = (CollisionDetectionSystem) getSystem(CollisionDetectionSystem.class);

            return s.getCollidableComponentsDebug();
        }
        return -1;
    }


    public void setDebug(boolean state){
        if(getSystem(Debugger.class) != null){
            Debugger s = (Debugger) getSystem(Debugger.class);
            s.debug = state;
        }
    }

    public void toggleDebug(){
        if(getSystem(Debugger.class) != null){
            Debugger s = (Debugger) getSystem(Debugger.class);
            s.debug = !s.debug;
            s.debugBox2D = false;
        }
    }

    public void toggleDebugBox2D(){
        if(getSystem(Debugger.class) != null){
            Debugger s = (Debugger) getSystem(Debugger.class);
            s.debugBox2D = !s.debugBox2D;
            s.debug = s.debugBox2D;
        }
    }
    //-----------------------
}

package EntityEngine;

import EntityEngine.Architect.Architect;
import EntityEngine.Architect.ArchitectHandler;
import EntityEngine.Components.Component;
import EntityEngine.Components.RigidBody2D;
import EntityEngine.Components.TransformComponent;
import EntityEngine.GameClasses.TDCamera;
import EntityEngine.Renderer.SpatialHashGrid;
import EntityEngine.Systems.*;
import EntityEngine.Systems.System;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.ChainVfxEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*
* This engine composes of an ECS, renderer, assetManager, Collision detection and
* resolution, Box2D, lightning, postprocessing and lightweight TCP packet transmission.
*
* To use this class correctly one should make use of components and systems in order
* to update different aspects of a game. The use of systems is optional,
* one system must exist in order for the game to have logic.
*
* Author: Fardis Nazemroaya Sedeh
*
* */

public class Engine {

    public final HashMap<Integer, Entity> componentMap = new HashMap<>();
    public final HashMap<String, Entity> entityMapper = new HashMap<>();
    private final Array<System> systems = new Array<>();
    private SpatialHashGrid spatialHashGrid;

    int entityId = 0;
    public List<Entity> flagedForDelete = new ArrayList<>();
    public List<Entity> flagedRigidBodyforDelete = new ArrayList<>();

    public String user; // for networking //TODO change this later for more modular implementation

    public AssetManager assetManager;
    public World world;
    public RayHandler lightning;
    public ExecutorService threadPool;
    public TDCamera camera;
    public Batch batch;
    public ArchitectHandler architectHandler;
    public VfxManager vfxManager;
    public Array<ChainVfxEffect> effects;


    boolean reset = false;

    public Engine(float width, float height, Script scriptLoader){
        camera = new TDCamera(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        batch = new SpriteBatch();

        threadPool = Executors.newCachedThreadPool();

        assetManager = new AssetManager();
        world = new World(new Vector2(0, 0f),true);
        lightning = new RayHandler(world);


        architectHandler = new ArchitectHandler();

        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
        effects = new Array<>();
        vfxManager.setBlendingEnabled(true);

        initSetup();


        scriptLoader.addEngine(this);
        scriptLoader.loadAssets();

        spatialHashGrid = new SpatialHashGrid();
        spatialHashGrid.setData((int) camera.viewportHeight);

        scriptLoader.onCreate();

        buildSystems();

    }

    public void initSetup(){
        addSystem(new TileMapRenderer());
        addSystem(new SpatialRenderer());
        addSystem(new NetworkManager());
        addSystem(new AnimationSystem());
        addSystem(new CollisionDetectionSystem());
        addSystem(new Debugger());
        addSystem(new PhysicsSystem());
        addSystem(new LightningSystem());
        addSystem(new UIRenderer());
    }


    public void update(float dt){

        spatialHashGrid.calculateSpatialGrid(camera.getCameraTransform());
        batch.setProjectionMatrix(camera.combined);

        //Updates logic
        for (int i = 0; i < systems.size; i++){

            systems.get(i).startTimer();
            if (systems.get(i).isActive){
                systems.get(i).update(dt);
            }
            systems.get(i).endTimer();
        }

        render(dt);


        deleteFlaged();

        for (int i = 0; i < systems.size; i++){

            if (systems.get(i).isActive){
                systems.get(i).lastUpdate(dt);
            }
        }
    }

    private void render(float dt){
        ScreenUtils.clear(1, 1, 1, 1f);

        vfxManager.cleanUpBuffers();
        vfxManager.beginInputCapture();

        //Everything with its own batch mby change later so everything has own batch or uses engine batch
        for (int i = 0; i < systems.size; i++){

            if (systems.get(i).isActive){
                systems.get(i).preRender(dt);
            }
        }

        //Renders everything with global batch
        batch.begin();
        for (int i = 0; i < systems.size; i++){

            if (systems.get(i).isActive){
                systems.get(i).render(dt);
            }
        }
        batch.end();

        vfxManager.endInputCapture();
        vfxManager.update(dt);
        vfxManager.applyEffects();
        vfxManager.renderToScreen();

        //Post render, rendering with own batch, ex Lights
        for (int i = 0; i < systems.size; i++){

            if (systems.get(i).isActive){
                systems.get(i).postRender(dt);
            }
        }

        //Renders UI
        for (int i = 0; i < systems.size; i++){

            if (systems.get(i).isActive){
                systems.get(i).UIRender(dt);
            }
        }
    }


    public void setCameraBounds(float width, float height){
        camera.viewportHeight = height;
        camera.viewportWidth = width;

        spatialHashGrid.setData((int) height);
    }

    private void buildSystems(){

        //TODO loadingscreen
        assetManager.finishLoading();

        for (int i = 0; i < systems.size; i++){
            systems.get(i).onCreate();
        }

        for (int i = 0; i < systems.size; i++){
            systems.get(i).postCreate();
        }
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
        if (entity.name != null){
            entityMapper.put(entity.name, entity);
        }
        spatialHashGrid.addEntity(entity);
        architectHandler.addToArchitect(entity);

        for (int i = 0; i < systems.size; i++){
            systems.get(i).addEntity(entity);
        }




    }

    public void removeEntity(Entity entity){
        if (entity == null || entity.flagForDelete)
            return;

        flagedForDelete.add(entity);
        entity.flagForDelete = true;

        entityMapper.remove(entity.name);

        if (entity.getComponent(RigidBody2D.class) != null){
            deleteRigidBody(entity);
        }
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

    public Component getEntityComponent(int id, Class<?extends Component> component){
        if (componentMap.get(id) != null)
            return componentMap.get(id).getComponent(component);

        return null;
    }


    public SpatialHashGrid getSpatialHashGrid(){
        return spatialHashGrid;
    }

    /*
    * Returns integer array with the index of the components inrange
    * from the given architect
    * */
    public Array<Integer> getSpatialArc(Architect architect){

        if (architect == null)
            return null;

        Array<TransformComponent> transforms = getSpatialHashGrid().getNearbyTransforms();
        Array<Integer> ints = new Array<>();
        if (transforms == null)
            return null;
        for (int j = 0; j < transforms.size; j++){
            TransformComponent t = transforms.get(j);
            int p = t.getArchitectID(architect.id);
            if (p > -1)
                ints.add(p);
        }

        return ints;
    }

    public TDCamera getCamera(){
        return camera;
    }

    public Batch getBatch(){
        return batch;
    }

    private void deleteFlaged() {

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

            if (e.name != null)
                entityMapper.remove(e.name);

            spatialHashGrid.removeEntity(e);
            architectHandler.removeEntity(e);

            for (int i = 0 ; i < systems.size; i++){
                systems.get(i).removeEntity(e);
            }

        }
        flagedForDelete.clear();


    }

    public void deleteRigidBody(Entity temp) {
        flagedRigidBodyforDelete.add(temp);
    }

    /*
    * This method removes every entity and components from the internal ecs
    * at the end of the frame.
    * Because deletion is not instant, one may use system.lastUpdate for
    * engine use after ecs is cleared.
    * */
    public void resetECS(){

        for (int i = 0; i < entityId; i++){
            removeEntity(componentMap.get(i));

        }
        entityId = 0;
        getSystem(NavMesh.class).reset();

    }

    public void dispose() {
        for (int i = 0; i < systems.size; i++){
            systems.get(i).dispose();
        }

        batch.dispose();
        assetManager.dispose();
        world.dispose();
        lightning.dispose();

        for (int i = 0; i < effects.size; i++){
            effects.get(i).dispose();
        }

        vfxManager.dispose();

    }

    public void addEffect(ChainVfxEffect effect){
        vfxManager.addEffect(effect);
        effects.add(effect);
    }

    public void addAsset(AssetDescriptor<TextureAtlas> asset) {
        assetManager.load(asset);
    }

    public void exit() {
        Gdx.app.exit();
    }
}

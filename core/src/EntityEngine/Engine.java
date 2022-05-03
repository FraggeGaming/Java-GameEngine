package EntityEngine;

import EntityEngine.Components.Component;
import EntityEngine.Components.VelocityComponent;
import EntityEngine.GameClasses.TDCamera;
import EntityEngine.Network.ClientUpdate;
import EntityEngine.Renderer.Cell;
import EntityEngine.Renderer.SpatialHashGrid;
import EntityEngine.Systems.*;
import EntityEngine.Systems.System;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Engine {

    public final HashMap<Integer, Entity> componentMap = new HashMap();
    public final HashMap<String, Entity> entityMapper = new HashMap();
    private final Array<System> systems = new Array<>();
    private final SpatialHashGrid spatialHashGrid;
    public ExecutorService pool;

    public TDCamera camera;
    public Batch batch;
    int entityId = 0;
    public List<Entity> flagedForDelete = new ArrayList<>();
    public String user; // for networking //TODO change this later for more modular implementation
    boolean running = false;

    public Engine(TDCamera camera){
        batch = new SpriteBatch();
        this.camera = camera;
        spatialHashGrid = new SpatialHashGrid();
        spatialHashGrid.setData((int) camera.viewportHeight);
        pool = Executors.newCachedThreadPool();

        initSetup();

    }

    public void initSetup(){
        addSystem(new SpatialRenderer());
        addSystem(new NetworkManager());
        addSystem(new AnimationSystem());
        addSystem(new CollisionDetectionSystem());
        addSystem(new ComponentManagerSystem());
        addSystem(new Debugger());
    }

    public void update(float dt){
        deleteFlaged();
        getSpatialHashGrid().calculateSpatialGrid(camera.getCameraTransform());
        ScreenUtils.clear(1, 1, 1, 0.7f);
        //TODO: render drawable first, when drawable ends, end batch

        //Render batches
        batch.setProjectionMatrix(camera.combined);

        for (int i = 0; i < systems.size; i++){
            systems.get(i).startTimer();
            systems.get(i).update(dt);
            systems.get(i).endTimer();
        }


    }

    public void buildSystems(){
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
        entity.setId(entityId++, componentMap); // and add to component map

        for (System s : systems){
            s.addEntity(entity);
        }

        if (entity.tag != null){
            entityMapper.put(entity.tag, entity);
        }

        spatialHashGrid.addEntity(entity);

    }

    public void addCamera(TDCamera camera){
        this.camera = camera;
    }

    public void removeEntity(Entity entity){
        flagedForDelete.add(entity);
        entity.flagForDelete = true;

    }

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

    public long getSystemFunctionTime(Class<?extends System> System){
        if (getSystem(System) != null)
        return getSystem(System).getFunctionDuration();

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

    public Entity getEntityID(Entity e){
        return componentMap.get(e.id);
    }


    public System getSystem(Class<?extends System> System){
        for (System s : systems){
            if (s.getClass().equals(System))
                return s;
        }

        return null;
    }

    public Array<Component> getloadedComponents(Class<?extends Component> c){
        for (System s : systems){
            if (s.getClass().equals(ComponentManagerSystem.class))
                return ((ComponentManagerSystem) s).getLoadedComponents(c);
        }

        return null;
    }

    public SpatialHashGrid getSpatialHashGrid(){
        return spatialHashGrid;
    }

    public Array<Cell> getCellsFromCameraCenter(){
        return getSpatialHashGrid().getNeighbours();
    }

    /*
    * Override this when creating custom server client
    * */
    public void addNetWorkClientOnUpdate(ClientUpdate client){
        NetworkManager manager = (NetworkManager) getSystem(NetworkManager.class);
        manager.addClientOnUpdate(client);
    }

    public TDCamera getCamera(){
        return camera;
    }

    public Batch getBatch(){
        return batch;
    }


    public void deleteFlaged() {

        //TODO delete flaged enteties once every x frames, before threads start with inacurate data.
        for (Entity e : flagedForDelete){
            spatialHashGrid.removeEntity(e);
            componentMap.remove(e.id);

        }

        flagedForDelete.clear();


    }


    public void dispose() {
        batch.dispose();
    }
}

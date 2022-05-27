package TestFiles.scripts;

import EntityEngine.Components.Component;
import EntityEngine.Components.TextureComponent;
import EntityEngine.Components.TransformComponent;
import TestFiles.scripts.Systems.DebugStats;
import EntityEngine.Script;
import TestFiles.scripts.Systems.*;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class ScriptLoader extends Script {

    @Override
    public void loadAssets() {
        engine.addAsset(new AssetDescriptor<>("atlas/Fire.atlas", TextureAtlas.class));
        engine.addAsset(new AssetDescriptor<>("atlas/TP.atlas", TextureAtlas.class));
        engine.addAsset(new AssetDescriptor<>("atlas/StoneCrab.atlas", TextureAtlas.class));
        engine.addAsset(new AssetDescriptor<>("atlas/LarvMovement.atlas", TextureAtlas.class));
    }

    @Override
    public void onCreate() {
        engine.addSystem(new DebugStats());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new WorldSystem());
        engine.addSystem(new BulletSystem());
        //engine.addSystem(new NetworkScript());
        engine.camera.zoom = 1;

        Array<Class<?extends Component>> types = new Array<>();
        types.add(TransformComponent.class, TextureComponent.class);
        engine.architectHandler.createArchitect(types);


    }

    //TODO Highest frametime, frametime debugging
    //TODO collision detection optimization, flag sertain cells for update or not
    //TODO archtect type dynamic allocator
    //TODO only calculate 1 time everu frame (componentsfromcenter(byte b))
    //TODO engine pool for less deletetion of entities
    //TODO lightning filter
    //TODO audio component
    //TODO different screen support, loading, settings etc
    //TODO navMesh (multithreaded pathfinding) //Navmeshcomponents
    //TODO optimized physics simulation
    //TODO add reversed animation
    //TODO fix so animation gets synced //Synced animation parameter
    //TODO add dispose on stuff

    //TODO create mouse clicking system (ClickableComponent)

    //TODO PP


    //MBY
    //TODO sorting system for systems based on priority order
    //TODO fix large collision //multiple collision boxes
    //TODO add UpNp
    //TODO UDP support
    //TODO some kind of particle system (gpu calculated?)
    //TODO raycast component

}

package TestFiles.scripts;

import EntityEngine.Components.*;
import EntityEngine.Type;
import TestFiles.SideScroller.SideMovement;
import TestFiles.SideScroller.WorldGen;
import TestFiles.scripts.Systems.DebugStats;
import EntityEngine.Script;
import TestFiles.scripts.Systems.*;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class ScriptLoader extends Script {

    @Override
    public void loadAssets() {
        engine.addAsset(new AssetDescriptor<>("atlas/Fire.atlas", TextureAtlas.class));
        engine.addAsset(new AssetDescriptor<>("atlas/TP.atlas", TextureAtlas.class));
        engine.addAsset(new AssetDescriptor<>("atlas/StoneCrab.atlas", TextureAtlas.class));
        engine.addAsset(new AssetDescriptor<>("atlas/LarvMovement.atlas", TextureAtlas.class));

        Type type = new Type(TransformComponent.class, TextureComponent.class);
        engine.architectHandler.createArchitect(type);

        type = new Type(Light.class, TransformComponent.class);
        engine.architectHandler.createArchitect(type);

        type = new Type(AnimationComponent.class, TransformComponent.class, TextureComponent.class);
        engine.architectHandler.createArchitect(type);

    }

    @Override
    public void onCreate() {
        topDownTest();

        //sideScrollerTest();

    }

    public void topDownTest(){
        engine.addSystem(new DebugStats());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new WorldSystem());
        engine.addSystem(new BulletSystem());
        engine.addSystem(new TimerSystem());
        //engine.addSystem(new NetworkScript());
        engine.camera.zoom = 1;
    }
    public void sideScrollerTest(){
        engine.addSystem(new DebugStats());
        engine.addSystem(new BulletSystem());
        engine.addSystem(new TimerSystem());
        engine.addSystem(new WorldGen());
        engine.addSystem(new SideMovement());
    }



    //TODO Highest frametime, frametime debugging
    //TODO collision detection optimization, flag sertain cells for update or not
    //TODO archtect type dynamic allocator
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

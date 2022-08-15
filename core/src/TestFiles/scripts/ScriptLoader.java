package TestFiles.scripts;

import EntityEngine.Components.*;
import EntityEngine.Systems.NavMesh;
import TestFiles.scripts.Shaders.ShaderTest;
import TestFiles.scripts.Systems.StageHandler;
import EntityEngine.Architect.Type;
import TestFiles.SideScroller.SideMovement;
import TestFiles.SideScroller.WorldGen;
import TestFiles.scripts.Components.StoneCrabLogic;
import TestFiles.scripts.Systems.DebugStats;
import EntityEngine.Utils.Script;
import TestFiles.scripts.Systems.*;
import TestFiles.scripts.sim.TileSimManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


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
        Type type = new Type(TransformComponent.class, TextureComponent.class);
        engine.architectHandler.createArchitect(type);

        type = new Type(Light.class, TransformComponent.class);
        engine.architectHandler.createArchitect(type);

        type = new Type(AnimationComponent.class, TransformComponent.class, TextureComponent.class);
        engine.architectHandler.createArchitect(type);

        type = new Type(TextureComponent.class, UIElement.class);
        engine.architectHandler.createArchitect(type);


        type = new Type(StoneCrabLogic.class, TransformComponent.class);
        engine.architectHandler.createArchitect(type);

        Gdx.graphics.setForegroundFPS(0);


        topDownTest();

        //sideScrollerTest();

    }

    public void topDownTest(){
        engine.addSystem(new NavMesh());
        engine.addSystem(new TileSimManager());

        engine.addSystem(new StageHandler());
        engine.addSystem(new DebugStats());

        engine.addSystem(new BulletSystem());
        engine.addSystem(new TimerSystem());

        engine.addSystem(new MovementSystem());
        engine.addSystem(new WorldSystem());
        engine.addSystem(new InputManager());
        engine.addSystem(new CollisionResolver());
        engine.addSystem(new ShaderTest());

        engine.addSystem(new WorldChanger());

        //engine.addSystem(new NetworkScript());
        engine.camera.zoom = 1;
    }
    public void sideScrollerTest(){
        engine.addSystem(new StageHandler());
        engine.addSystem(new DebugStats());

        engine.addSystem(new WorldGen());
        engine.addSystem(new SideMovement());

        engine.addSystem(new BulletSystem());
        engine.addSystem(new TimerSystem());
    }

    //TODO reset tilemap
    //TODO archtect type dynamic allocator

    //TODO Make pathfinding multithreaded
    //TODO audio component
    //TODO add dispose on stuff



    //Gameplay
    //TODO put timer on stonecrab in order to go back into pile
    //TODO create game
    //TODO pool for bullets
    //TODO some kind of particle system using shaders + vfxmanager
    //TODO fix so animation gets synced //Synced animation parameter
    //TODO add reversed animation


    //MBY
    //TODO sorting system for systems based on priority order

    //TODO lightning filter
    //TODO better PP management

    //TODO collision detection optimization, flag sertain cells for update or not
    //TODO optimize actors
    //TODO fix large collision //multiple collision boxes
    //TODO add collision calculation using multiple steps
    //TODO add UpNp
    //TODO UDP support
    //TODO raycast component

}

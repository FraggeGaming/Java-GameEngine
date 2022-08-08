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
import EntityEngine.Script;
import TestFiles.scripts.Systems.*;
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


    //TODO Make pathfinding multithreaded

    //TODO engine pool for less deletetion of entities
    //TODO audio component
    //TODO archtect type dynamic allocator
    //TODO lightning filter


    //TODO add reversed animation
    //TODO fix so animation gets synced //Synced animation parameter
    //TODO add dispose on stuff
    //TODO sorting system for systems based on priority order


    //Gameplay
    //TODO put timer on stonecrab in order to go back into pile
    //TODO create game

    //MBY
    //TODO collision detection optimization, flag sertain cells for update or not
    //TODO optimize actors
    //TODO fix large collision //multiple collision boxes
    //TODO add collision calculation using multiple steps
    //TODO add UpNp
    //TODO UDP support
    //TODO some kind of particle system (gpu calculated?)
    //TODO raycast component

}

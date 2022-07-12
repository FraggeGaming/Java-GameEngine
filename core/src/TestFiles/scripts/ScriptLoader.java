package TestFiles.scripts;

import EntityEngine.Components.*;
import EntityEngine.Components.Node;
import EntityEngine.Type;
import TestFiles.SideScroller.SideMovement;
import TestFiles.SideScroller.WorldGen;
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

        Type type = new Type(TransformComponent.class, TextureComponent.class);
        engine.architectHandler.createArchitect(type);

        type = new Type(Light.class, TransformComponent.class);
        engine.architectHandler.createArchitect(type);

        type = new Type(AnimationComponent.class, TransformComponent.class, TextureComponent.class);
        engine.architectHandler.createArchitect(type);

        type = new Type(Node.class);
        engine.architectHandler.createArchitect(type);

        type = new Type(ActorComponent.class);
        engine.architectHandler.createArchitect(type);

        Gdx.graphics.setForegroundFPS(0);

    }

    @Override
    public void onCreate() {
        topDownTest();

        //sideScrollerTest();

    }

    public void topDownTest(){
        inheritCommon();
        engine.addSystem(new MovementSystem());
        engine.addSystem(new WorldSystem());
        engine.addSystem(new InputManager());
        //engine.addSystem(new NetworkScript());
        engine.camera.zoom = 1;
    }
    public void sideScrollerTest(){
        inheritCommon();
        engine.addSystem(new WorldGen());
        engine.addSystem(new SideMovement());
    }

    public void inheritCommon(){
        engine.addSystem(new DebugStats());
        //engine.addSystem(new BulletSystem());
        engine.addSystem(new TimerSystem());
    }


    //TODO Make pathfinding multithreaded

    //TODO create mouse clicking system (ClickableComponent) Or mby collision / get color of pixel
    //just use actors

    //TODO PP

    //TODO engine pool for less deletetion of entities
    //TODO audio component
    //TODO collision detection optimization, flag sertain cells for update or not
    //TODO archtect type dynamic allocator
    //TODO lightning filter
    //TODO different screen support, loading, settings etc

    //TODO add reversed animation
    //TODO fix so animation gets synced //Synced animation parameter
    //TODO add dispose on stuff





    //TODO sorting system for systems based on priority order

    //TODO create game
    //MBY

    //TODO fix large collision //multiple collision boxes
    //TODO add collision calculation using multiple steps
    //TODO add UpNp
    //TODO UDP support
    //TODO some kind of particle system (gpu calculated?)
    //TODO raycast component

}

package TestFiles.scripts;

import EntityEngine.Script;
import TestFiles.scripts.Network.NetWorkClient;
import TestFiles.scripts.Systems.BulletSystem;
import TestFiles.scripts.Systems.MovementSystem;
import TestFiles.scripts.Systems.UI;
import TestFiles.scripts.Systems.WorldSystem;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ScriptLoader extends Script {

    @Override
    public void loadAssets() {
        engine.addAsset(new AssetDescriptor<>("atlas/Fire.atlas", TextureAtlas.class));
        engine.addAsset(new AssetDescriptor<>("atlas/TP.atlas", TextureAtlas.class));
        engine.addAsset(new AssetDescriptor<>("atlas/StoneCrab.atlas", TextureAtlas.class));
    }

    @Override
    public void onCreate() {
        engine.addSystem(new UI());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new WorldSystem());
        engine.addSystem(new BulletSystem());

        //to stuff with netWork
        NetWorkClient client = new NetWorkClient();
        engine.addNetWorkClientOnUpdate(client);
    }

    //TODO add reversed animation
    //TODO fix large collision //multiple collision boxes
    //TODO fix so animation gets synced //Synced animation parameter
    //TODO audio component
    //TODO add dispose on stuff

    //TODO navMesh (multithreaded pathfinding) //Navmeshcomponents
    //TODO create mouse clicking system (ClickableComponent)
    //TODO Physics system //use box2d
    //TODO raycast component
    //TODO Lightning
    //TODO some kind of particle system (gpu calculated?)

    //TODO PP
    //TODO add UpNp
    //TODO UDP support

    //MBY
    //TODO sorting system for systems based on priority order
}

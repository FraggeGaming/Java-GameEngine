package TestFiles.scripts.Systems;

import EntityEngine.Components.CollisionComponent;
import EntityEngine.Components.TextureComponent;
import EntityEngine.Components.TransformComponent;
import EntityEngine.Components.VelocityComponent;
import EntityEngine.Entity;
import EntityEngine.Systems.NetworkManager;
import EntityEngine.Systems.System;
import TestFiles.scripts.Network.NetWorkClient;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class NetworkScript extends System {
    public NetworkScript(){

    }

    @Override
    public void onCreate() {
        //to stuff with netWork
        NetWorkClient client = new NetWorkClient();
        NetworkManager manager = (NetworkManager) engine.getSystem(NetworkManager.class);
        manager.addClientOnUpdate(client);

        DebugStats debugStats = (DebugStats) engine.getSystem(DebugStats.class);
        debugStats.addNetworkButtons();

        TextureAtlas atlas = engine.assetManager.get("atlas/TP.atlas");

        //Create second player
        Entity player = new Entity();
        player.addComponents(new TextureComponent(new TextureRegion(atlas.findRegion("RedAnt"))));
        player.addComponents(new TransformComponent(engine.camera.viewportWidth / 2, engine.camera.viewportHeight / 2, 5, 30, 30));
        CollisionComponent c = new CollisionComponent(engine.camera.viewportWidth / 2, engine.camera.viewportHeight / 2, 30, 30);
        c.id = "Player2";
        player.addComponents(c);
        player.addComponents(new VelocityComponent());
        player.name = "Player2";
        engine.addEntity(player);
    }
}

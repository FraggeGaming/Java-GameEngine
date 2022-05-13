package TestFiles;

import EntityEngine.Engine;
import EntityEngine.GameClasses.TDCamera;
import TestFiles.scripts.Network.NetWorkClient;
import TestFiles.scripts.Systems.BulletSystem;
import TestFiles.scripts.Systems.MovementSystem;
import TestFiles.scripts.Systems.UI;
import TestFiles.scripts.Systems.WorldSystem;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class DOPVsOOP extends ApplicationAdapter {
	Engine engine;

	@Override
	public void create () {
		float height = 300;
		float width = height*16/9;

		TDCamera camera = new TDCamera(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);


		//engine setup
		engine = new Engine(camera);

		engine.addAsset(new AssetDescriptor<>("atlas/TexturePack.atlas", TextureAtlas.class));
		engine.addAsset(new AssetDescriptor<>("atlas/Fire.atlas", TextureAtlas.class));

		engine.addSystem(new UI());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new WorldSystem());
		engine.addSystem(new BulletSystem());

		//to stuff with netWork
		NetWorkClient client = new NetWorkClient();
		engine.addNetWorkClientOnUpdate(client);

		engine.buildSystems();

		//TODO audio component
		//TODO assetmanager
		//TODO add dispose on stuff
		//TODO collision filter

		//TODO create mouse clicking system (ClickableComponent)
		//TODO raycast component
		//TODO Physics system

		//TODO some kind of particle system (gpu calculated?)
		//TODO navMesh (multithreaded pathfinding)

		//TODO PP
		//TODO add UpNp
		//TODO UDP support

		//MBY
		//TODO sorting system for systems based on priority order


	}


	@Override
	public void render() {
		engine.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose () {
		engine.dispose();
	}


}

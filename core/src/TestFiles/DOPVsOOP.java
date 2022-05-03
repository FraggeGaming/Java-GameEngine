package TestFiles;

import EntityEngine.Engine;
import EntityEngine.GameClasses.TDCamera;
import TestFiles.scripts.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class DOPVsOOP extends ApplicationAdapter {
	SpriteBatch batch;
	Engine engine;

	@Override
	public void create () {
		float height = 300;
		float width = height*16/9;

		TDCamera camera = new TDCamera(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		batch = new SpriteBatch();

		TextureAtlas atlas = new TextureAtlas("atlas/TexturePack.atlas");
		//engine setup
		engine = new Engine(batch, camera);

		engine.addSystem(new UI());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new WorldSystem(atlas));
		engine.addSystem(new BulletSystem(atlas));

		//to stuff with netWork
		NetWorkClient client = new NetWorkClient();
		engine.addNetWorkClientOnUpdate(client);

		//TODO assetmanager

		//TODO collision filter
		//TODO add UpNp
		//TODO UDP support
		//TODO create mouse clicking system
		//TODO raycast component
		//TODO audio component
		//TODO Physics system
		//TODO PP
		//TODO some kind of particle system (gpu calculated?)
		//TODO navMesh (multithreaded pathfinding)

		//MBY
		//TODO sorting system for systems based on priority order

		engine.buildSystems();
	}


	@Override
	public void render() {
		engine.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose () {
		batch.dispose();
	}


}

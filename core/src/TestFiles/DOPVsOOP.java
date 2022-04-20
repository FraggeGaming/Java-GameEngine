package TestFiles;

import EntityEngine.Components.*;
import EntityEngine.Engine;
import EntityEngine.Entity;
import EntityEngine.GameClasses.Animation;
import EntityEngine.GameClasses.TDCamera;
import EntityEngine.Noise.OpenSimplexNoise;
import EntityEngine.Systems.*;
import EntityEngine.Tile;
import TestFiles.scripts.MovementSystem;
import TestFiles.scripts.NetWorkClient;
import TestFiles.scripts.UI;
import TestFiles.scripts.WorldSystem;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class DOPVsOOP extends ApplicationAdapter {
	SpriteBatch batch;
	Engine engine;

	@Override
	public void create () {
		float height = 500;
		float width = height*16/9;

		TDCamera camera = new TDCamera(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		batch = new SpriteBatch();

		//engine setup
		engine = new Engine(batch, camera);

		engine.addSystem(new UI());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new WorldSystem());

		//to stuff with netWork
		NetWorkClient client = new NetWorkClient();
		engine.addNetWorkClientOnUpdate(client);

		//TODO sorting system for systems based on priority order
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

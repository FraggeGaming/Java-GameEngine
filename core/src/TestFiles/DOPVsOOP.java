package TestFiles;

import EntityEngine.Engine;
import EntityEngine.GameClasses.TDCamera;
import TestFiles.scripts.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class DOPVsOOP extends ApplicationAdapter {

	Engine engine;

	@Override
	public void create () {
		float height = 300;
		float width = height*16/9;

		TDCamera camera = new TDCamera(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);


		TextureAtlas atlas = new TextureAtlas("atlas/TexturePack.atlas");
		//engine setup
		engine = new Engine(camera);

		engine.addSystem(new UI());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new WorldSystem(atlas));
		engine.addSystem(new BulletSystem(atlas));

		//to stuff with netWork
		NetWorkClient client = new NetWorkClient();
		engine.addNetWorkClientOnUpdate(client);

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

		engine.buildSystems();
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

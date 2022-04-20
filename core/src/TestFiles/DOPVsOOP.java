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
	TextureAtlas atlas;
	Entity player;
	CollisionDetectionSystem collisionSystem;



	@Override
	public void create () {
		float height = 500;
		float width = height*16/9;

		TDCamera camera = new TDCamera(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		batch = new SpriteBatch();
		atlas = new TextureAtlas("atlas/TexturePack.atlas");
		TextureAtlas fireAtlas = new TextureAtlas("atlas/Fire.atlas");
		//engine setup
		engine = new Engine(batch, camera);

		engine.addSystem(new UI());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new WorldSystem(atlas));
		collisionSystem = (CollisionDetectionSystem) engine.getSystem(CollisionDetectionSystem.class);


		createPlayers(camera);

		for (int i = 0; i < 5; i++){
			for (int j = 0; j < 5; j++)
			createFireAnimationTest(30 * i, 30*j, fireAtlas );
		}


		//to stuff with netWork
		NetWorkClient client = new NetWorkClient();
		engine.addNetWorkClientOnUpdate(client);


	}

	public void createFireAnimationTest(int x, int y, TextureAtlas fireAtlas){
		Entity e = new Entity();
		e.addComponents(new TextureComponent(new TextureRegion(fireAtlas.findRegion("fire", 0))));
		e.addComponents(new TransformComponent(x, y, 1, 120, 120));
		CollisionComponent c = new CollisionComponent(x + 20, y + 20, 40, 40);
		c.id = "Fire";
		c.isStatic = true;
		e.addComponents(c);
		Animation animation = new Animation(fireAtlas, "fire", 109);
		AnimationComponent a = new AnimationComponent(0.02f, true, animation.getFrames());
		a.addAnimation(0.3f, true, animation.getFrames());
		e.addComponents(a);

		engine.addEntity(e);
	}



	public void createPlayers(TDCamera camera){
		engine.user = "Player"; //TODO change this later to less shit way

		player = new Entity();
		player.addComponents(new TextureComponent(new TextureRegion(atlas.findRegion("RedAnt"))));
		player.addComponents(new TransformComponent(camera.viewportWidth / 2, camera.viewportHeight / 2, 5, 30, 30));
		CollisionComponent c = new CollisionComponent(100, 100, 30, 30);
		c.id = "Player2";
		player.addComponents(c);
		player.addComponents(new VelocityComponent());
		player.tag = "Player2";
		engine.addEntity(player);

		player = new Entity();
		player.addComponents(new TextureComponent(new TextureRegion(atlas.findRegion("BlueAnt"))));
		player.addComponents(new TransformComponent(camera.viewportWidth / 2, camera.viewportHeight / 2, 5, 30, 30));
		c = new CollisionComponent(100, 100, 30, 30);
		c.id = "Player";
		player.addComponents(c);
		player.addComponents(new VelocityComponent());
		player.tag = "Player";
		engine.addEntity(player);
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

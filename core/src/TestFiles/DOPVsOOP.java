package TestFiles;

import EntityEngine.Components.*;
import EntityEngine.Engine;
import EntityEngine.Entity;
import EntityEngine.GameClasses.Animation;
import EntityEngine.GameClasses.TDCamera;
import EntityEngine.Network.ClientUpdate;
import EntityEngine.Noise.OpenSimplexNoise;
import EntityEngine.Systems.*;
import EntityEngine.Tile;
import TestFiles.scripts.NetWorkClient;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import java.lang.System;

public class DOPVsOOP extends ApplicationAdapter {
	SpriteBatch batch;

	Engine engine;
	TextureAtlas atlas;
	Entity player;
	CollisionDetectionSystem collisionSystem;
	int tileMapRenderIndexX = 0;
	int tileMapRenderIndexY = 0;
	double scale = 0.04f;
	float z = 1f;
	int mapSize = 500;
	int mapSizeIndex = 0;
	OpenSimplexNoise noise;




	@Override
	public void create () {
		float height = 500;
		float width = height*16/9;

		TDCamera camera = new TDCamera(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		System.out.println(Gdx.graphics.getHeight());
		batch = new SpriteBatch();

		//engine setup
		engine = new Engine(batch, camera);
		engine.addSystem(new SpatialRenderer());
		engine.addSystem(new UI());
		engine.addSystem(new AnimationSystem());
		collisionSystem = new CollisionDetectionSystem();
		engine.addSystem(collisionSystem);
		engine.addSystem(new Debugger());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new ComponentManagerSystem());
		engine.addSystem(new NetworkManager());

		atlas = new TextureAtlas("atlas/TexturePack.atlas");
		TextureAtlas fireAtlas = new TextureAtlas("atlas/Fire.atlas");
		noise = new OpenSimplexNoise();

		//TODO collision filter
		//TODO network

		//TODO create mouse clicking system (perhaps with mouse position? MouseEventComponent? actors? collision component on mouse?)

		//TODO mouseEventSystem
			//get unprojected mouse position
			//find correct cell in loadedCells
			//check every object in the cell that has a onMouseComponent
			//sort or project components on 1d surface for a correct layered design
			//component.playEvent(int id);
			//save component for exit check


			//also check UI?? or use stage with ui?



		//TODO mousecComponent
			//bounds
			//eventsArray

			//playEvent(int id)

		//TODO event
			//onClick
			//onEnter
			//onExit
			//onButtonDown
			//onButtonUp

			//if id == 0
				//onClick = true
			//if id == 1
				//etc...


		//TODO physics component (physics2D system)
			//TODO Physics system (basic) (use on collision event and transform)

		//TODO raycast component
		//TODO audio component

		//TODO some kind of particle system (gpu calculated?)

		//TODO navMesh (multithreaded pathfinding)


		player = new Entity();
		player.addComponents(new TextureComponent(new TextureRegion(atlas.findRegion("BlueAnt"))));
		player.addComponents(new TransformComponent(camera.viewportWidth / 2, camera.viewportHeight / 2, 5, 30, 30));
		CollisionComponent c = new CollisionComponent(100, 100, 30, 30);
		c.id = "Player";
		player.addComponents(c);
		player.addComponents(new VelocityComponent());
		player.tag = "Player";
		engine.addEntity(player);


		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++)
			createFireAnimationTest(30 * i, 30*j, fireAtlas );
		}


		player = new Entity();
		player.addComponents(new TextureComponent(new TextureRegion(atlas.findRegion("RedAnt"))));
		player.addComponents(new TransformComponent(150, 150, 5, 30, 30));
		c = new CollisionComponent(100, 100, 30, 30);
		c.id = "Player2";
		player.addComponents(c);
		player.addComponents(new VelocityComponent());
		player.tag = "Player2";
		engine.addEntity(player);


		NetWorkClient client = new NetWorkClient();
		//to stuff with netGame
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

	public void createTileMap(TextureAtlas atlas){
		Tile t;

		if (mapSizeIndex > mapSize*mapSize)
			return;

		for (int i = 0; i < 200; i++){
			if (tileMapRenderIndexX < mapSize){

				t = new Tile(atlas, noise.eval(tileMapRenderIndexX*scale, tileMapRenderIndexY*scale, z), 15*tileMapRenderIndexX, 15*tileMapRenderIndexY, 0, 15, 15);
				engine.addEntity(t.getEntity());

				tileMapRenderIndexX++;
			}

			else {
				tileMapRenderIndexY++;
				tileMapRenderIndexX = 0;
			}

			mapSizeIndex++;
		}



	}



	@Override
	public void render() {

		createTileMap(atlas);
		ScreenUtils.clear(1, 1, 1, 0.7f);

		engine.update(Gdx.graphics.getDeltaTime());



		//Switch between animations
		/*if (Gdx.input.isKeyPressed(Input.Keys.Q)){
			AnimationComponent a = (AnimationComponent)test.getComponent(AnimationComponent.class);
			a.pickAnimation(1);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.E)){
			AnimationComponent a = (AnimationComponent)test.getComponent(AnimationComponent.class);
			a.pickAnimation(0);
		}*/



		if (collisionSystem.CollisionWithID((CollisionComponent) player.getComponent(CollisionComponent.class), "Fire")){
			//java.lang.System.out.println("fire");
		}

		if (collisionSystem.CollisionWithID((CollisionComponent) player.getComponent(CollisionComponent.class), "Water")){
			//java.lang.System.out.println("water");
		}

	}



	@Override
	public void dispose () {
		batch.dispose();
	}




}

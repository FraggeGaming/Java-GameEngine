package TestFiles;

import EntityEngine.Engine;
import EntityEngine.Script;
import TestFiles.scripts.Network.NetWorkClient;
import TestFiles.scripts.ScriptLoader;
import TestFiles.scripts.Systems.BulletSystem;
import TestFiles.scripts.Systems.MovementSystem;
import TestFiles.scripts.Systems.UI;
import TestFiles.scripts.Systems.WorldSystem;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class DOPVsOOP extends ApplicationAdapter {
	Engine engine;
	float height = 300;
	float width = height*16/9;

	@Override
	public void create () {
		engine = new Engine(width, height, new ScriptLoader());

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

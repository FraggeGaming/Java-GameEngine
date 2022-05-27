package TestFiles;

import EntityEngine.Engine;
import TestFiles.scripts.ScriptLoader;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;


public class DOPVsOOP extends ApplicationAdapter {
	Engine engine;
	float height = 250;
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

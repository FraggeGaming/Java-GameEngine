package TestFiles.scripts;
import EntityEngine.Engine;
import EntityEngine.Systems.System;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import EntityEngine.Debug.DebugStats;

public class UI extends System {
    private Stage stage;
    DebugStats debugStats;

    public UI(){
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void addEngine(Engine engine){
        super.addEngine(engine);
        debugStats = new DebugStats(stage, engine);
    }

    @Override
    public void update(float dt){
        stage.draw();
        stage.act(dt);

        debugStats.render();
    }
}

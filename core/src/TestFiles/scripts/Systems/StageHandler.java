package TestFiles.scripts.Systems;

import EntityEngine.Architect;
import EntityEngine.Components.ActorComponent;
import EntityEngine.Components.Component;
import EntityEngine.Components.TransformComponent;
import EntityEngine.Entity;
import EntityEngine.Systems.System;
import EntityEngine.Type;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class StageHandler extends System {

    //Borde ha alla stages och hålla koll på dom
    public Stage stage;
    public Stage UI;
    ActorComponent actorComponent;
    @Override
    public void onCreate() {
        stage = new Stage(new FitViewport(engine.camera.viewportWidth,engine.camera.viewportHeight,engine.camera));
        UI = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(UI);

    }

    @Override
    public void addEntity(Entity entity) {
        if (entity.getComponent(ActorComponent.class) != null){
            actorComponent = (ActorComponent) entity.getComponent(ActorComponent.class);
            if (actorComponent.isSpatial)
                stage.addActor(actorComponent.actor);

            else
                UI.addActor(actorComponent.actor);
        }
    }

    @Override
    public void postCreate() {

    }

    @Override
    public void update(float dt) {
        stage.act();
        UI.act();
    }

    @Override
    public void render(float dt) {
        //stage.draw();

    }

    @Override
    public void postRender(float dt) {
        UI.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        UI.dispose();
    }
}

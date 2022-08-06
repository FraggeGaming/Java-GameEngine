package EntityEngine.Systems;

import EntityEngine.Architect.Architect;
import EntityEngine.Components.Component;
import EntityEngine.Components.Light;
import EntityEngine.Components.TransformComponent;
import EntityEngine.Architect.Type;
import com.badlogic.gdx.utils.Array;

public class LightningSystem extends System {

    Architect architect;
    @Override
    public void onCreate() {
        architect = engine.architectHandler.getArchitect(new Type(Light.class, TransformComponent.class));
    }

    @Override
    public void postRender(float dt) {
        engine.lightning.render();
    }

    @Override
    public void update(float dt) {
        engine.lightning.setCombinedMatrix(engine.getCamera());
        engine.lightning.update();
    }

}

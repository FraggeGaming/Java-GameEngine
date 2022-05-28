package EntityEngine.Systems;

import EntityEngine.Architect;
import EntityEngine.Components.Component;
import EntityEngine.Components.Light;
import EntityEngine.Components.TransformComponent;
import EntityEngine.Type;
import com.badlogic.gdx.utils.Array;

public class LightningSystem extends System {

    @Override
    public void onCreate() {
    }

    @Override
    public void update(float dt) {

        engine.lightning.setCombinedMatrix(engine.getCamera());

        Architect architect = engine.architectHandler.getArchitect(new Type(Light.class, TransformComponent.class));
        Array<Integer> ints = engine.getSpatialArc(architect);

        Array<Component> lightArray = architect.getComponents(Light.class);


        Light light;
        if (!ints.isEmpty()){
            for (int i = 0; i <ints.size; i++){

                light = (Light) lightArray.get(ints.get(i));
                light.setActiveByHandler(true);
            }
            engine.lightning.updateAndRender();


            for (int i = 0; i <ints.size; i++){
                light = (Light) lightArray.get(ints.get(i));
                light.setActiveByHandler(false);
            }
        }

    }
}

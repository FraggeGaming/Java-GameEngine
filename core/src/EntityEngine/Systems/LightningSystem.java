package EntityEngine.Systems;

import EntityEngine.Architect;
import EntityEngine.Components.Component;
import EntityEngine.Components.Light;
import com.badlogic.gdx.utils.Array;

public class LightningSystem extends System {

    @Override
    public void onCreate() {
    }

    @Override
    public void update(float dt) {

        engine.lightning.setCombinedMatrix(engine.getCamera());

        Array<Integer> ints = engine.NearbyComponentsFromArc((byte) 0x2, false);
        Architect architect = engine.architectHandler.getArchitect((byte) 0x2);
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

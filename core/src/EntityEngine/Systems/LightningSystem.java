package EntityEngine.Systems;

import EntityEngine.Components.Component;
import EntityEngine.Components.Light;
import com.badlogic.gdx.utils.Array;

public class LightningSystem extends System {

    @Override
    public void onCreate() {
        engine.lightning.setAmbientLight(0.2f);


    }

    @Override
    public void update(float dt) {

        engine.lightning.setCombinedMatrix(engine.getCamera());

        Array<Component> lights = engine.getloadedComponents(Light.class);
        Light light;
        if (lights != null){
            for (int i = 0; i <lights.size; i++){
                light = (Light) lights.get(i);
                light.setActiveByHandler(true);
            }
            engine.lightning.updateAndRender();


            for (int i = 0; i <lights.size; i++){
                light = (Light) lights.get(i);
                light.setActiveByHandler(false);
            }
        }

    }
}

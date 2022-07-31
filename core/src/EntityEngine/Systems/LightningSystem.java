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

        engine.lightning.setCombinedMatrix(engine.getCamera());

        renderAll();

    }

    public void renderAll(){
        engine.lightning.updateAndRender();
    }

    private void optimzedRender(){
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

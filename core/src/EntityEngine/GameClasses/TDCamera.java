package EntityEngine.GameClasses;

import EntityEngine.Components.TransformComponent;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class TDCamera extends OrthographicCamera {
    TransformComponent cameraTransform;


    public TDCamera(float width, float height){
        viewportWidth = width;
        viewportHeight = height;
        near = 0;
        cameraTransform  = new TransformComponent(position.x, position.y, 0, 0, 0);

        super.update();

    }
    @Override
    public void update() {
        super.update();

        cameraTransform.setVec(position);

    }

    public TransformComponent getCameraTransform() {
        return cameraTransform;
    }
}

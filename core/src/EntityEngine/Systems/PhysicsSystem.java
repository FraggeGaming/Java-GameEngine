package EntityEngine.Systems;

import com.badlogic.gdx.Gdx;

public class PhysicsSystem extends System {
    @Override
    public void update(float dt) {
        engine.world.step(Math.min(Gdx.graphics.getDeltaTime(), 1/60f), 6, 2);
    }
}

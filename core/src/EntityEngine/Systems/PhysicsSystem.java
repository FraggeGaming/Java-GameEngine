package EntityEngine.Systems;

public class PhysicsSystem extends System {
    @Override
    public void update(float dt) {
        engine.world.step(1f/60f, 6, 2);
    }
}

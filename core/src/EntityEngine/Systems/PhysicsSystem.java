package EntityEngine.Systems;

import EntityEngine.Components.*;
import EntityEngine.Engine;
import EntityEngine.Entity;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class PhysicsSystem extends System{


    TransformComponent t;
    CollisionComponent c;
    VelocityComponent v;

    Box2DDebugRenderer debugRenderer;
    Debugger s;

    public PhysicsSystem(){

    }

    @Override
    public void onCreate() {

        s = (Debugger) engine.getSystem(Debugger.class);
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void update(float dt) {

        engine.world.step(1f/144f, 6, 2);


        if (s.debug)
            debugRenderer.render(engine.world, engine.camera.combined);
    }

    public void addVelocity(Entity e, VelocityComponent v){
        if (e == null)
            return;
        t = (TransformComponent) e.getComponent( TransformComponent.class);
        c = (CollisionComponent) e.getComponent(CollisionComponent.class);


        t.addVelocity(v);
        c.followTransform(t);


    }

    public void addVelocity(Entity e){

        if (e == null)
            return;
        t = (TransformComponent) e.getComponent( TransformComponent.class);
        c = (CollisionComponent) e.getComponent(CollisionComponent.class);
        v = (VelocityComponent) e.getComponent(VelocityComponent.class);

        t.addVelocity(v);
        c.followTransform(t);
    }
}

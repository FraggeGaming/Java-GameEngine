package TestFiles.scripts;

import EntityEngine.Components.CollisionComponent;
import EntityEngine.Components.TextureComponent;
import EntityEngine.Components.TransformComponent;
import EntityEngine.Components.VelocityComponent;
import EntityEngine.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Projectile {

    Entity e;
    public Projectile(float life, float speed, TransformComponent position, float width, float height, TextureRegion textureRegion, Vector2 bulletVector){


        e = new Entity();

        e.addComponents(new TextureComponent(textureRegion));
        e.addComponents(new TransformComponent(position.getX(), position.getY(), 5, width, height));
        e.addComponents(new CollisionComponent(position.getX(), position.getY(),width, height));
        e.addComponents(new VelocityComponent(bulletVector.x * speed, bulletVector.y * speed, 1));
        e.addComponents(new LifeCount(life));
        e.addComponents(new BulletComponent());
    }

    public Entity getProjectile(){
        return e;
    }
}

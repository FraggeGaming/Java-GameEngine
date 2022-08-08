package TestFiles.scripts.Systems;

import EntityEngine.Components.AnimationComponent;
import EntityEngine.Components.CollisionComponent;
import EntityEngine.Components.TextureComponent;
import EntityEngine.Entity;
import EntityEngine.Systems.CollisionDetectionSystem;
import EntityEngine.Systems.System;
import TestFiles.scripts.Components.StoneCrabLogic;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class CollisionResolver extends System {
    CollisionDetectionSystem collisions;
    TextureAtlas sC;

    @Override
    public void onCreate() {
        sC = engine.assetManager.get("atlas/StoneCrab.atlas");
        collisions = (CollisionDetectionSystem) engine.getSystem(CollisionDetectionSystem.class);
    }

    @Override
    public void update(float dt) {
        stoneCrabJump();

    }

    private void stoneCrabJump(){
        Entity player = engine.getEntity("Player");

        if (player == null)
            return;

        CollisionComponent collisionComponent = (CollisionComponent) player.getComponent(CollisionComponent.class);

        Array<CollisionComponent> comp = collisions.getCollisision(collisionComponent, "StoneCrab");
        for (int i = 0; i < comp.size; i++){
            Entity sCrab = engine.getEntity(comp.get(i).getId());

            AnimationComponent a = (AnimationComponent) sCrab.getComponent(AnimationComponent.class);
            StoneCrabLogic logic = (StoneCrabLogic) sCrab.getComponent(StoneCrabLogic.class);

            if (!logic.isScared){
                a.setAlive(true);
                logic.setScared(true);
            }

            if (!a.isAlive() && logic.isScared){
                TextureComponent t = (TextureComponent) sCrab.getComponent(TextureComponent.class);
                t.setTexture(new TextureRegion(sC.findRegion("Stonepile", 12)));
            }

            //Put timer on stonecrab in order to go back into pile after x amount of time

            /*if (Gdx.input.isKeyJustPressed(Input.Keys.Q)){
                logic.setScared(false);

                TextureComponent t = (TextureComponent) sCrab.getComponent(TextureComponent.class);
                t.setTexture(new TextureRegion(sC.findRegion("Stonepile")));

            }*/
        }
    }
}

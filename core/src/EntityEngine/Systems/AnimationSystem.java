package EntityEngine.Systems;

import EntityEngine.Components.*;
import EntityEngine.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AnimationSystem extends System{
    AnimationComponent a;
    TextureComponent t;
    Array<Component> comp;

    public int numOfAnimations = 0;
    public AnimationSystem(){

    }

    @Override
    public void update(float dt) {
        numOfAnimations = 0;


        comp = engine.getloadedComponents(AnimationComponent.class);
        if (comp != null){
            for (int i = 0; i < comp.size; i++){
                setUpAnimation(comp.get(i).getId());
            }
        }

    }


    private void setUpAnimation(int componentID){
        a = (AnimationComponent) engine.getEntityComponent(componentID, AnimationComponent.class);
        if (a != null && a.isAlive()){

            if (a.getFrameSpeed() <= 0){

                //swap its texture component with next animation
                t = (TextureComponent) engine.getEntityComponent(componentID, TextureComponent.class);
                t.setTexture(getNextFrame(a));
            }

            a.decreaseTimer(Gdx.graphics.getDeltaTime());

            numOfAnimations++;
        }
    }

    private TextureRegion getNextFrame(AnimationComponent a){
        a.resetFrameTime();
        a.incrementCurrentFrame();
        if (a.getCurrentFrameNumber() >= a.animationSize()){
            if (!a.isRepeat()){
                a.setAlive(false);

                if (a.killAfterAnimate){
                    //TODO maby fix this and put as parameter if user wants to delete animation after played
                    Entity e = engine.getEntity(a.getId());
                    engine.removeEntity(e);
                }

            }

            a.setCurrentFrameNumber(0);
        }

        return a.getCurrentFrame();
    }
}

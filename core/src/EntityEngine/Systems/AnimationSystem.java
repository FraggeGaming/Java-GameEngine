package EntityEngine.Systems;

import EntityEngine.Architect.Architect;
import EntityEngine.Components.*;
import EntityEngine.Entity;
import EntityEngine.Architect.Type;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AnimationSystem extends System{
    AnimationComponent a;
    TextureComponent t;

    public int numOfAnimations = 0;
    public AnimationSystem(){

    }

    @Override
    public void update(float dt) {
        numOfAnimations = 0;

        Architect architect = engine.architectHandler.getArchitect(new Type(TextureComponent.class, AnimationComponent.class, TransformComponent.class));
        Array<Integer> ints = engine.getSpatialArc(architect);

        if (architect == null)
            return;

        Array<Component> animationArray = architect.getComponents(AnimationComponent.class);
        Array<Component> textureArray = architect.getComponents(TextureComponent.class);

        if (ints != null && !ints.isEmpty()){
            for (int i = 0; i < ints.size; i++){
                a = (AnimationComponent) animationArray.get(ints.get(i));
                if (a != null && a.isAlive()){

                    if (a.getFrameSpeed() <= 0){

                        //swap its texture component with next animation
                        t = (TextureComponent) textureArray.get(ints.get(i));
                        t.setTexture(getNextFrame(a));
                    }

                    a.decreaseTimer(Gdx.graphics.getDeltaTime());

                    numOfAnimations++;
                }
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

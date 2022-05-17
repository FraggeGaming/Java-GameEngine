package EntityEngine.Components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class AnimationComponent extends Component{

    public boolean alive;
    public boolean killAfterAnimate = false;
    public List<AnimationStruct> animations = new ArrayList<>();
    int animationID = 0;
    //add support for multiple different animations
    public AnimationComponent(float frameSpeed, boolean repeat, List<TextureRegion> frames){
        alive = true;
        seperate = true;
        animations.add(new AnimationStruct(frameSpeed, repeat, frames));
    }

    public AnimationComponent(float frameSpeed, boolean repeat, List<TextureRegion> frames, boolean killAfterAnimate){
        alive = true;
        seperate = true;
        this.killAfterAnimate = killAfterAnimate;
        animations.add(new AnimationStruct(frameSpeed, repeat, frames));
    }

    public void addAnimation(float frameSpeed, boolean repeat, List<TextureRegion> frames){
        animations.add(new AnimationStruct(frameSpeed, repeat, frames));
    }

    public float getFrameSpeed(){
        return animations.get(animationID).frameSpeed;
    }

    public void decreaseTimer(float time){
        animations.get(animationID).frameSpeed -= time;
    }

    public TextureRegion getCurrentFrame(){
        return animations.get(animationID).frames.get(animations.get(animationID).currentFrame);
    }

    public void setAlive(boolean bool){
        alive = bool;
    }

    public boolean isAlive(){
        return alive;
    }

    public boolean isRepeat(){
        return animations.get(animationID).repeat;
    }

    public int animationSize(){
        return animations.get(animationID).frames.size();
    }

    public void resetFrameTime(){
        animations.get(animationID).frameSpeed = animations.get(animationID).originalFrameSpeed;
    }

    public void incrementCurrentFrame(){
        animations.get(animationID).currentFrame++;
    }

    public void pickAnimation(int animationID){
        this.animationID = animationID;
    }

    public int getCurrentFrameNumber(){
        return animations.get(animationID).currentFrame;
    }

    public void setCurrentFrameNumber(int i){
        animations.get(animationID).currentFrame = i;
    }
}


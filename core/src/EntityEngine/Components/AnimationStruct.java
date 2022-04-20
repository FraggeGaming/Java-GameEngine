package EntityEngine.Components;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

class AnimationStruct {
    public int currentFrame = 0;
    public List<TextureRegion> frames;
    public float frameSpeed;
    public float originalFrameSpeed;
    public boolean repeat;

    public AnimationStruct(float frameSpeed, boolean repeat, List<TextureRegion> frames) {
        this.repeat = repeat;
        this.frames = frames;

        originalFrameSpeed = frameSpeed;
        this.frameSpeed = 0;
    }
}

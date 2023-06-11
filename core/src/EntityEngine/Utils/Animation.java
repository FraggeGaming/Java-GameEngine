package EntityEngine.Utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class Animation {
    List<TextureRegion> frames = new ArrayList<>();
    public Animation(TextureAtlas atlas, String name, int numOfFrames){
        for (int i = 0; i < numOfFrames; i++){
            frames.add(new TextureRegion(atlas.findRegion(name, i)));
        }
    }

    public  List<TextureRegion> getFrames(){
        return frames;
    }
}

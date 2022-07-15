package EntityEngine.Components;

import EntityEngine.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureComponent extends Component{
    TextureRegion texture;

    public TextureComponent(TextureRegion texture){
        this.texture = texture;
        seperate = true;
    }

    public TextureRegion getRegion() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

}

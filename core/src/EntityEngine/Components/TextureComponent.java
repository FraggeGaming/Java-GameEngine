package EntityEngine.Components;

import EntityEngine.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureComponent extends Component{
    TextureRegion texture;
    public int renderTurn = 0; // 0 or 1

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

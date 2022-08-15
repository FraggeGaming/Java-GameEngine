package TestFiles.scripts.sim;

import EntityEngine.Components.Component;
import com.badlogic.gdx.utils.Array;

public class TileSim extends Component {
    public float x,y;
    public boolean blocked = false;
    public Array<Element> elements;


    public TileSim(float x, float y){
        this.x = x;
        this.y= y;


        elements = new Array<>();
    }

    public void clearElements(){
        elements.clear();
    }


}

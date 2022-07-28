package EntityEngine.Components;

import com.badlogic.gdx.math.Vector2;

public class Node extends Component{

    public boolean isBlocked =  false;

    public boolean debug = false;
    //Distance from startingNode
    int gCost;
    //Distance from endNode
    int hCost;
    //gCost + hCost
    int fCost;

    Vector2 pos;

    public Node parentNode;

    public void setParentNode(Node node){
        parentNode = node;
    }

    public Node getParentNode(){
        return parentNode;
    }

    public int getgCost() {
        return gCost;
    }

    public int gethCost() {
        return hCost;
    }

    public void sethCost(int hCost) {
        this.hCost = hCost;
    }

    public int getfCost() {
        return gCost + hCost;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public void setgCost(int gCost) {
        this.gCost = gCost;
    }
}


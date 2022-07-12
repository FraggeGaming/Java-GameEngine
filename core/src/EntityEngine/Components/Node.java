package EntityEngine.Components;

import com.badlogic.gdx.math.Vector2;

public class Node extends Component{
    public boolean isMovable;
    //Distance from startingNode
    int gCost;
    //Distance from endNode
    int hCost;
    //gCost + hCost
    int fCost;
    Vector2 pos;
    public Node parentNode;
    public int nodeSize;

    public Node(int nodeSize){
        this.nodeSize = nodeSize;
    }
    
    
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

    public void setfCost(int fCost) {
        this.fCost = fCost;
    }

    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getIndex(){
        return new Vector2(pos.x/nodeSize, pos.y/nodeSize);
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public void setgCost(int gCost) {
        this.gCost = gCost;
    }
}


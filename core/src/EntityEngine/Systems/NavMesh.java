package EntityEngine.Systems;

import EntityEngine.Architect;
import EntityEngine.Components.ActorComponent;
import EntityEngine.Components.Component;
import EntityEngine.Components.TransformComponent;
import EntityEngine.Entity;
import EntityEngine.GameClasses.AstarPathFinding;
import EntityEngine.Components.Node;
import EntityEngine.Type;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class NavMesh extends System{
    List<List<Node>> nodeMap;
    AstarPathFinding pathFinding;
    int nodeSize = -1;

    public void setNodeSize(int nodeSize){
        this.nodeSize = nodeSize;
    }

    public NavMesh(){
        nodeMap = new ArrayList<>();
    }

    @Override
    public void postCreate() {

        nodeExeption();
        pathFinding = new AstarPathFinding(nodeMap, nodeSize);
    }

    @Override
    public void addEntity(Entity entity) {

        nodeExeption();


        if (entity.getComponent(Node.class) != null){
            Node node = (Node) entity.getComponent(Node.class);

            while (nodeMap.size() <= node.getPos().x/nodeSize){
                nodeMap.add(new ArrayList<Node>());
            }
            nodeMap.get((int) node.getPos().x/nodeSize ).add((int) node.getPos().y/nodeSize, node);
        }
    }

    public void testPath(){
        Entity p = engine.getEntity("Player");
        TransformComponent component = (TransformComponent) p.getComponent(TransformComponent.class);

        Node start = getNode(0,0);
        Node end = nodeMap.get((int) (component.getX()/16)). get((int) (component.getY()/16));
        pathFinding.pathFindTo(start, end);

        Stack<Node> path = pathFinding.getPath();

        while (!path.isEmpty()){
            Node node = path.pop();
            ActorComponent actorComponent = (ActorComponent) engine.getEntityComponent(node.getId(), ActorComponent.class);
            actorComponent.actor.setDebug(true);
        }
    }

    private void nodeExeption(){
        if (nodeSize == -1){
            try {
                throw new Exception("Node size is -1. Set node size to > 0");
            } catch (Exception e) {
                e.printStackTrace();
                engine.exit();
            }
        }
    }


    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
            testPath();
        }
    }

    public Node getNode(int x, int y){
        return nodeMap.get(x).get(y);
    }

    public void setNodeBlocked(int indexX, int indexY){
        blockNode(getNode(indexX,indexY));
    }

    public void setNodeBlocked(float x, float y){
        blockNode(getNode((int)x/nodeSize,(int)y/nodeSize));
    }

    public void freeNode(float x, float y){
        freeNode(getNode((int)x/nodeSize,(int)y/nodeSize));
    }

    private void blockNode(Node node){
        node.isBlocked = true;
    }

    private void freeNode(Node node){
        node.isBlocked = false;
    }

}

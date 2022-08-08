package EntityEngine.Systems;


import EntityEngine.Components.TransformComponent;
import EntityEngine.Entity;
import EntityEngine.GameClasses.AstarPathFinding;
import EntityEngine.Components.Node;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class NavMesh extends System{
    List<List<Node>> nodeMap;
    AstarPathFinding pathFinding;
    int nodeSize;

    Array<Node> activeNodes = new Array<>();

    public void setNodeSize(int nodeSize){
        this.nodeSize = nodeSize;
    }

    public NavMesh(){
        nodeMap = new ArrayList<>();
    }

    @Override
    public void reset() {
        nodeMap.clear();
    }

    @Override
    public void postCreate() {
        try {
            nodeExeption();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pathFinding = new AstarPathFinding(nodeMap, nodeSize);
    }

    @Override
    public void addEntity(Entity entity) {

        try {
            nodeExeption();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (entity.getComponent(Node.class) != null){
            Node node = (Node) entity.getComponent(Node.class);

            while (nodeMap.size() <= node.getPos().x/nodeSize){
                nodeMap.add(new ArrayList<Node>());
            }
            nodeMap.get((int) node.getPos().x/nodeSize ).add((int) node.getPos().y/nodeSize, node);
        }
    }

    public void testPath(){

        for (int i = 0; i < activeNodes.size; i++){
            activeNodes.get(i).debug = false;
        }

        Entity p = engine.getEntity("Player");
        TransformComponent component = (TransformComponent) p.getComponent(TransformComponent.class);

        Node start = getNode(0,0);
        Node end = nodeMap.get((int) (component.getOriginX()/16)). get((int) (component.getOriginY()/16));
        pathFinding.pathFindTo(start, end);

        Stack<Node> path = pathFinding.getPath();

        while (path != null && !path.isEmpty()){
            Node node = path.pop();
            node.debug = true;
            activeNodes.add(node);
        }
    }

    private void nodeExeption() throws Exception{
        if (nodeSize < 1){
            throw new Exception("Node size is: " + nodeSize + ". Set node size to > 0");
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
        //Block multiple nodes that overlap with width and height

        Array<Integer> verticies = getVerticies(x, y);

        for (int i = 0; i < verticies.size; i+=2){
            blockNode(getNode((verticies.get(i)), (verticies.get(i+1))));
        }

        //blockNode(getNode((int)x/nodeSize,(int)y/nodeSize));
    }

    private Array<Integer> getVerticies(float x, float y){

        Array<Integer> verticies = new Array<>(7);
        verticies.add(0, 0, 0, 0);
        verticies.add(0, 0, 0, 0);
        verticies.set(0, (int)x/nodeSize);
        verticies.set(1, (int)y/nodeSize);


        verticies.set(2, (int)x/nodeSize);
        verticies.set(3, (int)(y + nodeSize-2)/nodeSize);

        verticies.set(4, (int)(x + nodeSize-2)/nodeSize);
        verticies.set(5, (int)(y + nodeSize-2)/nodeSize);

        verticies.set(6, (int)(x + nodeSize-2)/nodeSize);
        verticies.set(7, (int)y/nodeSize);

        return verticies;

    }

    public void freeNode(float x, float y){

        Array<Integer> verticies = getVerticies(x, y);

        for (int i = 0; i < verticies.size; i+=2){
            freeNode(getNode((verticies.get(i)), (verticies.get(i+1))));
        }
        //freeNode(getNode((int)x/nodeSize,(int)y/nodeSize));
    }

    private void blockNode(Node node){
        node.isBlocked = true;
    }

    private void freeNode(Node node){
        node.isBlocked = false;
    }

}

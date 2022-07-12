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

    @Override
    public void postCreate() {
        nodeMap = new ArrayList<>();
        Architect architect = engine.architectHandler.getArchitect(new Type(Node.class));

        Array<Component> nodes = architect.getComponents(Node.class);

        Node node;
        for (int i = 0; i < nodes.size; i++){
            node = (Node) nodes.get(i);

            while (nodeMap.size() <= node.getIndex().x){
                nodeMap.add(new ArrayList<Node>());
            }
            nodeMap.get((int) node.getIndex().x ).add((int) node.getIndex().y, node);
        }

        pathFinding = new AstarPathFinding(nodeMap);
    }



    public void testPath(){
        Entity p = engine.getEntity("Player");
        TransformComponent component = (TransformComponent) p.getComponent(TransformComponent.class);

        Node start = nodeMap.get(0).get(0);
        Node end = nodeMap.get((int) (component.getX()/16)). get((int) (component.getY()/16));
        pathFinding.pathFindTo(start, end);

        Stack<Node> path = pathFinding.getPath();

        while (!path.isEmpty()){
            Node node = path.pop();
            ActorComponent actorComponent = (ActorComponent) engine.getEntityComponent(node.getId(), ActorComponent.class);
            actorComponent.actor.setDebug(true);
        }
    }


    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
            testPath();
        }
    }

}

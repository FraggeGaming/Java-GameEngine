package EntityEngine.Utils;

import EntityEngine.Components.Node;

import java.util.*;

public class AstarPathFinding {

    PriorityQueue<Node> open;
    HashSet<Node> closed;
    List<Node> neighbours;
    List<Node> tempNeighbours;

    List<List<Node>> nodeMap;

    Node currentNode;

    Stack<Node> path;
    Node startNode;
    Node targetNode;

    int nodeSize;

    public AstarPathFinding(List<List<Node>> nodeMap, int nodeSize){
        this.nodeMap = nodeMap;
        this.nodeSize = nodeSize;
    }

    public void pathFindTo(Node start, Node target){
        closed = new HashSet<>();

        open = new PriorityQueue<>(new CompareNode());

        this.targetNode = target;
        this.startNode = start;


        open.add(startNode);

        while(open.size() > 0){

            currentNode = open.remove();

            closed.add(currentNode);

            if (currentNode.getPos().equals(targetNode.getPos())){
                getPath(startNode, targetNode);
                return;
            }
            neighbours = createNeighbours(currentNode);

            for (Node node : neighbours){

                if (!closed.contains(node)){
                    int newMovementNeighbourCost = currentNode.getgCost() + getDistance(currentNode, node);

                    if (newMovementNeighbourCost < node.getgCost() || !open.contains(node)){
                        node.setgCost(newMovementNeighbourCost);
                        node.sethCost(getDistance(node,targetNode));
                        node.setParentNode(currentNode);

                        if (!open.contains(node)){
                            open.add(node);
                        }

                        else{
                            open.remove(node);
                            open.add(node);
                        }

                    }
                }

            }
         }

    }

    private void getPath(Node start, Node end){
        path = new Stack<>();
        Node currentNode = end;

        while (currentNode != start){
            path.push(currentNode);
            currentNode = currentNode.getParentNode();
        }

    }
    public Stack<Node> getPath(){
        return path;
    }

    private int getDistance(Node nodeA, Node nodeB){
        int distanceX = (int) Math.abs(nodeA.getPos().x - nodeB.getPos().x);
        int distanceY = (int) Math.abs(nodeA.getPos().y - nodeB.getPos().y);

        if (distanceX > distanceY){
            return 14*distanceY + 10*(distanceX - distanceY);
        }

        return 14*distanceX + 10*(distanceY - distanceX);

    }

    public List<Node> createNeighbours(Node node){
        tempNeighbours = new ArrayList<>();

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (node.getPos().x/nodeSize -1 + j >= 0 && node.getPos().y/nodeSize -1 + i >= 0){
                    if (i == 1 && j == 1)
                        continue;


                    if (node.getPos().x/nodeSize -1 + j < nodeMap.size() && node.getPos().y/nodeSize -1 + i < nodeMap.get((int) (node.getPos().x/nodeSize -1 + j)).size()){
                        Node node1 = nodeMap.get((int) (node.getPos().x/nodeSize -1 + j)).get((int) (node.getPos().y/nodeSize -1 + i));
                        if (node1 != null){

                            if (!node1.isBlocked)
                                tempNeighbours.add(node1);


                        }


                    }
                }
            }
        }

        return tempNeighbours;
    }
}

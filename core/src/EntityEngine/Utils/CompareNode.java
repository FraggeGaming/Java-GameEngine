package EntityEngine.Utils;

import EntityEngine.Components.Node;

import java.util.Comparator;

public class CompareNode implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) throws NullPointerException {

        /* If o2 har priority over o1, return 1
           assigning o1 as GREATER than o2. */

        if ( o1.getfCost() > o2.getfCost() || o1.getfCost() == o2.getfCost() && o1.gethCost() > o2.gethCost()) {
            return 1;

        }
        /* If o1 and o2 has equal priority, return 0. */

        else if ( o1.getfCost() == o2.getfCost() ) {
            return 0;
        }

        /* If o1 has priority over o2, return -1
           assigning o1 as LESS than o2. */
        else return -1;
    }


}

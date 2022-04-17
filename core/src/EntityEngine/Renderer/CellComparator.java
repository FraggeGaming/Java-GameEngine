package EntityEngine.Renderer;

import java.util.Comparator;

public class CellComparator implements Comparator<Cell>{
    @Override
    public int compare(Cell c1, Cell c2) {
        return Float.compare(c1.order, c2.order);
    }

}

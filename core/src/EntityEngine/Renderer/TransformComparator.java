package EntityEngine.Renderer;

import EntityEngine.Components.TransformComponent;

import java.util.Comparator;

public class TransformComparator implements Comparator<TransformComponent> {


    @Override
    public int compare(TransformComponent o1, TransformComponent o2) {
        return Float.compare(o1.getZ(), o2.getZ());
    }
}

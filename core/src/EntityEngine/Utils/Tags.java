package EntityEngine.Utils;
import java.util.HashSet;

public class Tags {
    private final HashSet<String> tags;
    public Tags(){
        tags = new HashSet<>();
    }

    public void add(String tag){
        tags.add(tag);
    }

    public boolean has(String tag){
        return tags.contains(tag);
    }

    public void remove(String tag){
        tags.remove(tag);
    }

    @Override
    public String toString() {
        return "Tags{" +
                "tags=" + tags +
                '}';
    }
}

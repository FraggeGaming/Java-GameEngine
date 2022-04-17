package EntityEngine.Components;

import EntityEngine.Entity;

public class Component {
    private int id;
    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }
}

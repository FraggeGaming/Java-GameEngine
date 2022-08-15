package EntityEngine.Components;

import java.util.HashMap;

public class Component {
    private int id;
    public boolean seperate = false;
    //On architect x, id is y
    public HashMap<Byte, Integer> architectMapper = new HashMap<>();
    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void dispose(){

    }

    public void setArchitectMapper(byte b, int i){
        architectMapper.put(b, i);
    }

    public void updateArchitectId(byte b, int i){
        architectMapper.remove(b);
        setArchitectMapper(b, i);
    }

    public int getArchitectID(byte b){

        if (architectMapper.containsKey(b))
            return architectMapper.get(b);

        else return -1;
    }

}

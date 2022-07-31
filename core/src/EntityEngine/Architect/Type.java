package EntityEngine.Architect;

import EntityEngine.Components.Component;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;

public class Type {
    Array<Class<?extends Component>> types = new Array<>();

    public Type(Class<?extends Component> type){
        types.add(type);
    }

    public Type(Class<?extends Component> type, Class<?extends Component> type1){
        types.add(type, type1);
    }

    public Type(Class<?extends Component> type, Class<?extends Component> type1,Class<?extends Component> type2 ){
        types.add(type, type1, type2);
    }

    public Type(Class<?extends Component> type, Class<?extends Component> type1,Class<?extends Component> type2,Class<?extends Component> type3 ){
        types.add(type, type1, type2, type3);
    }

    public Type(Array<Class<?extends Component>> types){
        types.addAll(types);
    }

    public int getSize(){
        return types.size;
    }

    public Class<?extends Component> getType(int i){
        return types.get(i);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Type)) return false;
        Type type = (Type) o;

        //TODO type equal code
        for (int i = 0; i < getSize(); i++){
            if (!type.types.contains(getType(i), true)){
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(types);
    }
}

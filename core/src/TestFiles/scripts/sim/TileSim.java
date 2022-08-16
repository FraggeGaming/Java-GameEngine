package TestFiles.scripts.sim;

import EntityEngine.Components.Component;
import com.badlogic.gdx.utils.Array;

public class TileSim extends Component {
    public float x,y;
    public boolean blocked = false;
    private Array<Element> elements;

    float mass = 0;
    float volume = 0;


    public TileSim(float x, float y){
        this.x = x;
        this.y= y;


        elements = new Array<>();
    }

    public float getMass() {
        return mass;
    }

    public void addElement(Element element){
        if (element == null)
            return;
        elements.add(element);
        mass += element.mass;
        volume += element.volume;
    }

    public void calculateMass(){
        mass = 0;
        for (int i = 0; i < elements.size; i++){
            mass += elements.get(i).mass;
        }
    }

    public Element popElement(int atomicNum, ElementState state){

        Element element = new Element(0, 0, atomicNum, state);

        for (int i = 0; i < elements.size; i++){
            if (elements.get(i).equals(element)){

                Element e = elements.removeIndex(i);
                mass -= e.mass;
                volume -= e.volume;
                return e;
            }
        }

        return null;




    }

    public Element popElement(Element element){

        return popElement(element.atomicNum, element.state);

    }

    public Element popRandomElement(){
        if (elements.isEmpty())
            return null;

        return popElement(elements.get(0).atomicNum, elements.get(0).state);
    }

    private Element getRandomElement(){
        return elements.get(getRandomInteger(elements.size));
    }

    public void CalculateVolume(){
        volume = 0;
        for (int i = 0; i < elements.size; i++){
            volume += elements.get(i).volume;
        }
    }

    public void clearElements(){
        elements.clear();
        volume = 0;
        mass = 0;
    }

    private int getRandomInteger(int bound){

        return 0;

    }


}

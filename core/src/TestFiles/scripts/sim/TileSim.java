package TestFiles.scripts.sim;

import EntityEngine.Components.Component;
import com.badlogic.gdx.Gdx;

import java.util.Random;

public class TileSim extends Component {
    public float x,y;
    public boolean blocked = false;
    public final Layer air;
    private final Layer ground;
    public double heat = 0;


    public TileSim(float x, float y){
        this.x = x;
        this.y= y;
        ground = new Layer();
        air = new Layer();
        ground.setAcceptedStates(ElementState.SOLID);
        ground.setAcceptedStates(ElementState.LIQUID);

        air.setAcceptedStates(ElementState.GAS);
        air.setAcceptedStates(ElementState.PLASMA);

    }

    public float getPressure(){
        return air.getPressure();
    }

    public float getAirPressure(){
        return air.getPressure();
    }

    public void addElement(Element element){
        if (element == null)
            return;

        air.addElement(element);


    }

    public Element popElement(int atomicNum, ElementState state){
        Element element = new Element(0, 0,"" , atomicNum, state);

        if (state.equals(ElementState.GAS)){
            return air.popElement(element);
        }

        else if (state.equals(ElementState.SOLID)){
            return ground.popElement(element);
        }

        return null;

    }

    public Element popRandomAir(){
        return air.popRandomElement();
    }

    public void clearElements(){
        air.clearElements();
        ground.clearElements(); //TODO fix this

    }

    public void addHeat(double C){
        heat += C;

        //Element element = air.checkTransitions(heat);

        //TODO fix this
        //When added heat, elements change state
        //However if heat is not added, state should go back?

    }

    public double giveHalfOfHeat(){
        heat /= 2;
        return heat;
    }


    public void heatTransition(){

        //This is fixed

        air.applyHeat(heat);
            //Element element = ground.applyHeat(heat/2);

        heat = 0;


            /*if (element != null) {
                System.out.println("CHANGE");
                ground.popElement(element);
                air.addElement(element);

            }*/




        /*else {
            if (baseHeat + heat < 0){
                Element element = air.applyHeat((baseHeat + heat )/2);
                ground.applyHeat((baseHeat + heat )/2);

                heat += Gdx.graphics.getDeltaTime();
                if (element != null){

                    System.out.println("CHANGE");
                    air.popElement(element);
                    ground.addElement(element);
                }
            }

        }*/

        heat += air.goToStable();

    }
    public double getE(){

        return air.getTotE() + heat;
    }



    public boolean getPlasma(){

        for (int i = 0; i < air.layer.size; i++){
            Element element = air.layer.get(i);

            if (element.getIntState() > 2)
                return true;
        }

        return false;
    }

}

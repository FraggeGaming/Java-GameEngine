package TestFiles.scripts.sim;

import EntityEngine.Utils.Tags;

import java.util.Objects;

public class Element {
    float mass;
    float volume;
    int atomicNum;
    double faseChange = 10; //kelvin it takes to change fase
    String name;
    ElementState state; //mby remove
    int intState;
    double e = 0; //energy value
    Tags tag;

    public Element(float mass, float volume, String name, int atomicNum, ElementState state){
        this.mass = mass;
         this.volume = volume;
         this.state = state;
         this.atomicNum = atomicNum;
         this.name = name;
         tag = new Tags();

         if (state.equals(ElementState.SOLID))
             intState = 0;
         else if (state.equals(ElementState.LIQUID))
             intState = 1;

         else if (state.equals(ElementState.GAS))
             intState = 2;
         else
             intState = 3;

         e = equilibrium();

    }

    public double equilibrium(){
        return intState * faseChange;
    }

    @Override
    public String toString() {
        return "Element{" +
                "mass=" + mass +
                ", volume=" + volume +
                ", atomicNum=" + atomicNum +
                ", faseChange=" + faseChange +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", tag=" + tag +
                '}';
    }

    public void addEnergy(double addedHeat){

        e += addedHeat;
        generateState(getIntState());
    }

    public double dissipate(){

        double d = e - equilibrium();

        //THIS SHIT DOESENT WORK
        if (d > 0){
            double cut = d/2;
            e -= cut;
            return cut;
        }

        return 0;

    }


    public int getIntState(){
        return (int)(e/faseChange);
    }

    public void generateState(int i){
        if (i == 0)
            state = ElementState.SOLID;
        if (i == 1)
            state = ElementState.LIQUID;

        if (i == 2)
            state = ElementState.GAS;
        if (i >= 3)
            state = ElementState.PLASMA;

        //System.out.println("Element: " + name + ", Total energy: " + e + ", State: " + state);

    }

    public int getState(){
        int s;
        if (state.equals(ElementState.SOLID))
            s = 0;
        else if (state.equals(ElementState.LIQUID))
            s = 1;

        else if (state.equals(ElementState.GAS))
            s = 2;
        else
            s = 3;

        return s;

        //System.out.println("Element: " + name + ", Total energy: " + e + ", State: " + state);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Element)) return false;
        Element element = (Element) o;
        return atomicNum == element.atomicNum && state == element.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mass, volume, atomicNum, state);
    }
}

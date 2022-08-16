package TestFiles.scripts.sim;

import java.util.Objects;

public class Element {
    float mass;
    float volume;
    int atomicNum;
    ElementState state;

    public Element(float mass, float volume, int atomicNum, ElementState state){
        this.mass = mass;
         this.volume = volume;
         this.state = state;
         this.atomicNum = atomicNum;
    }

    @Override
    public String toString() {
        return "Element{" +
                "mass=" + mass +
                ", volume=" + volume +
                ", atomicNum=" + atomicNum +
                ", state=" + state +
                '}';
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

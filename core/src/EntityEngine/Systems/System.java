package EntityEngine.Systems;

import EntityEngine.Engine;
import EntityEngine.Entity;
/*
* Every system can get the camera and batch and other values from the engine
*
* */
public class System {
    int priorityOrder = 10;
    Engine engine;
    Long startTime;
    Long endTime;
    Long duration = 0L;
    int iteration = 0;
    Long averageDuration = 0L;

    public System(){

    }

    public void update(float dt){

    }

    public void addEntity(Entity entity){

    }

    public void addEngine(Engine engine){
        this.engine = engine;
    }

    public void setPriority(int priority){
        priorityOrder = priority;
    }

    public Long getFunctionDuration(){
        return averageDuration/100;
    }

    public void startTimer(){

        startTime = java.lang.System.nanoTime();
    }

    public void endTimer(){
        iteration++;
        endTime = java.lang.System.nanoTime();
        duration += (endTime - startTime);
        if (iteration >= 500){
            averageDuration = duration/iteration;
            iteration = 0;
            duration = 0L;
        }

    }



}

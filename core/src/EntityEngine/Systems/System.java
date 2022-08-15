package EntityEngine.Systems;

import EntityEngine.Engine;
import EntityEngine.Utils.DebugTimer;
import EntityEngine.Utils.Entity;
/*
* Every system can get the camera and batch and other values from the engine
*
* */
public class System {
    int priorityOrder = 10;
    public Engine engine;
    DebugTimer timer;
    public boolean isActive = true;

    public System(){
        timer = new DebugTimer();
    }

    public void onCreate(){

    }

    public void update(float dt){

    }


    public void render(float dt){

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
        return timer.getAverageDuration();
    }

    public void startTimer(){

       timer.startTimer();
    }

    public void endTimer(){
        timer.endTimer();

    }

    public void dispose(){

    }


    public void preRender(float dt) {
    }

    public void postRender(float dt) {
    }

    public void postCreate() {
    }

    public void UIRender(float dt) {
    }

    public void reset() {
    }

    public void lastUpdate(float dt) {
    }

    public void removeEntity(Entity entity) {
    }
}

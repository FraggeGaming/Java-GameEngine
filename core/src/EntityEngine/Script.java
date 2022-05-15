package EntityEngine;

public abstract class Script {
    public Engine engine;
    public void addEngine(Engine engine){
        this.engine = engine;
    }

    public abstract void loadAssets();

    public abstract void onCreate();
}

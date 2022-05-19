package EntityEngine.Systems;
import EntityEngine.Components.TimerComponent;
import EntityEngine.Entity;
import EntityEngine.Systems.System;
import com.badlogic.gdx.utils.Array;

public class TimerSystem extends System {
    Array<Entity> timedEntities = new Array<>();
    Array<Entity> toRemove = new Array<>();
    TimerComponent timerComponent;
    Entity entity;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void addEntity(Entity entity){

        timerComponent = (TimerComponent) entity.getComponent(TimerComponent.class);
        if (timerComponent != null)
            timedEntities.add(entity);
    }

    @Override
    public void update(float dt) {
        timedEntities.removeAll(toRemove, true);
        toRemove.clear();

        for (int i = 0; i < timedEntities.size; i++){
            entity = timedEntities.get(i);
            if (entity != null && !entity.flagForDelete){
                timerComponent = (TimerComponent) entity.getComponent(TimerComponent.class);
                if (timerComponent != null){
                    timerComponent.update(dt);

                    if (timerComponent.isDead()){
                        toRemove.add(entity);
                        engine.removeEntity(entity);
                    }
                }
            }

            else if (entity != null){
                toRemove.add(entity);
            }
        }
    }

    public Array<Entity> entities(){
        return timedEntities;
    }
}

package TestFiles.scripts.Components;

import EntityEngine.Components.Component;

public class TimerComponent extends Component {
    float time;
    public TimerComponent(float time){
        this.time = time;

    }

    public void update(float time){
        this.time -= time;
    }

    public boolean isDead(){
        return time < 0;
    }
}

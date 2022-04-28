package TestFiles.scripts;

import EntityEngine.Components.Component;

public class LifeCount extends Component {
    float life;
    public LifeCount(float life){
        this.life = life;
    }

    public void live(float time){
        life -= time;
    }

    public boolean isDead(){
        return life < 0;
    }
}

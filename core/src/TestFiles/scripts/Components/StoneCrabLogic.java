package TestFiles.scripts.Components;

import EntityEngine.Components.Component;

public class StoneCrabLogic extends Component {
    public boolean isScared = false;
    public StoneCrabLogic(){

    }

    public void setScared(Boolean scared){
        this.isScared = scared;
    }
}

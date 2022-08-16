package TestFiles.scripts.Systems;

import EntityEngine.Components.TransformComponent;
import EntityEngine.Systems.System;
import TestFiles.scripts.sim.Element;
import TestFiles.scripts.sim.ElementState;
import TestFiles.scripts.sim.TileSim;
import TestFiles.scripts.sim.TileSimManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InputManager extends System {

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            engine.exit();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.M)){
            TileSimManager tileSimManager = (TileSimManager) engine.getSystem(TileSimManager.class);
            TransformComponent t = (TransformComponent) engine.getEntity("Player").getComponent(TransformComponent.class);
            TileSim tileSim = tileSimManager.getTile(t.getOriginX(), t.getOriginY());


           tileSim.addElement(new Element(1, 5, 1, ElementState.GAS));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.N)){
            TileSimManager tileSimManager = (TileSimManager) engine.getSystem(TileSimManager.class);
            for (int i = 0; i < 2 ; i++)tileSimManager.sim();
        }

        movement();
    }

    public void movement(){
        float moveSpeed = 70;
        float x = 0, y = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            //change vertical direction
            x -= moveSpeed;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            //change vertical direction
            x += moveSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            //change vertical direction
            y += moveSpeed;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            //change vertical direction
            y -= moveSpeed;
        }

        MovementSystem system = (MovementSystem) engine.getSystem(MovementSystem.class);
        system.movePlayer(x, y);
    }
}

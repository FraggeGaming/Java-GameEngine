package TestFiles.scripts.Systems;

import EntityEngine.Systems.System;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InputManager extends System {

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            engine.exit();
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

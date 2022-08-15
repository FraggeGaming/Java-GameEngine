package EntityEngine.Debug;

import EntityEngine.Engine;
import EntityEngine.Systems.System;
import TestFiles.scripts.UIItem;
import EntityEngine.Systems.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


public class DebugLabel {
    Label l;
    Stage stage;
    int whatToDebug = -1;
    String text;
    GLProfiler profiler;
    Engine engine;

    int boxWidth = 180;
    int boxHeight = 30;
    int labelHeight = 20;
    public DebugLabel(Label.LabelStyle style, Stage stage, int y, String text, GLProfiler profiler, Engine engine, int whatToDebug){
        this.stage = stage;
        this.text = text;
        l = new Label("", style);
        this.profiler = profiler;
        this.engine = engine;
        this.whatToDebug = whatToDebug;
        createStat(l, y);
    }

    public void setText(int i){
        l.setText(text + ": " + i);
    }

    public void setText(Long i){
        l.setText(text + ": " + i);
    }

    public void setText(double i){
        l.setText(text + ": " + i);
    }

    public void render(){
        if (whatToDebug == 0)
            setText(Gdx.graphics.getFramesPerSecond());
        else if (whatToDebug == 1){
            setText(profiler.getDrawCalls());
        }
        else if (whatToDebug == 2){
            setText(profiler.getCalls());
        }
        else if (whatToDebug == 3){
            setText(profiler.getVertexCount().total);
        }
        else if (whatToDebug == 4){
            setText(getDrawnEntities());
        }
        else if (whatToDebug == 5){
            setText((double)Math.round(Gdx.graphics.getFramesPerSecond()/(double)getDrawnEntities() * 10000f)/10000f);
        }
        else if (whatToDebug == 6){
            setText(engine.componentMap.size());
        }
        else if (whatToDebug == 7){
            setText(getAnimations());
        }
        else if (whatToDebug == 8){
            setText(getCollisions());
        }
        else if (whatToDebug == 9){
            setText(getCollidableObjectsInRange());
        }
        else if (whatToDebug == 10){
            setText(getSystemFunctionTime(SpatialRenderer.class));
        }

        else if (whatToDebug == 11){
            setText(getSystemFunctionTime(CollisionDetectionSystem.class));
        }
        else if (whatToDebug == 12){
            setText(getSystemFunctionTime(Debugger.class));
        }

        else if (whatToDebug == 13){
            setText(getSystemFunctionTime(AnimationSystem.class));
        }

        else if (whatToDebug == 14){
            setText(getSystemFunctionTime(ComponentManagerSystem.class));
        }

        else if (whatToDebug == 20){
            setText(getSystemFunctionTime(TileMapRenderer.class));
        }

        else if (whatToDebug == 21){
            setText(getSystemFunctionTime(PhysicsSystem.class));
        }

        else if (whatToDebug == 22){
            setText(getSystemFunctionTime(LightningSystem.class));
        }

        else if (whatToDebug == 23){
            setText(engine.updateTime.getAverageDuration());
        }

        else if (whatToDebug == 24){
            setText(engine.renderTime.getAverageDuration());
        }
    }

    public long getSystemFunctionTime(Class<?extends System> System){
        if (engine.getSystem(System) != null)
            return engine.getSystem(System).getFunctionDuration();

        return -1;
    }

    public int getCollidableObjectsInRange(){
        if(engine.getSystem(CollisionDetectionSystem.class) != null){
            CollisionDetectionSystem s = (CollisionDetectionSystem) engine.getSystem(CollisionDetectionSystem.class);

            return s.getCollidableComponentsDebug();
        }
        return -1;
    }

    public int getDrawnEntities(){
        if (engine.getSystem(SpatialRenderer.class) != null){
            SpatialRenderer s = (SpatialRenderer) engine.getSystem(SpatialRenderer.class);
            return s.drawnEntities;
        }

        return -1;
    }

    public int getAnimations(){
        if (engine.getSystem(AnimationSystem.class) != null){
            AnimationSystem s = (AnimationSystem) engine.getSystem(AnimationSystem.class);
            return s.numOfAnimations;
        }

        return -1;
    }

    public int getCollisions(){
        if(engine.getSystem(CollisionDetectionSystem.class) != null){
            CollisionDetectionSystem s = (CollisionDetectionSystem) engine.getSystem(CollisionDetectionSystem.class);

            return s.getNumOfCollisions();
        }

        return -1;
    }

    private void createStat(com.badlogic.gdx.scenes.scene2d.ui.Label label, int y){
        UIItem item = new UIItem(stage);
        item.setTexture(createTexture(Color.WHITE, 200,200));
        item.setBounds(boxWidth,boxHeight);
        item.setMargin(10);
        item.floatTop();
        item.floatLeft();
        item.translate(0, y);

        UIItem child = new UIItem(stage);
        item.addChild(child);
        child.setBounds(boxWidth,labelHeight);
        child.setMargin(0, 0, 0, 10);

        child.setCenter();
        child.floatLeft();
        child.syncActor(label);

        stage.addActor(label);
    }

    private Texture createTexture(Color color, int height, int width) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        if (color == null)
            pixmap.setColor(Color.GRAY);

        else
            pixmap.setColor(color);

        pixmap.fill();

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

}

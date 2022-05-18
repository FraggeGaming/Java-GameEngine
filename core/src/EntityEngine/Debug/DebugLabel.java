package EntityEngine.Debug;

import EntityEngine.Engine;
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
            setText(engine.getDrawnEntities());
        }
        else if (whatToDebug == 5){
            setText((double)Math.round(Gdx.graphics.getFramesPerSecond()/(double)engine.getDrawnEntities() * 10000f)/10000f);
        }
        else if (whatToDebug == 6){
            setText(engine.componentMap.size());
        }
        else if (whatToDebug == 7){
            setText(engine.getAnimations());
        }
        else if (whatToDebug == 8){
            setText(engine.getCollisions());
        }
        else if (whatToDebug == 9){
            setText(engine.getCollidableObjectsInRange());
        }
        else if (whatToDebug == 10){
            setText(engine.getSystemFunctionTime(SpatialRenderer.class));
        }

        else if (whatToDebug == 11){
            setText(engine.getSystemFunctionTime(CollisionDetectionSystem.class));
        }
        else if (whatToDebug == 12){
            setText(engine.getSystemFunctionTime(Debugger.class));
        }

        else if (whatToDebug == 13){
            setText(engine.getSystemFunctionTime(AnimationSystem.class));
        }

        else if (whatToDebug == 14){
            setText(engine.getSystemFunctionTime(ComponentManagerSystem.class));
        }

        else if (whatToDebug == 20){
            setText(engine.getSystemFunctionTime(TileMapRenderer.class));
        }

        else if (whatToDebug == 21){
            setText(engine.getSystemFunctionTime(PhysicsSystem.class));
        }



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

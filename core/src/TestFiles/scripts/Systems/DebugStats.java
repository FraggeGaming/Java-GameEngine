package TestFiles.scripts.Systems;

import EntityEngine.Debug.DebugLabel;
import EntityEngine.Engine;
import EntityEngine.Systems.System;
import TestFiles.scripts.UIItem;
import EntityEngine.Systems.NetworkManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class DebugStats extends System {
    Stage stage;
    Label frameTimeLabel;

    BitmapFont font;
    Label.LabelStyle style;

    long frameTime;
    long frameAverage;
    long frameCount;
    int fMP = 20; // frame it takes to update stats
    int dbB = 35; //distance bewteen boxes
    int labelOrder = 0;

    TextButton debugger;
    TextureAtlas buttonAtlas;
    private GLProfiler profiler;
    Array<DebugLabel> labels = new Array<>();
    DebugLabel frameTimeDebug;

    String ip = "192.168.50.181"; //"94.255.149.44";
    int port = 1234;


    @Override
    public void onCreate() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont();
        buttonAtlas = new TextureAtlas("atlas/TP.atlas");
        style = new Label.LabelStyle(font, Color.BLACK);
        frameTimeLabel = new Label(" ", style);
        stage.addActor(frameTimeLabel);
        profiler = new GLProfiler(Gdx.graphics);
        profiler.enable();

        frameTimeDebug = new DebugLabel(style, stage, -dbB*(labelOrder++), "FrameTime", profiler, engine, -1);
        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"Renderer function time", profiler, engine, 10 ));
        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"Tilemap function time", profiler, engine, 20 ));
        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"Collision detection function time", profiler, engine, 11 ));
        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"Debugger function time", profiler, engine, 12 ));
        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"Animation function time", profiler, engine, 13 ));
        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"Component manager function time", profiler, engine, 14 ));
        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"Physics function time", profiler, engine, 21 ));
        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"Light function time", profiler, engine, 22 ));

        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"Fps", profiler, engine, 0 ));
        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"DrawCalls", profiler, engine, 1 ));
        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"Gl Calls", profiler, engine, 2 ));
        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"VertexCount", profiler, engine, 3 ));
        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"Drawn Entities", profiler, engine, 4 ));
        //labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"FPS per entity", profiler, engine, 5 ));
        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"Total Entities", profiler, engine, 6 ));
        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"Rendered animations", profiler, engine, 7 ));
        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"Objects in collision", profiler, engine, 8 ));
        labels.add(new DebugLabel(style, stage, -dbB*(labelOrder++),"Potential collisions", profiler, engine, 9 ));


        UIItem item = new UIItem(stage);
        item.setMargin(10);
        item.floatTop();
        item.floatLeft();
        item.translate(30, -dbB*(labelOrder++));
        debugger = createButton("Debug entities", item.getX(), item.getY());
        debugger.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {

                engine.toggleDebug();
            }
        });

        item = new UIItem(stage);
        item.setMargin(10);
        item.floatTop();
        item.floatLeft();
        item.translate(30, -40*(labelOrder++));
        debugger = createButton("Collision debug",  item.getX(), item.getY());
        debugger.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                engine.toggleDebugBox2D();
            }
        });



    }

    @Override
    public void update(float dt) {
        stage.draw();
        stage.act(dt);

        render();
    }

    public void addNetworkButtons(){
        if(engine.getSystem(NetworkManager.class) != null){

            UIItem item = new UIItem(stage);
            item.setMargin(10);
            item.floatTop();
            item.floatLeft();
            item.translate(30, -45*(labelOrder++));
            debugger = createButton("Host Game: " + ip,  item.getX(), item.getY());
            debugger.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    NetworkManager manager = (NetworkManager) engine.getSystem(NetworkManager.class);
                    manager.openNetwork(true, ip, port);
                }
            });

            item = new UIItem(stage);
            item.setMargin(10);
            item.floatTop();
            item.floatLeft();
            item.translate(30, -49*(labelOrder++));
            debugger = createButton("Join game: " + ip,  item.getX(), item.getY());
            debugger.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    NetworkManager manager = (NetworkManager) engine.getSystem(NetworkManager.class);
                    manager.openNetwork(false, ip, port);
                }
            });

            item = new UIItem(stage);
            item.setMargin(10);
            item.floatTop();
            item.floatLeft();
            item.translate(30, -50*(labelOrder++));
            debugger = createButton("Join as Player 1" ,  item.getX(), item.getY());
            debugger.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    engine.user = "Player";
                }
            });

            item = new UIItem(stage);
            item.setMargin(10);
            item.floatTop();
            item.floatLeft();
            item.translate(30, -52*(labelOrder++));
            debugger = createButton("Join as Player 2",  item.getX(), item.getY());
            debugger.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    engine.user = "Player2";
                }
            });
        }
    }

    private TextButton createButton(String text, float x, float y){

        TextButton.TextButtonStyle b = new TextButton.TextButtonStyle();
        b.font = font;
        Skin skin = new Skin();
        skin.addRegions(buttonAtlas);
        b.up = skin.getDrawable("ZombieAnt");
        TextButton button = new TextButton(text, b);
        stage.addActor(button);

        button.setBounds(x, y, 100, 100);

        return button;
    }

    private void renderStats(){
        for (int i = 0; i < labels.size; i++){
            labels.get(i).render();
        }
    }


    public void render(){

        frameCount++;
        if (frameCount > fMP){
            frameAverage = TimeUtils.nanosToMillis(TimeUtils.timeSinceNanos(frameTime) / frameCount);
            frameTimeDebug.setText(frameAverage);
            frameTime = TimeUtils.nanoTime();
            frameCount = 0;
            renderStats();

        }

        profiler.reset();
    }
}

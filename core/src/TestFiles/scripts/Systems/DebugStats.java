package TestFiles.scripts.Systems;

import EntityEngine.Debug.DebugLabel;
import EntityEngine.Systems.*;
import EntityEngine.Systems.System;
import TestFiles.scripts.UIItem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;


public class DebugStats extends System {
    Label frameTimeLabel;

    BitmapFont font;
    Label.LabelStyle style;

    long frameTime;
    long frameAverage;
    long frameCount;

    long highestframeTime;
    long highestframeAverage;
    long highestframeCount;
    Label highestframeTimeLabel;
    DebugLabel highestframeTimeDebug;

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

        font = new BitmapFont();
        buttonAtlas = new TextureAtlas("atlas/TP.atlas");
        style = new Label.LabelStyle(font, Color.BLACK);

        frameTimeLabel = new Label(" ", style);
        highestframeTimeLabel = new Label(" ", style);

        StageHandler handler = (StageHandler) engine.getSystem(StageHandler.class);

        handler.UI.addActor(frameTimeLabel);
        profiler = new GLProfiler(Gdx.graphics);
        profiler.enable();

        frameTimeDebug = new DebugLabel(style, handler.UI, -dbB*(labelOrder++), "FrameTime", profiler, engine, -1);
        highestframeTimeDebug = new DebugLabel(style, handler.UI, -dbB*(labelOrder++), "Highest FrameTime", profiler, engine, -1);
        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"Renderer function time", profiler, engine, 10 ));
        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"Tilemap function time", profiler, engine, 20 ));
        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"Collision detection function time", profiler, engine, 11 ));
        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"Debugger function time", profiler, engine, 12 ));
        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"Animation function time", profiler, engine, 13 ));
        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"Component manager function time", profiler, engine, 14 ));
        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"Physics function time", profiler, engine, 21 ));
        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"Light function time", profiler, engine, 22 ));

        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"Fps", profiler, engine, 0 ));
        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"DrawCalls", profiler, engine, 1 ));
        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"Gl Calls", profiler, engine, 2 ));
        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"VertexCount", profiler, engine, 3 ));
        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"Drawn Entities", profiler, engine, 4 ));
        //labels.add(new DebugLabel(style, engine.stage, -dbB*(labelOrder++),"FPS per entity", profiler, engine, 5 ));
        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"Total Entities", profiler, engine, 6 ));
        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"Rendered animations", profiler, engine, 7 ));
        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"Objects in collision", profiler, engine, 8 ));
        labels.add(new DebugLabel(style, handler.UI, -dbB*(labelOrder++),"Potential collisions", profiler, engine, 9 ));


        UIItem item = new UIItem(handler.UI);
        item.setMargin(10);
        item.floatTop();
        item.floatLeft();
        item.translate(30, -dbB*(labelOrder++));
        debugger = createButton("Debug entities", item.getX(), item.getY());
        debugger.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {

                Debugger s = (Debugger) engine.getSystem(Debugger.class);
                if (s != null){
                    s.debug = !s.debug;
                    s.debugBox2D = false;
                }

            }
        });


        item = new UIItem(handler.UI);
        item.setMargin(10);
        item.floatTop();
        item.floatLeft();
        item.translate(30, (-dbB - 3)*(labelOrder++));
        debugger = createButton("Collision debug",  item.getX(), item.getY());
        debugger.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Debugger s = (Debugger) engine.getSystem(Debugger.class);
                if (s != null){
                    s.debugBox2D = !s.debugBox2D;
                    s.debug = s.debugBox2D;
                }

            }
        });


        item = new UIItem(handler.UI);
        item.setMargin(10);
        item.floatTop();
        item.floatLeft();
        item.translate(30, (-dbB - 6)*(labelOrder++));
        debugger = createButton("Turn off Lights",  item.getX(), item.getY());
        debugger.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                LightningSystem s = (LightningSystem) engine.getSystem(LightningSystem.class);
                if (s != null){
                    s.isActive = !s.isActive;

                }
            }
        });


        item = new UIItem(handler.UI);
        item.setMargin(10);
        item.floatTop();
        item.floatLeft();
        item.translate(30, (-dbB - 8)*(labelOrder++));
        debugger = createButton("debug Navmesh",  item.getX(), item.getY());
        debugger.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Debugger s = (Debugger) engine.getSystem(Debugger.class);
                if (s != null){
                    s.debugNavmesh = !s.debugNavmesh;

                }
            }
        });


    }

    @Override
    public void update(float dt) {
        render();
    }

    public void addNetworkButtons(){
        StageHandler handler = (StageHandler) engine.getSystem(StageHandler.class);
        if(engine.getSystem(NetworkManager.class) != null){

            UIItem item = new UIItem(handler.UI);
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

            item = new UIItem(handler.UI);
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

            item = new UIItem(handler.UI);
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

            item = new UIItem(handler.UI);
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
        StageHandler handler = (StageHandler) engine.getSystem(StageHandler.class);

        TextButton.TextButtonStyle b = new TextButton.TextButtonStyle();
        b.font = font;
        Skin skin = new Skin();
        skin.addRegions(buttonAtlas);
        b.up = skin.getDrawable("ZombieAnt");
        TextButton button = new TextButton(text, b);
        handler.UI.addActor(button);

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


        highestframeCount++;
        if (highestframeCount > 2000){
           highestframeAverage = 0;
            highestframeCount = 0;
        }

        long c = TimeUtils.timeSinceNanos(highestframeTime);
        long t = TimeUtils.nanosToMillis(c);

        if (highestframeAverage < t){
            highestframeAverage = t;
            highestframeTimeDebug.setText(c);
        }

        highestframeTime = TimeUtils.nanoTime();



        profiler.reset();
    }

    @Override
    public void dispose() {

        font.dispose();


    }
}

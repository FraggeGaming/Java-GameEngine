package TestFiles.scripts.sim;


import EntityEngine.Systems.System;
import EntityEngine.Utils.Entity;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.List;

public class TileSimManager extends System {

    TileSim tileSim;
    public List<List<TileSim>> tileMap;
    public int tileSize = 16;

    ShapeRenderer shapeRenderer = new ShapeRenderer();


    @Override
    public void onCreate() {
        shapeRenderer.setAutoShapeType(true);
        tileMap = new ArrayList<>();

    }

    @Override
    public void addEntity(Entity entity) {

        if (entity.getComponent(TileSim.class) != null){
            tileSim = (TileSim) entity.getComponent(TileSim.class);

            while (tileMap.size() <= tileSim.x/tileSize){
                tileMap.add(new ArrayList<>());
            }
            tileMap.get((int) tileSim.x/tileSize ).add((int)tileSim.y/tileSize, tileSim);
        }
    }

    public TileSim getTile(int x, int y){
        return tileMap.get(x).get(y);
    }

    public void freeTile(float x, float y){
        getTile(x,y).blocked = false;
    }

    public void setBlocked(float x, float y){
        getTile(x,y).blocked = true;
    }


    public TileSim getTile(float x, float y) {
        return tileMap.get((int)x/tileSize).get((int)y/tileSize);
    }

    @Override
    public void update(float dt) {



    }

    public void sim(){
        for (int i = 0; i < tileMap.size(); i++){
            for (int j = 0; j < tileMap.get(i).size(); j++){
                simulateTile(i,j);
            }
        }
    }

    private void simulateTile(int index, int jndex){

        TileSim sim = tileMap.get(index).get(jndex);

        if (sim.volume > 0){

            int x = getRandomDir(index);
            int y = getRandomDir(jndex);

            if (x >= 0 && y >= 0)

            if (x < tileMap.size() && y < tileMap.get(x).size()){
                TileSim next = tileMap.get(x).get(y);

                if (sim.volume > next.volume){
                    next.addElement(sim.popRandomElement());

                }
            }


            /*

            for (int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++){
                    if (index-1 + i >= 0 && jndex-1 + j >= 0){
                        if (i == 1 && j == 1)
                            continue;

                        if (index-1 + i < tileMap.size() && jndex-1 + j < tileMap.get(index-1 + i).size()){
                            TileSim nextTile = tileMap.get(index-1 + i).get(jndex-1 + j);

                            if (sim.volume > nextTile.volume){
                                Element element = sim.popRandomElement();
                                //java.lang.System.out.println(sim.mass);

                                if (element != null){
                                    //java.lang.System.out.println(element);
                                    nextTile.addElement(element);

                                }
                            }
                        }
                    }
                }
            }*/
        }

    }

    private int getRandomDir(int index) {
        return index + engine.getRandomInteger(3) -1;

    }


    public void setTileSize(int i) {
        tileSize = i;
    }
}

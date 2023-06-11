package TestFiles.scripts.sim;


import EntityEngine.Systems.System;
import EntityEngine.Utils.Entity;
import java.util.ArrayList;
import java.util.List;

public class TileSimManager extends System {

    TileSim tileSim;
    public List<List<TileSim>> tileMap;
    public int tileSize = 16;


    float updateRate = 5;
    float updateTimer = 0;
    @Override
    public void onCreate() {
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
        TileSim tile = getTile(x,y);
        if (tile != null)
            tile.blocked = false;
        else {
            try {
                throw new Exception("Tile does not exist, Action dismissed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setBlocked(float x, float y){
        TileSim tile = getTile(x,y);
        if (tile != null)
            tile.blocked = true;
        else {
            try {
                throw new Exception("Tile does not exist, Action dismissed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public TileSim getTile(float x, float y) {
        if((int)x/tileSize < tileMap.size() && (int)y/tileSize < tileMap.get((int) (x/tileSize)).size())
            return tileMap.get((int)x/tileSize).get((int)y/tileSize);
        try {
            throw new Exception("Tile exeeds map bounds, Tile does not exist");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(float dt) {

        updateTimer += dt;
        if (updateTimer >= 1/updateRate){
            sim();

            updateTimer = 0;
        }


    }

    public void sim(){
        for (int i = 0; i < tileMap.size(); i++){
            for (int j = 0; j < tileMap.get(i).size(); j++){
                simulateTile(i,j);
            }
        }
        java.lang.System.out.println(getTotalEnergy());
        //cal();
    }

    private void simulateTile(int index, int jndex){

        TileSim sim = tileMap.get(index).get(jndex);

        sim.heatTransition();

        if (sim.getPressure() > 0){

            //TODO: change this,  higher pressure difference, it will try harder to find a tile
            for (int i = 0; i < 5; i++){
                int x = getRandomDir(index);
                int y = getRandomDir(jndex);


                if (x >= 0 && y >= 0){
                    if (x < tileMap.size() && y < tileMap.get(x).size()){
                        TileSim next = tileMap.get(x).get(y);

                        //TODO heat sensitivity???????
                        if (sim.heat > next.heat)
                            next.addHeat(sim.giveHalfOfHeat());

                        if (!next.blocked){
                            if (sim.getPressure() > next.getPressure()){

                                next.addElement(sim.popRandomAir());
                                //TODO: Change this to a system based on pressure, higher pressure difference, more atoms transfered
                                if (sim.getPressure() > next.getPressure()){
                                    next.addElement(sim.popRandomAir());
                                }

                                return;
                            }

                        }
                    }

                }
            }

        }



    }

    private void cal(){
        float s = 0;
        for (int i = 0; i < tileMap.size(); i++){
            for (int j = 0; j < tileMap.get(i).size(); j++){
               s += tileMap.get(i).get(j).getPressure();
            }
        }

        java.lang.System.out.println(s);
    }

    public double getTotalEnergy(){
        double s = 0;
        for (int i = 0; i < tileMap.size(); i++){
            for (int j = 0; j < tileMap.get(i).size(); j++){
                s += tileMap.get(i).get(j).air.getTotE();
                s += tileMap.get(i).get(j).heat;
            }
        }

        return s;
    }

    private int getRandomDir(int index) {
        return index + engine.getRandomInteger(3) -1;
    }

    public void setTileSize(int i) {
        tileSize = i;
    }
}

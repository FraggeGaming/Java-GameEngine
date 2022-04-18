package TestFiles;

import EntityEngine.GameClasses.TDCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;


public class ECS {
    Texture img;
    Array<Texture> Textures;
    Array<Vector2> positions;
    ComparePositions comparator;

    Vector2 temp = new Vector2();
    TDCamera camera;
    Sprite center;

    int radius = 15;
    int area = (int) (Math.PI*(radius*radius));

    public ECS(TDCamera camera){
        this.camera = camera;
        comparator = new ComparePositions();
        updateCamera();

        Textures = new Array<>();
        positions = new Array<>();
        img = new Texture("tex.png");
        for (int i = 0; i < 5000; i++){
            for (int j = 0; j < 5000; j++){

                positions.add(new Vector2(i*15, j*15));
                Textures.add(img);

            }
        }
    }

    public void updateCamera(){
        temp.set(camera.position.x, camera.position.y);
        comparator.updateCenter(temp);
    }

    public void sort(){
        comparator.updateCenter(temp);
        positions.sort(comparator);
    }

    public void render(Batch batch){
        temp.set(camera.position.x, camera.position.y);
        int draws = 0;
        int nonDraws = 0;
        boolean sortFlag = false;
        for (int i = 0; i < positions.size; i++){
            if (distance(positions.get(i), temp) < radius*15){
                batch.draw(Textures.get(i),positions.get(i).x, positions.get(i).y);

                draws++;

                if (draws >= area)
                    break;
            }

            else {
                nonDraws++;
                    if (nonDraws > 50000){
                        //System.out.println(nonDraws);
                       sortFlag = true;
                    }

            }

        }

        /*if (sortFlag){
            sort();
        }

        /*positions.get(0).set(temp);
        batch.draw(Textures.get(0), positions.get(0).x, positions.get(0).y);*/


    }

    private double distance(Vector2 u, Vector2 v){
        return Math.sqrt((u.x - v.x)*(u.x - v.x) + (u.y - v.y)*(u.y - v.y));
    }
}

class ComparePositions implements Comparator<Vector2> {
    Vector2 center = new Vector2();
    public ComparePositions(){
    }

    public void updateCenter(Vector2 cameraVec){
        center.set(cameraVec);
    }

    @Override
    public int compare(Vector2 o1, Vector2 o2) {

    return distance(o1, center) - distance(o2, center);
    }

    private int distance(Vector2 u, Vector2 v){
        return (int)Math.sqrt((u.x - v.x)*(u.x - v.x) + (u.y - v.y)*(u.y - v.y));
    }


}

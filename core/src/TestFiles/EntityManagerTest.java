package TestFiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class EntityManagerTest {
    List<Entity2> entityList;
    public EntityManagerTest(){
        entityList = new ArrayList<>();
        Entity2 entity;
        Vector2 vec = new Vector2(0, 0);
        Texture img = new Texture("tex.png");

        for (int i = 0; i < 1000; i++){
            for (int j = 0; j < 1000; j++){
                vec.set(i*15, j*15);
                entity = new Entity2(img, vec);
                entityList.add(entity);
            }
        }
    }

    public void render(Batch batch){
        for (int i = 0; i < entityList.size(); i++){
            entityList.get(i).draw(batch);
        }
    }

}

class Entity2{
    Sprite sprite;

    public Entity2(Texture texture, Vector2 vec){

        sprite = new Sprite(texture);
        sprite.setBounds(vec.x, vec.y, 15, 15);
    }

    public void draw(Batch batch){
        sprite.draw(batch);
    }
}

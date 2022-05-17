package TestFiles.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UIItem extends Actor {

    private Vector2 position = new Vector2();
    private Vector2 origo = new Vector2();
    private Vector2 bounds = new Vector2();
    private Vector2 temp = new Vector2();

    private float width = 100;
    private float height = 100;

    private float marginTop = 0;
    private float marginBottom = 0;
    private float marginLeft = 0;
    private float marginRight = 0;
    private boolean ignoreClamp = false;

    private Actor syncedActor;
    private Image image = null;
    private Texture texture;
    private Stage stage;
    public TextButton button;
    Skin skin;


    public UIItem(Stage stage){
        origo.set(0, 0);
        bounds.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.stage = stage;

        setBounds(origo.x, origo.y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(this);
        //setDebug(true);
        setTouchable(Touchable.disabled);

    }
    public void setButton(TextureAtlas atlas, String buttonUp, String ButtonDown, String buttonText){
        BitmapFont font = new BitmapFont();
        skin = new Skin();
        skin.addRegions(atlas);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        textButtonStyle.up = skin.getDrawable(buttonUp);
        textButtonStyle.down = skin.getDrawable(ButtonDown);

        button=new TextButton("", textButtonStyle);

        button.setText(buttonText);
        stage.addActor(button);
    }


    public void setTexture(Texture texture){
        this.texture = texture;
        image = new Image(texture);
        image.setBounds(origo.x, origo.y, width, height);
        stage.addActor(image);
    }

    public void setTextureFromAtlas(Sprite sprite){
        image = new Image(sprite);
        image.setBounds(origo.x, origo.y, width, height);
        stage.addActor(image);
    }

    public void updateTexture(Texture texture){
        image.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
    }

    public String toString(){

        return width + ", " + height;
    }
    public void addChild(UIItem item){
        item.bounds.set(width, height);
        item.origo.set(position);
        item.position.set(position);
        addChanges();
    }

    public void removeChild(UIItem item){
        item.bounds.set(bounds);
        item.origo.set(origo);
        item.position.set(position);
        addChanges();
    }

    /**
     * FIX THIS METHOD
     */
    public void setOriginToCenter(){
        origo.set(position.x + width/2, position.y + height/2);
    }

    public void setWidth(float x){
        width = x;
        if (image != null)
            image.setBounds(origo.x, origo.y, width, height);
        if (button != null){
            button.setBounds(origo.x, origo.y, width, height);
        }
        setBounds(origo.x, origo.y, width, height);
        addChanges();
    }

    public void setBounds(float width, float height){
        this.width = width;
        this.height = height;
        if (image != null)
            image.setBounds(origo.x, origo.y, width, height);
        if (button != null){
            button.setBounds(origo.x, origo.y, width, height);
        }
        setBounds(origo.x, origo.y, width, height);
        addChanges();
    }


    public void setPositionItem(float x, float y){
        position.add(x, y).add(origo);
        setPosition(position.x, position.y);

    }

    public void setCenter(){

        temp.set(bounds).scl(0.5f).add(origo);
        position.set(temp);

        position.add(-width/2, -height/2);

        addChanges();
    }

    public void translate(float x, float y){
        position.add(x,y);
        addChanges();
    }

    public void floatTop(){
        temp.set(position.x, origo.y + bounds.y);
        position.set(temp);
        addChanges();
    }

    public void floatLeft(){
        temp.set(0, position.y);
        position.set(temp);
        addChanges();
    }

    public void floatRight(){
        temp.set(origo.x + bounds.x, position.y);
        position.set(temp);
        addChanges();
    }

    public void floatBottom(){
        temp.set(position.x, origo.y);
        position.set(temp);
        addChanges();
    }

    public void setMargin(float top, float right, float bottom, float left){
        marginTop = top;
        marginRight = right;
        marginBottom = bottom;
        marginLeft = left;
        addChanges();
    }

    public void setMargin(float margin){
        marginTop = margin;
        marginRight = margin;
        marginBottom = margin;
        marginLeft = margin;
        addChanges();
    }

    private void clampToSection(){

        if (!ignoreClamp){
            if (position.x > origo.x + bounds.x - width )
                position.x = origo.x + bounds.x - width  - marginRight;
            else if (position.x <= origo.x)
                position.x = origo.x  + marginLeft;

            if (position.y > origo.y + bounds.y - height)
                position.y = origo.y + bounds.y - height  - marginTop;
            else if (position.y <= origo.y )
                position.y = origo.y + marginBottom;
        }


    }

    public void removeClamp(){
        ignoreClamp = true;
    }

    public void applyClamp(){
        ignoreClamp = false;
        addChanges();
    }

    private void addChanges(){
        //position.add(-width/2, -height/2);
        clampToSection();
        setPosition(position.x, position.y);

        if (syncedActor != null)
            syncedActor.setBounds(width,height, position.x,position.y);

        if (image != null)
            image.setPosition(position.x, position.y);
        if (button != null){
            button.setPosition(position.x, position.y);
        }
    }

    public void syncActor(Actor actor){
        syncedActor = actor;
        syncedActor.setBounds(position.x,position.y, width,height);

    }


    public Vector2 getPosition(){
        return  new Vector2(position);
    }

    public void hide(){
        if (image != null){
            image.setVisible(false);
        }

        setVisible(false);

    }

    public void show(){
        if (image != null)
            image.setVisible(true);
        setVisible(true);
    }
}

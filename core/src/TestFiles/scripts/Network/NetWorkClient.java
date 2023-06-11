package TestFiles.scripts.Network;

import EntityEngine.Components.CollisionComponent;
import EntityEngine.Components.TransformComponent;
import EntityEngine.Components.VelocityComponent;
import EntityEngine.Utils.Entity;
import EntityEngine.Network.ClientUpdate;
import EntityEngine.Network.NetWorkData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;

/*
* Host gets the data first and then can do that it wants with it, but when host send data everyone gets it
* To use the host correctly
*   Change data then apply to self, then send it to other clients
* */
public class NetWorkClient extends ClientUpdate {

    Data data;
    Entity e;
    Array<Data> hostPkt = new Array<>();



    TransformComponent t;
    CollisionComponent c;
    VelocityComponent v;
    float tickTimer = 0f;
    float serverTick = 5f; //for non instant update on movement

    String requestChange = "requestChange";
    String doUpdate = "doUpdate";

    @Override
    public void update() {

        /*
        * Example on how to use this class correctly
        */
        data = (Data) getData(Data.class);
        while (data != null){

            updateHost(data); //if host: change the data if needed
            onUpdate(data); //apply the data
            broadCastPkt(data); //if host: send data to other clients for uppdate

            data = (Data) getData(Data.class);
        }

        updateMovement();


    }

    private void broadCastPkt(Data data) {
        if (network.isHost){
            for (int i = 0; i < hostPkt.size; i++){
                giveData(data, Data.class);
            }
        }
    }

    private void updateMovement(){
        float x = 0, y = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            //change vertical direction
            x -= 1;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            //change vertical direction
            x += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            //change vertical direction
            y += 1;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            //change vertical direction
            y -= 1;
        }

        if (x != 0 || y != 0){
            engine.getCamera().translate(x * 300 * Gdx.graphics.getDeltaTime() , y * 300 * Gdx.graphics.getDeltaTime());
            addVelocity(engine.getEntity(engine.user), x*300*Gdx.graphics.getDeltaTime(), y*300*Gdx.graphics.getDeltaTime());

            if (tickTimer > serverTick){

                sendPosition();
                tickTimer = 0;
            }
        }

        tickTimer++;
        engine.getCamera().update();
    }

    private void sendPosition(){
        data = new Data();
        data.addComponent((TransformComponent) engine.getEntity(engine.user).getComponent(TransformComponent.class));
        data.setId(engine.getEntity(engine.user).id);
        data.setTag(doUpdate);
        giveData(data, Data.class);
    }

    private void updateHost(Data data) {
        if (network.isHost){
            if (data.tag.equals(requestChange)){

                //Update data

                data.setTag(doUpdate);
                hostPkt.add(data);
            }
        }
    }


    private void onUpdate(Data data){
        if(data.tag.equals(doUpdate)){
            e = engine.getEntity(data.entityID);
            if (data.component.getClass() == TransformComponent.class){
                if(!e.name.equals(engine.user))
                    setPosition(e, data.component);
            }
        }

    }

    private void setPosition(Entity e, TransformComponent component) {
        t = (TransformComponent) e.getComponent( TransformComponent.class);
        c = (CollisionComponent) e.getComponent(CollisionComponent.class);

        engine.getSpatialHashGrid().removeEntity(e);
        t.setVec(component.getVector());
        c.followTransform(t);
        engine.getSpatialHashGrid().addEntity(e);
    }


    public void addVelocity(Entity e, float x, float y){

        if (e == null)
            return;
        t = (TransformComponent) e.getComponent( TransformComponent.class);
        c = (CollisionComponent) e.getComponent(CollisionComponent.class);
        v = (VelocityComponent) e.getComponent(VelocityComponent.class);


        engine.getSpatialHashGrid().removeEntity(e);
        v.setVelocity(x, y, 0);
        t.addVelocity(v);
        c.followTransform(t);
        engine.getSpatialHashGrid().addEntity(e);
    }

    @Override
    public Class<?extends NetWorkData> getDataClass(){

        return Data.class;
    }
}

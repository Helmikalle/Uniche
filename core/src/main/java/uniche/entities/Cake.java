package main.java.uniche.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import main.java.uniche.utils.Skaalausta;

import java.util.ArrayList;
import java.util.List;

import static main.java.uniche.HUD.addCupcake;
import static main.java.uniche.HUD.addMango;

public class Cake {

    public Body cake;
    public String id;
    private float mangocounter;
    private float cupcakecpunter;
    private boolean isSetToDestroy = false;
    private boolean isDestroyed = false;
    private float stateTime;

    //LISÄTTY GETTERI isSetToDestroy:lle, jotta kakut katoaa
    public boolean isSetToDestroy() {
        return isSetToDestroy;
    }

    public Cake(World world, String id, float x, float y){
        this.id = id;
        createCake(world,x,y);
    }

    private void createCake(World world, float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation =  true;
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x,y);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10 /2/ Skaalausta.Scaler,10 /2/ Skaalausta.Scaler);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 1.0f;
        fixture.isSensor = true;

        this.cake = world.createBody(bdef);
        this.cake.createFixture(fixture).setUserData(this);

    }
    public void update(World world,float dt){
        stateTime += dt;
        if(isSetToDestroy && !isDestroyed){
            world.destroyBody((Body) cake.getUserData());
            isDestroyed = true;
            stateTime = 0;
        }
    }
    public void poimittu () {
        System.out.println(id + " TRIGGERED");
        if (id.equals("CUPCAKE")) {
            addCupcake();
        } else if (id.equals("MANGO")) {
            addMango();
        }
        isSetToDestroy = true;
    }

    public void kerätty(){
        isDestroyed = true;
    }
}
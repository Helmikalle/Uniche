package main.java.uniche.entities;

import com.badlogic.gdx.physics.box2d.*;
import main.java.uniche.utils.Skaalausta;

public class Cakes {

    public Body cake;
    public String id;
    private float mangocounter;
    private float cupcakecpunter;
    private boolean isSetToDestroy = false;
    private boolean isDestroyed = false;
    private float stateTime;

    public Cakes (World world, String id, float x, float y){
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
        isSetToDestroy = true;
    }

    public void kerätty(){
        isDestroyed = true;
    }
}

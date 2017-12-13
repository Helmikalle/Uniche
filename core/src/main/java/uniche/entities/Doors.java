package main.java.uniche.entities;

import com.badlogic.gdx.physics.box2d.*;
import main.java.uniche.utils.Skaalausta;

public class Doors {

    public Body door;
    public String id;
    private Pony pony;
    public boolean isSetToClose = false;

    public Doors(World world, String id, float x, float y){
        this.id = id;
        createDoor(world,x,y);

    }

    private void createDoor(World world, float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation =  true;
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x,y);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10 /2/ Skaalausta.Scaler,10 /2/ Skaalausta.Scaler);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 1.0f;


        this.door = world.createBody(bdef);
        this.door.createFixture(fixture).setUserData(this);

    }

    public boolean isSetToClose () {
        return true;
    }



}

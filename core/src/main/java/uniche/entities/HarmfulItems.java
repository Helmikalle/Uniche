package main.java.uniche.entities;

import com.badlogic.gdx.physics.box2d.*;
import main.java.uniche.utils.Skaalausta;

public class HarmfulItems {

    public Body cake;
    public String id;

    public HarmfulItems(World world, String id, float x, float y){
        this.id = id;
        createWaste(world,x,y);

    }

    private void createWaste(World world, float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation =  true;
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x/ Skaalausta.Scaler,y/Skaalausta.Scaler);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(64 / Skaalausta.Scaler,64 / Skaalausta.Scaler);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 1.0f;

        this.cake = world.createBody(bdef);
        this.cake.createFixture(fixture).setUserData(this);
    }

    public void poimittu (){
        System.out.println("DAMaGE CONTROL");
    }
}

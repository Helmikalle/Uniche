package main.java.uniche.entities;

import com.badlogic.gdx.physics.box2d.*;
import main.java.uniche.HUD;
import main.java.uniche.utils.Skaalausta;

import static main.java.uniche.HUD.wasteHit;


public class HarmfulItem {

    public Body waste;
    public String id;

    public HarmfulItem(World world, String id, float x, float y){
        this.id = id;
        createWaste(world,x,y);

    }

    private void createWaste(World world, float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation =  true;
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x,y);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(32/ 2 / Skaalausta.Scaler,32/ 2 / Skaalausta.Scaler);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 1.0f;
        fixture.isSensor = true;

        this.waste = world.createBody(bdef);
        this.waste.createFixture(fixture).setUserData(this);
    }

    public void wastePoimittu (){
        System.out.println("DAMaGE CONTROL");
        wasteHit();
    }
}

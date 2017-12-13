package main.java.uniche.entities;

import com.badlogic.gdx.physics.box2d.*;

import static main.java.uniche.utils.Skaalausta.Scaler;

public class Pony {

    public Body pony;
    public String id;

    public Pony (World world, String id, float x, float y){
        this.id = id;
        createPony(world,x,y);
    }
    private void createPony (World world, float x, float y){
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x,y);
        def.fixedRotation= true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(20/2/Scaler,20/2/Scaler);

        FixtureDef fixturePony = new FixtureDef();
        fixturePony.shape = shape;
        fixturePony.density = 1.0f;

        this.pony = world.createBody(def);
        this.pony.createFixture(fixturePony).setUserData(this);

    }
}

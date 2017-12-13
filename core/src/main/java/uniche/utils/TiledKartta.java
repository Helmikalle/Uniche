package main.java.uniche.utils;


import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class TiledKartta  {
    public static void parseTiledMap(World world, MapObjects objects) {
        //Tässä käydää karttatiedosto läpi annetaan layerit "shape":lle -Kalle
        for (MapObject object: objects) {
            Shape shape;
            if (object instanceof PolylineMapObject){
                shape = createPolyline((PolylineMapObject)object);
            }else {
                continue;
            }
            //Parseroidaan kartta missä on eri layereita ja annetaan se maailmalle -Kalle
            Body body;
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            body = world.createBody(bdef);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1.0f;
            fixtureDef.filter.maskBits= 1;
            fixtureDef.filter.groupIndex= 0;
            fixtureDef.filter.categoryBits = 8;
            body.createFixture(fixtureDef);
            shape.dispose();
        }
    }
    private static ChainShape createPolyline (PolylineMapObject polyline){
        float[] vertices = polyline.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length/2];

        for (int i = 0; i < worldVertices.length; i++){
            worldVertices[i] = new Vector2(vertices[i*2]/ Skaalausta.Scaler,vertices[i*2+1]/Skaalausta.Scaler);
        }
        ChainShape cs = new ChainShape();
        cs.createChain(worldVertices);
        return cs;
    }
}

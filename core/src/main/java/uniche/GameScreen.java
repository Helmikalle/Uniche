package main.java.uniche;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import main.java.uniche.entities.*;
import main.java.uniche.utils.ContactHandler;
import main.java.uniche.utils.TiledKartta;

import java.util.ArrayList;
import java.util.List;

import static main.java.uniche.utils.Skaalausta.Scaler;

public class GameScreen implements Screen {
    private static final double DEGREES_TO_RADIANS = (double)(Math.PI/180);
    private World world;
    final MainLauncher game;
    private Texture cupcakeimg,wasteimg,mangocakeimg,oviimg,kytkinimg;
    private OrthographicCamera camera;
    private Pony pony;
    private Animation animation;
    private float timePassed = 0;
    private TextureAtlas poniAtlasYlos,poniAtlasAlas,poniAtlasVasen,poniAtlasOikea;
    private Box2DDebugRenderer b2Render;
    private OrthogonalTiledMapRenderer tmr;
    private TiledMap tiledMap;
    private RayHandler rayHandler;
    private ConeLight horn;
    private HUD hud;
    private List<Cake> cakeList;
    private List<HarmfulItem> wasteList;
    private InvisLever lever,lever2,lever3,stageLever,stageComplete;
    private Door doorObj,doorObj2,doorObj3;



    public GameScreen(final MainLauncher game) {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        this.game = game;
        world = new World(new Vector2(0,0),false);
        this.world.setContactListener(new ContactHandler());

        //Kakkulista
        cakeList = new ArrayList<Cake>();
        cakeList.add(new Cake(world,"CUPCAKE",8,8));
        cakeList.add(new Cake(world,"CUPCAKE",9,8));
        cakeList.add(new Cake(world,"MANGO",8,5));
        cakeList.add(new Cake(world,"MANGO",9,5));

//        cakeList.add(new Cake(world,"CUPCAKE",20,20));
//        cakeList.add(new Cake(world,"CUPCAKE",15,14));
//        cakeList.add(new Cake(world,"CUPCAKE",5,5));

        //Jätetynnyrilista
        wasteList = new ArrayList<HarmfulItem>();
        wasteList.add(new HarmfulItem(world,"WASTEBARREL",8,1.5f));
        wasteList.add(new HarmfulItem(world,"WASTEBARREL",9,1.5f));

        //Unicorn
        pony = new Pony(world,"UNICORN",2,2);

        //OVI + KYTKIN
        lever = new InvisLever(world,"PIILOKYTKIN1",24.5f,36.5f);
        doorObj = new Door(world,"ovi1",lever.lever.getPosition().x ,lever.lever.getPosition().y -1f);
        lever2 = new InvisLever(world,"PIILOKYTKIN2",13.5f,36.5f);
        doorObj2 = new Door(world,"ovi2",lever2.lever.getPosition().x,lever2.lever.getPosition().y-1f);
        lever3 = new InvisLever(world,"PIILOKYTKIN3",26.5f,1.5f);
        doorObj3 = new Door(world,"OVI3",lever3.lever.getPosition().x - 1,lever3.lever.getPosition().y);
        stageLever = new InvisLever(world,"AKTIVOISTAGECOMPLETE" , 39,45.5f);
        stageComplete = new InvisLever(world,"TASOLÄPÄISTY",13.9f,49.3f);

        b2Render = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        hud = new HUD();
        camera.setToOrtho(false, w, h);

        //Kuvan tuontia -Kalle'
        kytkinimg = new Texture(Gdx.files.internal("core/assets/kytkin/kytkin2.png"));
        oviimg = new Texture(Gdx.files.internal("core/assets/ovielem/ovi.png"));
        cupcakeimg = new Texture(Gdx.files.internal("core/assets/kakkukuvia/kuppikakku.png"));
        mangocakeimg = new Texture(Gdx.files.internal("core/assets/kakkukuvia/mangokakku.png"));
        wasteimg = new Texture(Gdx.files.internal("core/assets/ydinjate/ydinjate.png"));
        poniAtlasYlos = new TextureAtlas(Gdx.files.internal("core/assets/ponieteen/poniylos.atlas"));
        poniAtlasAlas = new TextureAtlas(Gdx.files.internal("core/assets/ponitaakse/ponialas.atlas"));
        poniAtlasVasen = new TextureAtlas(Gdx.files.internal("core/assets/ponivasemmalle/ponivasen.atlas"));
        poniAtlasOikea = new TextureAtlas(Gdx.files.internal("core/assets/ponioikealle/ponioikea.atlas"));
        animation = new Animation(1/ 30f, poniAtlasOikea.getRegions());

        //Tuodaan kartta -Kalle
        tiledMap = new TmxMapLoader().load("core/assets/uudetkartat/kolmaskartta.tmx");
        tmr = new OrthogonalTiledMapRenderer(tiledMap);


        //TÄSSÄ TUODAAN TÖRMÄTTÄVÄT REUNAT -Kalle
        TiledKartta.parseTiledMap(world,tiledMap.getLayers()
                .get("objektit").getObjects());

        //TÄMÄ TUO HIMMENNYKSEN
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(.1f);

        //LUODAAN ETEENPÄIN NÄYTTÄVÄ VALO
        horn = new ConeLight(rayHandler,120,Color.WHITE,8,0,0,pony.pony.getAngle(),60);
        horn.setSoftnessLength(0f);
        horn.attachToBody(pony.pony);
        horn.setContactFilter((short)1,(short)0,(short)8);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        int i = 0;
        //Tässä piirtää tavaraa ruudulle -Kalle
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tmr.render();

        game.batch.begin();

        //TÄSSÄ REGOIVAT/POIMITTAVAT KAKUT + JÄTETYNNYRI
        //WASTE -Titta
        for (HarmfulItem wasteBarrel : wasteList){
            game.batch.draw(wasteimg, wasteBarrel.waste.getPosition().x * Scaler - 16,wasteBarrel.waste.getPosition().y * Scaler -16);
        }
        //KAKUT -Titta
        for (Cake cakeObj : cakeList){
            if (cakeObj.id.equals("CUPCAKE")){
                if(!cakeObj.isSetToDestroy()) game.batch.draw(cupcakeimg,cakeObj.cake.getPosition().x * Scaler -16,cakeObj.cake.getPosition().y * Scaler -16);
            } else if (cakeObj.id.equals("MANGO")){
                if(!cakeObj.isSetToDestroy()) game.batch.draw(mangocakeimg, cakeObj.cake.getPosition().x * Scaler - 16, cakeObj.cake.getPosition().y * Scaler -16);
            }
        }
        //TÄSSÄ TUODAAN OVET KUN KÄVELLÄÄN LÄPI OVIAUKOSTA -Kalle
        if(lever.isSetToClose() || lever2.isSetToClose()||lever3.isSetToClose()) {
            game.batch.draw(oviimg, doorObj.door.getPosition().x * Scaler - 16,
                    doorObj.door.getPosition().y * Scaler - 16);
            game.batch.draw(oviimg,doorObj2.door.getPosition().x * Scaler -16,
                    doorObj2.door.getPosition().y * Scaler -16);
            game.batch.draw(oviimg,doorObj3.door.getPosition().x * Scaler - 16,
                    doorObj3.door.getPosition().y * Scaler -16);

        }
        game.batch.draw(kytkinimg,stageLever.lever.getPosition().x * Scaler -14,stageLever.lever.getPosition().y * Scaler -14);
        game.batch.end();

        //TUODAAN VALO "horn" PONILLE
        rayHandler.render();

        //TULOSTETAAN PONI ERIKSEEN KOSKA VALO
        game.batch.begin();
        game.batch.draw((TextureRegion) animation.getKeyFrame(timePassed, true),
                pony.pony.getPosition().x * Scaler  - 16, pony.pony.getPosition().y * Scaler - 16);
        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        hud.stage.act();


        //TÄSSÄ TYÖKALU jOLLA SAA TÖRMÄYSVIIVAT NÄKYVIIN
//        b2Render.render(world,camera.combined.scl(Scaler));

        update(Gdx.graphics.getDeltaTime());

    }

    //Kamera seuraa ponia -Kalle
    public void cameraUpdate(float delta) {
        Vector3 position = camera.position;
        position.x = pony.pony.getPosition().x * Scaler;
        position.y = pony.pony.getPosition().y * Scaler;
        camera.position.set(position);

        camera.update();
    }

    public void update(float delta) {

        world.step(1/60f,6,2);
        //LISÄTTY if-lauseet, jotta kakut katoaa
        for (Cake cakeObj : cakeList){
            if(cakeObj.isSetToDestroy()) cakeObj.cake.setActive(false);
        }
        if (stageLever.isSetToClose()){
            stageComplete.lever.setActive(true);
        }else {
            stageComplete.lever.setActive(false);
        }
        if (stageComplete.isSetToClose()){
            game.setScreen(new GameOverScreen(game));
        }
        if(lever.isSetToClose() || lever2.isSetToClose() || lever3.isSetToClose()) {
            doorObj.door.setActive(true); doorObj2.door.setActive(true); doorObj3.door.setActive(true);
        }else{
            doorObj.door.setActive(false);doorObj2.door.setActive(false);doorObj3.door.setActive(false);
        }
        rayHandler.update();
        inputUpdate(delta);
        gameOver();
        exitGame();
        world.clearForces();
        cameraUpdate(delta);
        game.batch.setProjectionMatrix(camera.combined);
        rayHandler.setCombinedMatrix(camera.combined.cpy().scl(Scaler));
        tmr.setView(camera);
    }

    //PONI LIIKKUU TÄÄLTÄ NYKYÄÄN + Input toiminnallisuudet. Lisätty vielä bodyn kääntyminen jotta saatiin
    // Cone Light toimimaan -Kalle
    public void inputUpdate(float delta) {
        float angle;
        int horizontalForce = 0;
        int verticalForce =0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            horizontalForce -=1;
            timePassed = 100 * Gdx.graphics.getDeltaTime();
            animation = new Animation(1 / 30f, poniAtlasVasen.getRegions());
            pony.pony.setTransform(pony.pony.getWorldCenter(), angle = (float) (180*DEGREES_TO_RADIANS));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            horizontalForce +=1;
            timePassed = 100 * Gdx.graphics.getDeltaTime();
            animation = new Animation(1 / 30f, poniAtlasOikea.getRegions());
            pony.pony.setTransform(pony.pony.getWorldCenter(), angle = (float) (360*DEGREES_TO_RADIANS));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            verticalForce +=1;
            timePassed = 100 *  Gdx.graphics.getDeltaTime();
            animation = new Animation(1 / 30f, poniAtlasYlos.getRegions());
            pony.pony.setTransform(pony.pony.getWorldCenter(), angle = (float) (90*DEGREES_TO_RADIANS));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            verticalForce -=1;
            timePassed = 100* Gdx.graphics.getDeltaTime();
            animation = new Animation(1 / 30f, poniAtlasAlas.getRegions());
            pony.pony.setTransform(pony.pony.getWorldCenter(), angle = (float) (270*DEGREES_TO_RADIANS));
        }

        pony.pony.setLinearVelocity(verticalForce * 5,pony.pony.getLinearVelocity().y);
        pony.pony.setLinearVelocity(horizontalForce * 5,pony.pony.getLinearVelocity().x);


        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.DOWN) ||
                Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
            hud.reduceHealth();

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false,
                width/2 , height/2);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        b2Render.dispose();
        world.dispose();
        tmr.dispose();
        tiledMap.dispose();
        rayHandler.dispose();


    }

    //Peli loppuu ja siirtyy "Game over"-näkymään, jos health bar tyhjenee -Titta
    private void gameOver(){
        if (hud.getHealth() <= 0){
            game.setScreen(new GameOverScreen(game));}
    }
    //Peli voidaan keskeyttää painamalla esc:iä -Titta
    private void exitGame(){
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            game.setScreen(new MainMenuScreen(game));
        }
    }

}

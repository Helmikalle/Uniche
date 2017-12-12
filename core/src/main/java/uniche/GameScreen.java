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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import main.java.uniche.entities.Cakes;
import main.java.uniche.entities.HarmfulItems;
import main.java.uniche.entities.Pony;
import main.java.uniche.utils.ContactHandler;
import main.java.uniche.utils.TiledKartta;

import static main.java.uniche.utils.Skaalausta.Scaler;

public class GameScreen implements Screen {
    private static final double DEGREES_TO_RADIANS = (double)(Math.PI/180);
    private World world;
    final MainLauncher game;
    private Texture cupcakeimg,wasteimg,mangocakeimg;
    private OrthographicCamera camera;
    private Pony pony;
    private Animation animation;
    private float timePassed = 0;
    private TextureAtlas poniAtlasYlos,poniAtlasAlas,poniAtlasVasen,poniAtlasOikea;
    private int cupcakeCounter = 0;
    private int healthBar = 4000;
    private Box2DDebugRenderer b2Render;
    private OrthogonalTiledMapRenderer tmr;
    private TiledMap tiledMap;
    private Rectangle lever,door;
    private RayHandler rayHandler;
    private ConeLight horn;
    private Cakes cupcakeObj,mangocakeObj;
    private HarmfulItems wasteBarrel;
    private HUD hud;


    public GameScreen(final MainLauncher game) {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        this.game = game;
        world = new World(new Vector2(0,0),false);
        this.world.setContactListener(new ContactHandler());
        cupcakeObj = new Cakes(world,"CUPCAKE",8,8);
        mangocakeObj = new Cakes(world,"MANGO",8,5);
        pony = new Pony(world,"UNICORN",2,2);
        wasteBarrel = new HarmfulItems(world,"WASTEBARREL",8,1.5f);

        b2Render = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        hud = new HUD();
        camera.setToOrtho(false, w, h);

        //Kuvan tuontia -Kalle'
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


        lever = new Rectangle();
        lever.x = 300;
        lever.y = 150;
        lever.width = 32/2;
        lever.height = 32/2;

        door = new Rectangle();
        door.x = 360;
        door.y = 150;
        door.width = 32/2;
        door.height = 32/2;


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

//        leverChange();
//        completeLevel();
//        setBorders();
        game.batch.begin();
        if (healthBar <= 0) {
            game.setScreen(new GameOverScreen(game));
        }
        //Nää pitäis saada ruutuun kiinni varmaan mielummin ku poniin -Kalle
        game.font.draw(game.batch, "CUPCAKES: ", pony.pony.getPosition().x - 185, pony.pony.getPosition().y + 110);
        game.font.draw(game.batch, String.valueOf(cupcakeCounter), pony.pony.getPosition().x - 95, pony.pony.getPosition().y + 110);
        game.font.draw(game.batch, "HEALTH: ", pony.pony.getPosition().x - 185, pony.pony.getPosition().y + 90);
        game.font.draw(game.batch, String.valueOf(healthBar), pony.pony.getPosition().x - 120, pony.pony.getPosition().y + 90);

        //TÄSSÄ REGOIVAT/POIMITTAVAT KAKUT + JÄTETYNNYRI
        game.batch.draw(wasteimg, wasteBarrel.waste.getPosition().x * Scaler - 16,wasteBarrel.waste.getPosition().y * Scaler -16);
        game.batch.draw(mangocakeimg, mangocakeObj.cake.getPosition().x * Scaler - 16, mangocakeObj.cake.getPosition().y * Scaler -16);
        game.batch.draw(cupcakeimg,cupcakeObj.cake.getPosition().x * Scaler -16,cupcakeObj.cake.getPosition().y * Scaler -16);
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
        update(Gdx.graphics.getDeltaTime());

        //TÄSTÄ SAA COLLISION LAYERIT NÄKYMÄÄN
//        b2Render.render(world,camera.combined.scl(Scaler));

        // kysely random kuppikakuista ei tule itse peliin - Kalle
        //MUTTA täällä myös healthbarin ja cupcakeCounterin toiminnallisuus - Titta
        //Pistin vähän lisää healthbariin elämää :D ja lisäsin toiminnallisuuden
        // et joka keystrokesta lähtee elämää -Kalle
//        if (TimeUtils.nanoTime() - lastDropTime > 1000000000 && i < 3) spawnRaindrop();
//        Iterator<Body> iter = raindrops.iterator();
//        while (iter.hasNext()) {
//            Body raindrop = iter.next();
//            if (raindrop.overlaps(pony)) {
//                iter.remove();
//                cupcakeCounter += 5;
//                healthBar = healthBar + 100;0
//                if (healthBar <= 0) {
//                    game.setScreen(new GameOverScreen(game));
//                }
//            }
//        }
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
        game.batch.dispose();
        tmr.dispose();
        tiledMap.dispose();
        rayHandler.dispose();


    }
    //Kytkimen painaminen  -Titta
//    private void leverChange(){
//        if (lever.(pony)){
//            exitLevel = true;
//        }
//    }
//
//    //Tason päättyminen -Titta
//    private void completeLevel(){
//        if (door.overlaps(pony) && exitLevel == true){
//            game.setScreen(new NextLevelScreen(game));
//        }
//    }

    //Peli loppuu ja siirtyy "Game over"-näkymään, jos health bar tyhjenee -Titta
    private void gameOver(){
        if (healthBar <= 0){
            game.setScreen(new GameOverScreen(game));}
    }
    //Peli voidaan keskeyttää painamalla esc:iä -Titta
    private void exitGame(){
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            game.setScreen(new MainMenuScreen(game));
        }
    }

    //Asetettu rajat ettei poni mene ulos ruudusta  -Kalle
    // Laitoin omaan metodiin -Titta
//    private void setBorders(){
//        if (pony.x < 16) pony.x = 16;
//        if (pony.x > 800-32 ) pony.x = 800 - 32;
//        if (pony.y < 16 ) pony.y = 16;
//        if (pony.y > 480 - 200) pony.y = 480 - 200;
//
//    }
    //Randomina spawnaa kuppikakkua ympäriinsä. Ei tule itse peliin -Kalle
//    private void spawnRaindrop() {
//        Body raindrop = new Body();
//        raindrop.x = MathUtils.random(0, 800 - 150);
//        raindrop.y = MathUtils.random(0, 480 - 200);
//        raindrop.width = 32;
//        raindrop.height = 32;
//        raindrops.add(raindrop);
//        lastDropTime = TimeUtils.nanoTime();
//    }

}

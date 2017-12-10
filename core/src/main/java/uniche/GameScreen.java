package main.java.uniche;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import main.java.uniche.utils.TiledKartta;

import static main.java.uniche.utils.Skaalausta.Scaler;

public class GameScreen implements Screen {
    private World world;
    final MainLauncher game;
    private Texture cupcakeimg,wasteimg,mangocakeimg;
    private OrthographicCamera camera;
    private Body pony;
    private Array<Body> raindrops;
    private long lastDropTime;
    private Animation animation;
    private float timePassed = 0;
    private TextureAtlas poniAtlasYlos,poniAtlasAlas,poniAtlasVasen,poniAtlasOikea;
    private int cupcakeCounter = 0;
    private int healthBar = 4000;
    private Box2DDebugRenderer b2Render;
    private OrthogonalTiledMapRenderer tmr;
    private TiledMap tiledMap;
    private Rectangle lever,door;


    public GameScreen(final MainLauncher game) {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        this.game = game;
        world = new World(new Vector2(0,0),false);
        b2Render = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);

        //Kuvan tuontia -Kalle'
        cupcakeimg = new Texture(Gdx.files.internal("core/assets/kakkukuvia/kuppikakku.png"));
        mangocakeimg = new Texture(Gdx.files.internal("core/assets/kakkukuvia/mangokakku.png"));
        wasteimg = new Texture(Gdx.files.internal("core/assets/ydinjate/ydinjate.png"));
        poniAtlasYlos = new TextureAtlas(Gdx.files.internal("core/assets/ponieteen/poniylos.atlas"));
        poniAtlasAlas = new TextureAtlas(Gdx.files.internal("core/assets/ponitaakse/ponialas.atlas"));
        poniAtlasVasen = new TextureAtlas(Gdx.files.internal("core/assets/ponivasemmalle/ponivasen.atlas"));
        poniAtlasOikea = new TextureAtlas(Gdx.files.internal("core/assets/ponioikealle/ponioikea.atlas"));
        animation = new Animation(1/ 30f, poniAtlasYlos.getRegions());


        tiledMap = new TmxMapLoader().load("core/assets/uudetkartat/kolmaskartta.tmx");
        tmr = new OrthogonalTiledMapRenderer(tiledMap);
        pony = createPony();

        //TÄTÄ TARVITAAN SITTEN KUN ON KARTTA KUNNOSSA JA ON JOTAIN TÖRMÄTTÄVIÄ REUNOJA
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

        //        raindrops = new Array<Body>();
        //        spawnRaindrop();

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
        update(Gdx.graphics.getDeltaTime());
        b2Render.render(world,camera.combined.scl(Scaler));


//        leverChange();
//        completeLevel();

        game.batch.begin();
        if (healthBar <= 0) {
            game.setScreen(new GameOverScreen(game));
        }
        //Nää pitäis saada ruutuun kiinni varmaan mielummin ku poniin -Kalle
        game.font.draw(game.batch, "CUPCAKES: ", pony.getPosition().x - 185, pony.getPosition().y + 110);
        game.font.draw(game.batch, String.valueOf(cupcakeCounter), pony.getPosition().x - 95, pony.getPosition().y + 110);
        game.font.draw(game.batch, "HEALTH: ", pony.getPosition().x - 185, pony.getPosition().y + 90);
        game.font.draw(game.batch, String.valueOf(healthBar), pony.getPosition().x - 120, pony.getPosition().y + 90);
        game.batch.draw(mangocakeimg, lever.x, lever.y);
        game.batch.draw(wasteimg, door.x, door.y);

        game.batch.draw((TextureRegion) animation.getKeyFrame(timePassed, true),
                pony.getPosition().x * Scaler  - 16, pony.getPosition().y * Scaler - 16);
//        for (Body raindrop : raindrops) {
//            game.batch.draw(cupcakeimg, raindrop.getPosition().x, raindrop.getPosition().y);
//            ++i;
//        }

        game.batch.end();
//        setBorders();

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
//                healthBar = healthBar + 100;
//                if (healthBar <= 0) {
//                    game.setScreen(new GameOverScreen(game));
//                }
//            }
//        }
    }

    //Kamera seuraa ponia -Kalle
    public void cameraUpdate(float delta) {
        Vector3 position = camera.position;
        position.x = pony.getPosition().x * Scaler;
        position.y = pony.getPosition().y * Scaler;
        camera.position.set(position);

        camera.update();
    }

    public void update(float delta) {
        world.step(1/60f,6,2);
        inputUpdate(delta);
        gameOver();
        exitGame();
        world.clearForces();
        cameraUpdate(delta);
        game.batch.setProjectionMatrix(camera.combined);
        tmr.setView(camera);
    }

    //PONI LIIKKUU TÄÄLTÄ NYKYÄÄN + Input toiminnallisuudet -Kalle
    public void inputUpdate(float delta) {
        int horizontalForce = 0;
        int verticalForce =0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            horizontalForce -=1;
            timePassed = 100 * Gdx.graphics.getDeltaTime();
            animation = new Animation(1 / 30f, poniAtlasVasen.getRegions());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            horizontalForce +=1;
            timePassed = 100 * Gdx.graphics.getDeltaTime();
            animation = new Animation(1 / 30f, poniAtlasOikea.getRegions());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            verticalForce +=1;
            timePassed = 100 *  Gdx.graphics.getDeltaTime();
            animation = new Animation(1 / 30f, poniAtlasYlos.getRegions());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            verticalForce -=1;
            timePassed = 100* Gdx.graphics.getDeltaTime();
            animation = new Animation(1 / 30f, poniAtlasAlas.getRegions());
        }
        pony.setLinearVelocity(verticalForce * 5,pony.getLinearVelocity().y);
        pony.setLinearVelocity(horizontalForce * 5,pony.getLinearVelocity().x);


        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.DOWN) ||
                Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
            healthBar -= 1;

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


    }
    // Ponin koko ja aloitus sijainti määritelty  -Kalle
    public Body createPony () {
        Body pony;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(2,2);
        def.fixedRotation= true;
        pony = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(28/2/Scaler,28/2/Scaler);
        pony.createFixture(shape,0.0f);
        shape.dispose();
        return pony;
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

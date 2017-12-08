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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
    final MainLauncher game;
    private Texture cupcakeimg;
    private Texture mangocakeimg;
    private Texture wasteimg;
    private OrthographicCamera camera;
    private Rectangle pony;
    private Rectangle lever;
    private Rectangle door;
    private Array<Rectangle> raindrops;
    private long lastDropTime;
    private Animation animation;
    private float timePassed = 1;
    private TextureAtlas poniAtlasYlos;
    private TextureAtlas poniAtlasAlas;
    private TextureAtlas poniAtlasVasen;
    private TextureAtlas poniAtlasOikea;
    public int cupcakeCounter = 0;
    private int healthBar = 1000;
    private boolean exitLevel = false;

    public GameScreen(final MainLauncher game) {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        this.game = game;

        //Kuvan tuontia -Kalle'

        cupcakeimg = new Texture(Gdx.files.internal("core/assets/kakkukuvia/kuppikakku.png"));
        mangocakeimg = new Texture(Gdx.files.internal("core/assets/kakkukuvia/mangokakku.png"));
        wasteimg = new Texture(Gdx.files.internal("core/assets/ydinjate/ydinjate.png"));
        poniAtlasYlos = new TextureAtlas(Gdx.files.internal("core/assets/ponieteen/poniylos.atlas"));
        poniAtlasAlas = new TextureAtlas(Gdx.files.internal("core/assets/ponitaakse/ponialas.atlas"));
        poniAtlasVasen = new TextureAtlas(Gdx.files.internal("core/assets/ponivasemmalle/ponivasen.atlas"));
        poniAtlasOikea = new TextureAtlas(Gdx.files.internal("core/assets/ponioikealle/ponioikea.atlas"));
        animation = new Animation(3/2f,poniAtlasYlos.getRegions());

        //Kameran zoom määritelty
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w/2, h/2);

        // Ponin koko ja aloitus sijainti määritelty  -Kalle
        pony = new Rectangle();
        pony.x = 800 / 2 - 64 / 2;
        pony.y = 0;
        pony.width = 32/2;
        pony.height = 32/2;

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

        raindrops = new Array<Rectangle>();
        spawnRaindrop();

    }
        //Randomina spawnaa kuppikakkua ympäriinsä. Ei tule itse peliin -Kalle
    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0,800 -150);
        raindrop.y = MathUtils.random(0,480 - 200);
        raindrop.width = 32;
        raindrop.height = 32;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void show() {

    }

    @Override
    public void render (float delta) {
        update(Gdx.graphics.getDeltaTime());
        int i = 0;
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            //Tässä piirtää tavaraa ruudulle -Kalle
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        gameOver();
        leverChange();
        completeLevel();
            //Nää pitäis saada ruutuun kiinni varmaan mielummin ku poniin -Kalle
        game.font.draw(game.batch, "CUPCAKES: ", pony.x - 185, pony.y + 110);
        game.font.draw(game.batch, String.valueOf(cupcakeCounter), pony.x - 95, pony.y + 110);
        game.font.draw(game.batch, "HEALTH: ", pony.x - 185, pony.y + 90);
        game.font.draw(game.batch, String.valueOf(healthBar), pony.x - 120, pony.y + 90);
        game.batch.draw(mangocakeimg, lever.x, lever.y);
        game.batch.draw(wasteimg, door.x, door.y);
        game.batch.draw((TextureRegion) animation.getKeyFrame(timePassed,true), pony.x, pony.y);
        for (Rectangle raindrop: raindrops) {
            game.batch.draw(cupcakeimg, raindrop.x, raindrop.y);
            ++i;
        }

        game.batch.flush();
        game.batch.end();

        setBorders();
        exitGame();

            // kysely random kuppikakuista ei tule itse peliin - Kalle
            //MUTTA täällä myös healthbarin ja cupcakeCounterin toiminnallisuus - Titta
                //Pistin vähän lisää healthbariin elämää :D ja lisäsin toiminnallisuuden
                // et joka keystrokesta lähtee elämää -Kalle
        if(TimeUtils.nanoTime() - lastDropTime > 1000000000 && i < 3) spawnRaindrop() ;
        Iterator<Rectangle> iter = raindrops.iterator();
        while(iter.hasNext()){
            Rectangle raindrop = iter.next();
            if (raindrop.overlaps(pony)){
                iter.remove();
                cupcakeCounter++;
                healthBar = healthBar + 100;
                if (healthBar <= 0){
                    game.setScreen(new GameOverScreen(game));
                }

            }
        }
    }
    //Kytkimen painaminen  -Titta
    private void leverChange(){
        if (lever.overlaps(pony)){
            exitLevel = true;
        }
    }

    //Tason päättyminen -Titta
    private void completeLevel(){
        if (door.overlaps(pony) && exitLevel == true){
            game.setScreen(new NextLevelScreen(game));
        }
    }

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
    private void setBorders(){
        if (pony.x < 16) pony.x = 16;
        if (pony.x > 800-32 ) pony.x = 800 - 32;
        if (pony.y < 16 ) pony.y = 16;
        if (pony.y > 480 - 200) pony.y = 480 - 200;

    }

    //Kamera seuraa ponia -Kalle
    public void cameraUpdate (float delta){
        Vector3 position = camera.position;
        position.x = pony.getX();
        position.y = pony.getY();
        camera.position.set(position);

        camera.update();
    }

    public void update(float delta) {
        cameraUpdate(delta);
        inputUpdate(delta);
    }

            //PONI LIIKKUU TÄÄLTÄ NYKYÄÄN + Input toiminnallisuudet -Kalle
    public void inputUpdate(float delta){

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            timePassed = pony.x -= 200* Gdx.graphics.getDeltaTime();
            animation = new Animation(3/2f,poniAtlasVasen.getRegions());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            timePassed = pony.x += 200* Gdx.graphics.getDeltaTime();
            animation = new Animation(3/2f,poniAtlasOikea.getRegions());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            timePassed = pony.y += 200* Gdx.graphics.getDeltaTime();
            animation = new Animation(3/2f,poniAtlasYlos.getRegions());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            timePassed = pony.y -= 200* Gdx.graphics.getDeltaTime();
            animation = new Animation(3/2f,poniAtlasAlas.getRegions());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)||Gdx.input.isKeyPressed(Input.Keys.DOWN)||
                Gdx.input.isKeyPressed(Input.Keys.RIGHT)||Gdx.input.isKeyPressed(Input.Keys.LEFT))
            healthBar-=1;

    }
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false,width/2,height/2);
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
    public void dispose () {
        game.batch.dispose();



    }
}

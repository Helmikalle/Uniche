package main.java.uniche;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    SpriteBatch batch;
    Texture cupcakeimg;
    private Texture ponyImage;
    private OrthographicCamera camera;
    private Rectangle pony;
    private Array<Rectangle> raindrops;
    private long lastDropTime;
    private Animation animation;
    private TextureAtlas poniAtlas;
    private float timePassed = 0;
    private String path = "core/assets/ponijuoksee.atlas";



    public GameScreen(final MainLauncher game) {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        this.game = game;

        //Kuvan tuontia -Kalle
        cupcakeimg = new Texture(Gdx.files.internal("core/assets/kakkukuvia/kuppikakku.png"));
        ponyImage = new Texture(Gdx.files.internal("core/assets/poninkuvia/ponisprite.png"));
        poniAtlas = new TextureAtlas(Gdx.files.internal(path));
        animation = new Animation(3/2f,poniAtlas.getRegions());

        //Kameran zoom määritelty
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w/2, h/2);

        // Ponin koko ja aloitus sijainti määritelty  -Kalle
        pony = new Rectangle();
        pony.x = 800 / 2 - 64 / 2;
        pony.y = 0;
        pony.width = 32;
        pony.height = 32;

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

    public void setPath(String path) {
        this.path = path;
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
        game.batch.draw((TextureRegion) animation.getKeyFrame(timePassed,true), pony.x, pony.y);
        for (Rectangle raindrop: raindrops) {
            game.batch.draw(cupcakeimg, raindrop.x, raindrop.y);
            ++i;
        }
        game.batch.end();



        //Asetettu rajat ettei poni mene ulos ruudusta  -Kalle
        if (pony.x < 0) pony.x = 0;
        if (pony.x > 800-32 ) pony.x = 800 - 32;
        if (pony.y < 0 ) pony.y = 0;
        if (pony.y > 480 - 200) pony.y = 480 - 200;

            // kysely random kuppikakuista ei tule itse peliin - Kalle
        if(TimeUtils.nanoTime() - lastDropTime > 1000000000 && i < 3) spawnRaindrop() ;
        Iterator<Rectangle> iter = raindrops.iterator();
        while(iter.hasNext()){
            Rectangle raindrop = iter.next();
            if (raindrop.overlaps(pony)){
                iter.remove();
            }
        }


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
        suunta(delta);
    }

    //PONI VAIHTAA SUUNTAA (EHKÄ) tarvitaan atlas mappeja
    public void suunta (float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            setPath("core/assets/ponijuoksee.atlas");
            System.out.println("vasen");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            setPath("core/assets/ponijuoksee.atlas");
            System.out.println("oikea");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            setPath("core/assets/ponijuoksee.atlas");
            System.out.println("ylös");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            setPath("core/assets/ponijuoksee.atlas");
            System.out.println("alas");
        }
    }

            //PONI LIIKKUU TÄÄLTÄ NYKYÄÄN
    public void inputUpdate(float delta){

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            pony.x -= 200* Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            pony.x += 200* Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            timePassed = pony.y += 200* Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            pony.y -= 200* Gdx.graphics.getDeltaTime();
        }

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
        batch.dispose();


    }
}

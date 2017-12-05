package main.java.uniche;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
    final MainLauncher game;
    SpriteBatch batch;
    Texture img;
    private Texture bucketImage;
    private OrthographicCamera camera;
    private Rectangle bucket;
    private Array<Rectangle> raindrops;
    private long lastDropTime;

    public GameScreen(final MainLauncher game) {
        this.game = game;
        bucketImage = new Texture(Gdx.files.internal("core/assets/pixil-layer-Background(2).png"));

        //Ikkunan koko määritelty
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // Ponin koko ja reuna-arvot määritelty
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2;
        bucket.y = 0;
        bucket.width = 200;
        bucket.height = 200;

        raindrops = new Array<Rectangle>();
        spawnRaindrop();

    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0,800 -150);
        raindrop.y = MathUtils.random(0,480 - 200);
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void show() {

    }

    @Override
    public void render (float delta) {
        int i = 0;
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();


        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(bucketImage, bucket.x, bucket.y);
        for (Rectangle raindrop: raindrops) {
            game.batch.draw(bucketImage, raindrop.x, raindrop.y);
            ++i;
        }
        game.batch.end();

        //PONI LIIKKUU! //////////////////////////////Tämä numero kertoo nopeuden
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 400* Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 400* Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) bucket.y += 400* Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) bucket.y -= 400* Gdx.graphics.getDeltaTime();

        //Asetettu rajat ettei poni mene ulos ruudusta
        if (bucket.x < 0) bucket.x = 0;
        if (bucket.x > 800 - 150 ) bucket.x = 800 -150 ;
        if (bucket.y < -64 ) bucket.y = -64;
        if (bucket.y > 480 - 200) bucket.y = 480 - 200;


        if(TimeUtils.nanoTime() - lastDropTime > 1000000000 && i < 3) spawnRaindrop() ;
        Iterator<Rectangle> iter = raindrops.iterator();
        while(iter.hasNext()){
            Rectangle raindrop = iter.next();
            if (raindrop.overlaps(bucket)){
                iter.remove();
            }
        }


    }

    @Override
    public void resize(int width, int height) {

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
        img.dispose();
    }
}

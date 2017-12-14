package main.java.uniche;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.awt.*;

//Menu-luokka
public class MainMenuScreen implements Screen {
    final MainLauncher game;
    private Stage stage;
    OrthographicCamera camera;
    Texture cupcakeimg;
    private Music music;
    private int currentOption = 0;
    private String[] options = {
            "START",
            "QUIT"};
    private ShapeRenderer shapeRenderer;
    //private Skin skin;

    //private float progress;

//liikutaan menussa käyttänen nuolinpääimiä ja viereen kuppikakkukuva
    public MainMenuScreen(final MainLauncher game) {
        this.game = game;
        this.shapeRenderer = new ShapeRenderer();
        //this.progress = 0f;

        music = Gdx.audio.newMusic(Gdx.files.internal("core/assets/musiikki/rolemusi_-_05_-_05_rolemusic_-_the_black_frame.mp3"));
        music.play();
        music.setLooping(true);
        cupcakeimg = new Texture(Gdx.files.internal("core/assets/kakkukuvia/kuppikakku.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

    }

    @Override
    public void show() {
        /*Gdx.input.setInputProcessor(stage);

        this.skin = new Skin();
        this.skin.addRegions(new TextureAtlas(Gdx.files.internal("core/assets/skins/uiskin.atlas")));
        this.skin.add("default-font", game.font22);
        this.skin.load(Gdx.files.internal("core/assets/skins/uiskin.json"));

        inintButtons();*/



    }

    public void update(float delta) {

        if (game.assets.update()) {
            game.setScreen(new LogoScreen(game));
        }

        //progress = game.assets.getProgress();
    }


    //piiretään menuun valintanäppäimet
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(102/255f, 4/255f, 4/255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        handleInput();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        changeOption();
        game.font36.draw(game.batch, "CHERNOBYL UNICORN ", 335, 300);
        game.font22.draw(game.batch, "START", 390, 250);
        game.font22.draw(game.batch, "QUIT", 395, 200);
        game.batch.end();
    }

    public void changeOption(){
        if(currentOption == 0) {
            game.batch.draw(cupcakeimg, 340, 235);
        }
        if (currentOption == 1) {
            game.batch.draw(cupcakeimg,340, 185);

        } else if (currentOption == 2){
            game.batch.draw(cupcakeimg, 340, 135);
        }
    }
    //Mahdollistetaan valinnanmuutos ja itse valinta
    public void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && currentOption < options.length - 1) {
            currentOption++;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP) && currentOption > 0) {
            currentOption--;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            selectOption();
        }
    }
    //Valinnan aiheuttama tapahtuma
    public void selectOption() {
        if(currentOption == 0) {
            music.stop();
            game.setScreen(new GameScreen(game));
        }

        if(currentOption == 1) {
            System.exit(0);
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
    public void dispose() {
        shapeRenderer.dispose();
        music.dispose();

    }
    /*private void inintButtons() {

    }*/
}

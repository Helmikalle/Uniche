package main.java.uniche;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

//Menu-luokka
public class MainMenuScreen implements Screen {
    final MainLauncher game;
    OrthographicCamera camera;
    Texture cupcakeimg;
    private int currentOption = 0;
    private String[] options = {
            "START",
            "QUIT"};

//liikutaan menussa käyttänen nuolinpääimiä ja viereen kuppikakkukuva
    public MainMenuScreen(final MainLauncher game) {
        this.game = game;
        cupcakeimg = new Texture(Gdx.files.internal("core/assets/kakkukuvia/kuppikakku.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void show() {

    }
//piiretään menuun valintanäppäimet
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        handleInput();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        changeOption();
        game.font.draw(game.batch, "CHERNOBYL UNICORN ", 335, 300);
        game.font.draw(game.batch, "START", 390, 250);
        game.font.draw(game.batch, "QUIT", 395, 200);
        game.batch.end();
    }

    public void changeOption(){
        if(currentOption == 0){
            game.batch.draw(cupcakeimg, 340, 235);
        } else if (currentOption == 1){
            game.batch.draw(cupcakeimg, 340, 185);
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

    }
}

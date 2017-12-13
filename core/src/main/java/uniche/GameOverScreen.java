package main.java.uniche;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

//Game over -luokka
public class GameOverScreen implements Screen {

    final MainLauncher game;
    OrthographicCamera camera;
    Texture cupcakeimg;
    private int currentOption = 0;
    private String[] options = {
            "START",
            "QUIT"};

    public GameOverScreen(final MainLauncher game) {
        this.game = game;
        cupcakeimg = new Texture(Gdx.files.internal("core/assets/kakkukuvia/kuppikakku.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        handleInput();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.font22.draw(game.batch, "GAME OVER", 335, 300);
        game.font22.draw(game.batch, "NEW GAME", 390, 250);
        game.font22.draw(game.batch, "QUIT", 395, 200);
        changeOption();

        game.batch.end();
    }

    public void changeOption(){
        if(currentOption == 0){
            game.batch.draw(cupcakeimg, 340, 235);
        } else if (currentOption == 1){
            game.batch.draw(cupcakeimg, 340, 185);
        }
    }

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

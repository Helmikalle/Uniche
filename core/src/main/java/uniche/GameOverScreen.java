package main.java.uniche;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

//Game over -luokka
public class GameOverScreen implements Screen {

    final MainLauncher game;
    OrthographicCamera camera;
    private Texture cupcakeimg;
    private Music music;
    private int currentOption = 0;
    private String[] options = {
            "START",
            "QUIT"};

    public GameOverScreen(final MainLauncher game) {
        this.game = game;
        cupcakeimg = new Texture(Gdx.files.internal("core/assets/kakkukuvia/kuppikakku.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        music = Gdx.audio.newMusic(Gdx.files.internal("core/assets/musiikki/mathgrant_-_06_-_The_Uneasy_Town.mp3"));
        music.play();
        music.setLooping(true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(227/255f, 151/255f, 198/255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        handleInput();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.font36.draw(game.batch, "GAME OVER", 305, 330);
        game.font22.draw(game.batch, "NEW GAME", 375, 250);
        game.font22.draw(game.batch, "QUIT", 375, 200);
        changeOption();

        game.batch.end();
    }

    public void changeOption(){
        if(currentOption == 0){
            game.batch.draw(cupcakeimg, 335, 231);
        } else if (currentOption == 1){
            game.batch.draw(cupcakeimg, 335, 181);
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
        music.dispose();

    }
}

package main.java.uniche;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

//import static main.java.uniche.LogoScreen.music;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

//tehdään tutorialscreeni, joka käyttää kuvaa tuotoriaalina -sonja

public class TutorialScreen implements Screen{

    MainLauncher game;
    private Stage stage;
    private Image tutoriaalikuva;
    private Music music;

    public TutorialScreen (final MainLauncher game) {
        this.game = game;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        music = Gdx.audio.newMusic(Gdx.files.internal("core/assets/musiikki/rolemusi_-_05_-_05_rolemusic_-_the_black_frame.mp3"));
        music.play();
        music.setLooping(true);

        Texture splashTex = new Texture(Gdx.files.internal("core/assets/tutorial/unichetutorial.png"));
        tutoriaalikuva = new Image(splashTex);
        tutoriaalikuva.setOrigin(tutoriaalikuva.getWidth() / 2, tutoriaalikuva.getHeight() / 2);
        stage.addActor(tutoriaalikuva);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(102/255f, 4/255f, 4/255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
        valinta();
        stage.draw();

    }
    public void update (float delta) {
        stage.act(delta);
    }

    public void valinta() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            music.stop();
            game.setScreen(new MainMenuScreen(game));
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
        stage.dispose();
        music.dispose();
    }
}

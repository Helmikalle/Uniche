package main.java.uniche;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

//luokka oltava staattisena mukana, jottei aina tarvitse hakea koko pätkää uudestaan -sonja

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;


public class LogoScreen implements Screen {

    MainLauncher app;
    private Stage stage;
    private Image splashImg;
    private int valinta;

    public LogoScreen (final MainLauncher app) {
        this.app = app;
        this.stage = new Stage();

        // sallitaan input-eventit, seurataan, mitä aktorit saavat (key, touch yms). -sonja
        Gdx.input.setInputProcessor(stage);

        Texture splashTex = new Texture(Gdx.files.internal("core/assets/logo/uusiunichee(1).png"));
        splashImg = new Image(splashTex);
        splashImg.setPosition(stage.getHeight() /2 -128, stage.getWidth() / 2-  128);

        stage.addActor(splashImg);

    }

    @Override
    public void show() {
        System.out.println("Show"); //testataan, toimiiko metodi
        splashImg.setPosition(stage.getHeight() /2 -128, stage.getWidth() / 2-  128);
        //luodaan kuvalle fading-animaatin -sonja
        splashImg.addAction(sequence(alpha(0f), parallel(moveBy(100,-80,6f), fadeIn(8f))));

        // splashImg.addAction(fadeIn(3f));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(102/255f, 4/255f, 4/255f, 1f); // glClearColor käyttää värejä asteikolla 0-1, joten kaikki RGB värit pitää jakaa 255:llä -sonja
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
        valinta();
        stage.draw();

        //aina beginin ja endin väliin! -sonja

        app.batch.begin();
        app.font.draw(app.batch, "PRESS SPACE TO CONTINUE", 320,320);
        app.font.draw(app.batch, "PRESS ESC TO EXIT THE GAME", 320,280);

        app.batch.end();

    }
    public void update (float delta) {
        stage.act(delta); //lisää kaikki actit, joita stagella on -sonja
    }


    //Siirrytään päävalikkoon Space-barilla tai suljetaan peli -sonja

    public void valinta() {

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            app.setScreen(new MainMenuScreen(app));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
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
        stage.dispose();


    }
}


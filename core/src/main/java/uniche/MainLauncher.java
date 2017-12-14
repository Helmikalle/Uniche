package main.java.uniche;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.compression.rangecoder.BitTreeEncoder;


public class MainLauncher extends Game {
    public SpriteBatch batch;
    public BitmapFont font22;
    public BitmapFont font36;
    public AssetManager assets;
    public LogoScreen logoScreen;
    public MainMenuScreen mainMenuScreen;


    public void create() {
        assets = new AssetManager(); //Assetsmanager ei täyssillä käytössä -sonja
        batch = new SpriteBatch();

        initFonts();

        logoScreen = new LogoScreen(this);
        mainMenuScreen = new MainMenuScreen(this);

        this.setScreen(logoScreen);
    }

    public void render() {
        super.render();
    }


//muistetaan laittaa disposeen kaikki, jotta pelin pelaaminen olisi miellyttävämpää -sonja
    public void dispose() {
        batch.dispose();
        font22.dispose();
        font36.dispose();
        assets.dispose();
        this.getScreen().dispose();
    }
    //lisätään omat fontit peliin ja asetetaan niille parametrit, näitä voi kutsua Bitmappina pelin läpi -sonja
    //päivitetty Gradlen dependency, jotta freetype-fontit toimisivat -sonja
    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/fonts/LilitaOne-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parametri = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parametri.size = 22;
        parametri.color = Color.BLACK;
        font22 = generator.generateFont(parametri);

        // generoidaan otsikkofontti isommaksi -sonja
        FreeTypeFontGenerator.FreeTypeFontParameter otsikot = new FreeTypeFontGenerator.FreeTypeFontParameter();
        otsikot.size = 36;
        otsikot.color = Color.BLACK;
        font36 = generator.generateFont(otsikot);


    }

}

package main.java.uniche;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


//HUD (head-up display) tai "Status Bar"
public class HUD {
    public Stage stage;
    private Viewport viewport;
    public static HUD hud;
    //HUD:ssa kuppikakkulaskuri, mangokakkulaskuri ja elämäpalkki
    private static int cupCakeCounter;
    private static int mangoCounter;
    private static int health;


    private static Label cupCakeCounterLabel;
    private static Label healthLabel;
    private static Label mangoLabel;

    public HUD(){
        cupCakeCounter = 0;
        mangoCounter = 0;
        health = 5000;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        viewport = new FitViewport(w/2, h/2, new OrthographicCamera());
        stage = new Stage(viewport);

        //Laitetaan HUD:in osat taulukkoon (actor)
        Table table = new Table();
        table.top();
        table.setFillParent(true);


        cupCakeCounterLabel = new Label(String.format("%02d", cupCakeCounter), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        mangoLabel = new Label(String.format("%02d", mangoCounter), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthLabel = new Label(String.format("%02d", health), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        Image cupcakeImage = new Image(new Texture(Gdx.files.internal("core/assets/kakkukuvia/kuppikakku.png")));
        Image mangoImage = new Image(new Texture(Gdx.files.internal("core/assets/kakkukuvia/mangokakku.png")));
        Image sydanImage = new Image(new Texture(Gdx.files.internal("core/assets/sydan/sydan.png")));

        table.add(cupcakeImage).expandX().padTop(10);
        table.add(mangoImage).expandX().padTop(10);
        table.add(sydanImage).expandX().padTop(10);

        table.row();
        table.add(cupCakeCounterLabel).expandX();
        table.add(mangoLabel).expandX();
        table.add(healthLabel).expandX();

        //Asetetaan actor (taulukko) stagelle
        stage.addActor(table);

    }

    public int getHealth() {
        return health;
    }

    //Kerätään kuppikakku
    public static void addCupcake(){
        cupCakeCounter++;
        cupCakeCounterLabel.setText(String.format("%02d", cupCakeCounter));
        health += 200;
        healthLabel.setText(String.format("%02d", health));
    }
    //Kerätään mangokakku
    public static void addMango(){
        mangoCounter++;
        mangoLabel.setText(String.format("%02d", mangoCounter));
        health += 400;
        healthLabel.setText(String.format("%02d", health));
    }
    //Heikennetään terveyttä kävellessä
    public void reduceHealth(){
        health--;
        healthLabel.setText(String.format("%02d", health));
    }
    //Ydinjätetynnyriin osuminen aiheuttaa elämän vähenemisen
    public static void wasteHit(){
        health -= 500;
        healthLabel.setText(String.format("%02d", health));
    }

}

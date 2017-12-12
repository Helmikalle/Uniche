package main.java.uniche;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import main.java.uniche.utils.Skaalausta;

//HUD (head-up display) tai "Status Bar"
public class HUD {
    public Stage stage;
    private Viewport viewport;

    //HUD:ssa kuppikakkulaskuri, mangokakkulaskuri ja elämäpalkki
    private int cupCakeCounter;
    private int mangoCounter;
    private int health;

    private Label cupCakeCounterLabel;
    private Label cupcakeTextLabel;
    private Label healthLabel;
    private Label healthTextLabel;
    private Label mangoLabel;
    private Label mangoTextLabel;

    public HUD(){
        cupCakeCounter = 0;
        mangoCounter = 0;
        health = 4000;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        viewport = new FitViewport(w/2, h/2, new OrthographicCamera());
        stage = new Stage(viewport);

        //Laitetaan HUD:in osat taulukkoon (actor)
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        cupCakeCounterLabel = new Label(String.format("%02d", cupCakeCounter), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        cupcakeTextLabel = new Label("CUPCAKES: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        mangoLabel = new Label(String.format("%02d", mangoCounter), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        mangoTextLabel = new Label("MANGO CAKES: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthLabel = new Label(String.format("%02d", health), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthTextLabel = new Label("HEALTH: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(cupcakeTextLabel).expandX().padTop(10);
        table.add(mangoTextLabel).expandX().padTop(10);
        table.add(healthTextLabel).expandX().padTop(10);

        table.row();
        table.add(cupCakeCounterLabel).expandX();
        table.add(mangoLabel).expandX();
        table.add(healthLabel).expandX();

        //Asetetaan actor (taulukko) stagelle
        stage.addActor(table);

    }

    public int getCupCakeCounter() {
        return cupCakeCounter;
    }

    public int getMangoCounter() {
        return mangoCounter;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
    //Kerätään kuppikakku
    public void addCupcake(){
        cupCakeCounter++;
        cupCakeCounterLabel.setText(String.format("%02d", cupCakeCounter));
        health += 50;
        healthLabel.setText(String.format("%02d", health));
    }
    //Kerätään mangokakku
    public void addMango(){
        mangoCounter++;
        mangoLabel.setText(String.format("%02d", mangoCounter));
        health += 100;
        healthLabel.setText(String.format("%02d", health));
    }
    //Heikennetään terveyttä kävellessä
    public void reduceHealth(){
        health--;
        healthLabel.setText(String.format("%02d", health));
    }
    //Ydinjätetynnyriin osuminen aiheuttaa elämän vähenemisen
    public void wasteHit(){
        health -= 100;
        healthLabel.setText(String.format("%02d", health));
    }

}

package main.java.uniche.utils;


import com.badlogic.gdx.physics.box2d.*;
import main.java.uniche.entities.Cake;
import main.java.uniche.entities.HarmfulItem;
import main.java.uniche.entities.InvisLever;
import main.java.uniche.entities.Pony;

public class ContactHandler implements ContactListener {
    @Override
    public void beginContact(Contact contact) {

        Fixture figureA = contact.getFixtureA();
        Fixture figureB = contact.getFixtureB();
        //Aloitetaan katsomalla että on oikeasti tapahtunut törmäys
        if (figureA == null || figureB == null) return;
        if (figureA.getUserData() == null || figureB.getUserData() == null) return;

        //Tarkastellaan alempana toteutettu metodi kakun ja pelaajan yhteentörmäyksestä
        //ja jos toteutuu suoritetaan poiminta
        if (playerCollisionCake(figureA,figureB)){
            Pony unicorn;
            Cake cupcake;
            if (figureA.getUserData() instanceof Cake){
            unicorn = (Pony) figureB.getUserData();
            cupcake = (Cake) figureA.getUserData();
        } else {
            unicorn = (Pony) figureA.getUserData();
            cupcake = (Cake) figureB.getUserData();
        }
        cupcake.poimittu();

        }
        //Tarkastellaan alempana toteutettua metodia pelaajan ja jätteen törmäyksestä.
        // Jos toteutuu niin suoritetaan HarmfulItem luokassa wastePoimittu -Titta
        if (playerCollisionWaste(figureA,figureB)){
            Pony unicor;
            HarmfulItem wasteBarrel;
            if (figureA.getUserData() instanceof HarmfulItem){
                unicor = (Pony) figureB.getUserData();
                wasteBarrel = (HarmfulItem) figureA.getUserData();
            } else {
                unicor = (Pony) figureA.getUserData();
                wasteBarrel = (HarmfulItem) figureB.getUserData();
            }
            wasteBarrel.wastePoimittu();

        }
        //Tarkastellaan alempana toteutettua metodia
        // ja jos toteutuu niin suoritetaan nappulan painaminen -Kalle
        if (doorLeverContact(figureA,figureB)){
            Pony unicorn;
            InvisLever lever;
            if (figureA.getUserData() instanceof InvisLever){
                lever = (InvisLever) figureA.getUserData();
                unicorn = (Pony) figureB.getUserData();
            }else {
                unicorn = (Pony) figureA.getUserData();
                lever = (InvisLever) figureB.getUserData();
            }
            lever.painettu();

        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture pony = contact.getFixtureA();
        Fixture item = contact.getFixtureB();

        if (pony == null || item == null) return;
        if (pony.getUserData() == null || item.getUserData() == null) return;

        System.out.println("Kontakti loppui");

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
        //Katsotaan onko toinen törmääjistä kakku ja toinen poni -Kalle
    private boolean playerCollisionCake(Fixture a, Fixture b) {
        if (a.getUserData() instanceof Cake || b.getUserData() instanceof Cake) {
            if (a.getUserData() instanceof Pony || b.getUserData() instanceof Pony) {
                return true;
            }
        }
        return false;
    }
    //Katsotaan on törmääjät Pony ja jokin HarmfulItem -Titta
    private boolean playerCollisionWaste(Fixture a, Fixture b) {
        if (a.getUserData() instanceof HarmfulItem || b.getUserData() instanceof HarmfulItem) {
            if (a.getUserData() instanceof Pony || b.getUserData() instanceof Pony) {
                return true;
            }
        }
        return false;
    }
    //Tässä tarkastellaan onko törmääjät kenties InvisLever ja Pony -Kalle
    private boolean doorLeverContact(Fixture a, Fixture b){
        if (a.getUserData() instanceof Pony || b.getUserData() instanceof Pony){
            if (a.getUserData() instanceof InvisLever || b.getUserData() instanceof InvisLever)
                return true;
        }
        return false;
    }
}

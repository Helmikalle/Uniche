package main.java.uniche.utils;


import com.badlogic.gdx.physics.box2d.*;
import main.java.uniche.entities.Cake;
import main.java.uniche.entities.HarmfulItem;
import main.java.uniche.entities.Pony;

public class ContactHandler implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture figureA = contact.getFixtureA();
        Fixture figureB = contact.getFixtureB();

        if (figureA == null || figureB == null) return;
        if (figureA.getUserData() == null || figureB.getUserData() == null) return;

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
        cupcake.kerätty();

        }
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

    private boolean playerCollisionCake(Fixture a, Fixture b) {
        if (a.getUserData() instanceof Cake || b.getUserData() instanceof Cake) {
            if (a.getUserData() instanceof Pony || b.getUserData() instanceof Pony) {
                return true;
            }
        }
        return false;
    }
    private boolean playerCollisionWaste(Fixture a, Fixture b) {
        if (a.getUserData() instanceof HarmfulItem || b.getUserData() instanceof HarmfulItem) {
            if (a.getUserData() instanceof Pony || b.getUserData() instanceof Pony) {
                return true;
            }
        }
        return false;
    }
}

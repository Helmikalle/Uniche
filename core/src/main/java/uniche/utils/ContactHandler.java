package main.java.uniche.utils;


import com.badlogic.gdx.physics.box2d.*;
import main.java.uniche.entities.Cakes;
import main.java.uniche.entities.Pony;

public class ContactHandler implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture figureA = contact.getFixtureA();
        Fixture figureB = contact.getFixtureB();

        if (figureA == null || figureB == null) return;
        if (figureA.getUserData() == null || figureB.getUserData() == null) return;

        if (playerCollision(figureA,figureB)){
            Pony unicorn;
            Cakes cupcake;
            if (figureA.getUserData() instanceof Cakes){
            unicorn = (Pony) figureB.getUserData();
            cupcake = (Cakes) figureA.getUserData();
        } else {
            unicorn = (Pony) figureA.getUserData();
            cupcake = (Cakes) figureB.getUserData();
        }
        cupcake.poimittu();
        cupcake.kerätty();
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

    private boolean playerCollision(Fixture a, Fixture b) {
        if (a.getUserData() instanceof Cakes || b.getUserData() instanceof Cakes) {
            if (a.getUserData() instanceof Pony || b.getUserData() instanceof Pony) {
                return true;
            }
        }
        return false;
    }
}

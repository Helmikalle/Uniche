package main.java.uniche.utils;


import com.badlogic.gdx.physics.box2d.*;

public class ContactHandler implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture pony = contact.getFixtureA();
        Fixture item = contact.getFixtureB();

        if (pony == null || item == null) return;
        if (pony.getUserData() == null || item.getUserData() == null) return;

        System.out.println("BOOM");
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
}

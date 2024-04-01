package Simulation;

import Actors.Enemy.Enemy;
import com.badlogic.gdx.physics.box2d.*;

import java.util.List;
import java.util.Set;

public class EnemyContactListener implements ContactListener {
    private final World world;
    private final Body player;
    private final Set<Body> toBeKilled;



    public EnemyContactListener(World world, Body player, Set<Body> toBeKilled) {
        this.world = world;
        this.player = player;
        this.toBeKilled = toBeKilled;
    }

    private boolean playerInContact(Body b1, Body b2) {
        return b1 == player || b2 == player;
    }

    private void killIfNotPlayer(Body b) {
        if(b == player) return;
        //toBeKilled.add(b);
    }

    @Override
    public void beginContact(Contact contact) {
        Body b1 = contact.getFixtureA().getBody();
        Body b2 = contact.getFixtureB().getBody();

        if (!playerInContact(b1, b2)) return;

        killIfNotPlayer(b1);
        killIfNotPlayer(b2);
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}

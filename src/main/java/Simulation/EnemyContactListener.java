package Simulation;

import GameObjects.Actors.Enemy.Enemy;
import GameObjects.Actors.Player.Player;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.TimeUtils;

public class EnemyContactListener implements ContactListener {

    int contactNumber;

    private static final long COOL_DOWN_DURATION = 200; // cool down in millis
    private long lastHit;

    public EnemyContactListener() {
        contactNumber = 0;
    }

    /**
     *
     * @param b1 first body
     * @param b2 second body
     * @return true if collision occurs between player and enemy, false otherwise
     */
    private boolean playerEnemyCollision(Body b1, Body b2) {
        return (b1.getUserData() instanceof Player && b2.getUserData() instanceof Enemy)
                || (b1.getUserData() instanceof Enemy && b2.getUserData() instanceof Player);
    }



    @Override
    public void beginContact(Contact contact) {
        Body b1 = contact.getFixtureA().getBody();
        Body b2 = contact.getFixtureB().getBody();

        if (!playerEnemyCollision(b1, b2)) return;
        Player player = (Player) (b1.getUserData() instanceof Player ? b1.getUserData() : b2.getUserData());
        Enemy enemy = (Enemy) (b1.getUserData() instanceof Enemy ? b1.getUserData() : b2.getUserData());

        if(coolDown()) {
            System.out.println("COOL DOWN!");
            return;
        }

        contactNumber ++;


        collisionAttack(enemy, player);
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

    /**
     * Enemy attacks player when colliding
     * @param enemy
     * @param player
     */
    private void collisionAttack(Enemy enemy, Player player) {

        System.out.println("Contact nr: " + contactNumber + ", " + enemy.getType() + ", damage: " + enemy.damage);
        enemy.attack(player);
        System.out.println("Player HP: " + player.HP);

        lastHit = TimeUtils.millis();

    }

    /**
     *
     * @return true if Plauer is in cool down mode
     */
    private boolean coolDown() {
        return TimeUtils.millis() - lastHit < COOL_DOWN_DURATION;
    }
}

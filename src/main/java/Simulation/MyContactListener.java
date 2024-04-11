package Simulation;

import GameObjects.Actors.Enemy.Enemy;
import GameObjects.Actors.Player.Player;
import GameObjects.Weapon.Weapon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.TimeUtils;

public class MyContactListener implements ContactListener {

    int contactNumber;

    private static final long COOL_DOWN_DURATION = 200; // cool down in millis
    private long lastHit;

    public MyContactListener() {
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

    private boolean weaponEnemyCollision(Body b1, Body b2) {
        return (b1.getUserData() instanceof Weapon && b2.getUserData() instanceof Enemy)
                || (b1.getUserData() instanceof Enemy && b2.getUserData() instanceof Weapon);
    }


    @Override
    public void beginContact(Contact contact) {
        Body b1 = contact.getFixtureA().getBody();
        Body b2 = contact.getFixtureB().getBody();

        if (weaponEnemyCollision(b1, b2)) {
            Weapon weapon = b1.getUserData() instanceof Weapon ? (Weapon) b1.getUserData():(Weapon) b2.getUserData();
            Enemy enemy = b1.getUserData() instanceof Enemy ? (Enemy) b1.getUserData():(Enemy) b2.getUserData();
            weapon.attack(enemy, weapon.getOwner().damage);
            System.out.println("WEAPON COLLISION");
        } else if (playerEnemyCollision(b1, b2)) {
            Player player = b1.getUserData() instanceof Player ? (Player) b1.getUserData():(Player) b2.getUserData();
            Enemy enemy = b1.getUserData() instanceof Enemy ? (Enemy) b1.getUserData():(Enemy) b2.getUserData();
            if(!player.isUnderAttack()) {
                enemy.attack(player, enemy.damage);
            }
        }


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
        //enemy.attack(player);
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

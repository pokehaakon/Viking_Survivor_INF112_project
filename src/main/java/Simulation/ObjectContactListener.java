package Simulation;

import GameObjects.Actors.Enemy.Enemy;
import GameObjects.Actors.Player.Player;
import GameObjects.Weapon.Weapon;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.TimeUtils;

public class ObjectContactListener implements ContactListener {





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


}

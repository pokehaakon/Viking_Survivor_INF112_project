package Simulation;

import GameObjects.Actors.Enemy;
import GameObjects.Actors.Pickups;
import GameObjects.Actors.Player;
import GameObjects.Actors.Weapon;
import com.badlogic.gdx.physics.box2d.*;

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

    private boolean playerPickupCollision(Body b1, Body b2) {
        return (b1.getUserData() instanceof Player && b2.getUserData() instanceof Pickups)
                || (b1.getUserData() instanceof Pickups && b2.getUserData() instanceof Player);
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
            System.out.println("PLAYER COLLISION");
        }
        else if (playerPickupCollision(b1, b2)) {
            Player player = b1.getUserData() instanceof Player ? (Player) b1.getUserData():(Player) b2.getUserData();
            Pickups pickup = b1.getUserData() instanceof Pickups ? (Pickups) b1.getUserData():(Pickups) b2.getUserData();
            player.pickup(pickup);
            pickup.setPickedUp(true);
            System.out.println("PICKUP COLLISION");
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

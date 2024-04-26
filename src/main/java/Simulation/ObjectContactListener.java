package Simulation;

import GameObjects.Actors.Actor;
import GameObjects.Actors.Pickups;
import GameObjects.GameObject;
import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.*;

import static Tools.FilterTool.Category.*;

public class ObjectContactListener implements ContactListener {
    static final private short playerAndEnemyMask = FilterTool.combineMaskEnums(PLAYER, ENEMY);
    static final private short weaponAndEnemyMask = FilterTool.combineMaskEnums(WEAPON, ENEMY);
    static final private short playerAndPickupMask = FilterTool.combineMaskEnums(PLAYER, PICKUP);

    static private boolean isInCategory(Body body, FilterTool.Category category) {
        return (categoryBits(body) & category.getMask()) != 0;
    }

    /**
     * gets the category bits of the body
     * @param body
     * @return
     */
    static private short categoryBits(Body body) {
        return body.getFixtureList().get(0).getFilterData().categoryBits;
    }

    /**
     * returns true if the category bits between b1 and b2 covers the mask
     * @param b1
     * @param b2
     * @param mask
     * @return
     */
    static private boolean XYCollision(Body b1, Body b2, short mask) {
        return ((categoryBits(b1) | categoryBits(b2)) & mask) == mask;
    }

    /**
     * @param b1 first body
     * @param b2 second body
     * @return true if collision occurs between player and enemy, false otherwise
     */
    static private boolean playerEnemyCollision(Body b1, Body b2) {
        return XYCollision(b1, b2, playerAndEnemyMask);
    }

    /**
     * @param b1 first body
     * @param b2 second body
     * @return true if collision occurs between weapon and enemy, false otherwise
     */
    static private boolean weaponEnemyCollision(Body b1, Body b2) {
        return XYCollision(b1, b2, weaponAndEnemyMask);
    }

    /**
     * @param b1 first body
     * @param b2 second body
     * @return true if collision occurs between player and pickup, false otherwise
     */
    static private boolean playerPickupCollision(Body b1, Body b2) {
        return XYCollision(b1, b2, playerAndPickupMask);
    }

    static private GameObject getObjectWithCategory(Body b1, Body b2, FilterTool.Category category) {
        if ((categoryBits(b1) & category.getMask()) != 0 ) return (GameObject) b1.getUserData();
        return (GameObject) b2.getUserData();
    }

    @Override
    public void beginContact(Contact contact) {

        Body b1 = contact.getFixtureA().getBody();
        Body b2 = contact.getFixtureB().getBody();

        if (weaponEnemyCollision(b1, b2)) {
            Actor weapon = (Actor) getObjectWithCategory(b1, b2, WEAPON);
            Actor enemy = (Actor) getObjectWithCategory(b1, b2, ENEMY);
            weapon.attack(enemy);
            //enemy.underAttack = true;
            //System.out.println("WEAPON COLLISION");

        } else if (playerEnemyCollision(b1, b2)) {
            Actor player = (Actor) getObjectWithCategory(b1, b2, PLAYER);
            Actor enemy = (Actor) getObjectWithCategory(b1, b2, ENEMY);

            if(!player.isUnderAttack()) {
                enemy.attack(player);
            }
            //System.out.println("PLAYER COLLISION");
        }
        else if (playerPickupCollision(b1, b2)) {
            Actor player = (Actor) getObjectWithCategory(b1, b2, PLAYER);
            Pickups pickup = (Pickups) getObjectWithCategory(b1, b2, PICKUP);
            //TODO fix pickups
            //player.pickup(pickup);
            pickup.doPickupAction();
            pickup.destroy();
            //System.out.println("PICKUP COLLISION");
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

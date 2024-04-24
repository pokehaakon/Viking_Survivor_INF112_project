package GameObjects.Actors;

import Rendering.Animations.AnimationRendering.AnimationHandler;

import GameObjects.ObjectTypes.WeaponType;
import GameObjects.BodyFeatures;

public class Weapon extends Actor  {
    private final long ORBIT_INTERVAL = 1000;
    public float damage;
    private long lastOrbit;
    private float angleToPlayer = 0;

    private long lastAttack;

    private Player owner;
    public Weapon(String name, AnimationHandler animationHandler, BodyFeatures bodyFeatures, float scale) {
        super(name, animationHandler, bodyFeatures, scale);
    }



    public float getAngleToPlayer() {
        return angleToPlayer;
    }

    public void setAngleToPlayer(float newAngle) {
        angleToPlayer = newAngle;
    }


    public void setOwner(Player player) {
        owner = player;
        player.addToInventory(this);
    }

    public long getLastAttack() {
        return lastAttack;
    }

    public void setLastAttack(long newAttack) {
        lastAttack = newAttack;
    }

    public Player getOwner() {
        return owner;
    }
}

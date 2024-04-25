package GameObjects.Actors;

import GameObjects.Animations.AnimationRendering.AnimationHandler;

import GameObjects.ObjectTypes.WeaponType;
import GameObjects.BodyFeatures;

public class Weapon extends Actor<WeaponType>  {
    private final long ORBIT_INTERVAL = 1000;
    public float damage;
    private long lastOrbit;
    private float angleToPlayer = 0;


    private Player owner;
    public Weapon(WeaponType type, AnimationHandler animationHandler, BodyFeatures bodyFeatures, float scale) {
        super(type,animationHandler,bodyFeatures,scale);
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



    public Player getOwner() {
        return owner;
    }

}

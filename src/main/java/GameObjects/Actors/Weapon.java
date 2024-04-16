package GameObjects.Actors;

import GameObjects.Actors.Actor;
import GameObjects.Animations.AnimationRendering.ObjectAnimations;
import GameObjects.Animations.AnimationState;
import GameObjects.ObjectTypes.WeaponType;
import GameObjects.Actors.Player;
import GameObjects.BodyFeatures;
import GameObjects.Animations.AnimationRendering.AnimationRender;

import java.util.Map;

public class Weapon extends Actor<WeaponType>  {
    private final long ORBIT_INTERVAL = 1000;
    public float damage;
    private long lastOrbit;
    private float angleToPlayer = 0;

    private long lastAttack;

    private Player owner;
    public Weapon(WeaponType type, ObjectAnimations objectAnimations, BodyFeatures bodyFeatures, float scale) {
        super(type,objectAnimations,bodyFeatures,scale);
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

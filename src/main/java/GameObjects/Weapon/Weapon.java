package GameObjects.Weapon;

import GameObjects.Actors.Actor;
import GameObjects.Actors.ObjectTypes.WeaponType;
import GameObjects.Actors.Player.Player;
import GameObjects.BodyFeatures;
import Rendering.AnimationRender;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class Weapon extends Actor<WeaponType>  {
    private final long ORBIT_INTERVAL = 1000;
    public float damage;
    private long lastOrbit;
    private float angleToPlayer = 0;

    private long lastAttack;

    private Player owner;
    public Weapon(WeaponType type, AnimationRender render, BodyFeatures bodyFeatures, float scale) {
        super(type,render,bodyFeatures,scale);
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

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
    private float angleToPlayer;
    public Weapon(WeaponType type, AnimationRender render, BodyFeatures bodyFeatures, float scale) {
        super(type,render,bodyFeatures,scale);
    }


    public void orbit(long lastOrbit, Player player, float orbitSpeed, float orbitRadius) {
        if(TimeUtils.millis() - lastOrbit > ORBIT_INTERVAL) {
            body.setActive(true);

            float x = (float) (Math.cos(angleToPlayer) * orbitRadius);
            float y = (float) (Math.sin(angleToPlayer) * orbitRadius);
            body.setTransform(new Vector2(x, y).add(player.getBody().getPosition()), body.getAngle());
            angleToPlayer += orbitSpeed;
            if (angleToPlayer >= 2 * Math.PI) {
                body.setActive(false);
                lastOrbit = TimeUtils.millis();
                //System.out.println(w.getBody().isActive());
            }
        }

    }

    public float getAngleToPlayer() {
        return angleToPlayer;
    }

    public void setAngleToPlayer(float newAngle) {
        angleToPlayer = newAngle;
    }
}

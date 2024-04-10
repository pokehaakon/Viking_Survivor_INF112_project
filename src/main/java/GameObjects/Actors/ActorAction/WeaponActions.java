package GameObjects.Actors.ActorAction;

import GameObjects.Actors.Player.Player;
import GameObjects.Weapon.Weapon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.TimeUtils;

public abstract class WeaponActions {
    float angle = 0;
    public ActorAction<Weapon> startOrbit(float angle, float orbitRadius, Player player) {
        angle = this.angle;
        float finalAngle = angle;
        return (w) -> {
            float x = (float) (Math.cos(finalAngle) * orbitRadius);
            float y = (float) (Math.sin(finalAngle) * orbitRadius);
            w.getBody().setTransform(new Vector2(x, y).add(player.getBody().getPosition()), w.getBody().getAngle());
            if (finalAngle >= 2 * Math.PI) {
                System.out.println("Full round");
                w.getBody().setActive(false);
                System.out.println(w.getBody().isActive());
            }

        };
    }

    public static ActorAction<Weapon> orbit(Player player, float orbitSpeed, float orbitRadius) {
        return (w) -> {
            Weapon weapon = (Weapon) w;
                weapon.getBody().setActive(true);
                float x = (float) (Math.cos(weapon.getAngleToPlayer()) * orbitRadius);
                float y = (float) (Math.sin(weapon.getAngleToPlayer()) * orbitRadius);
                weapon.getBody().setTransform(new Vector2(x, y).add(player.getBody().getPosition()), weapon.getBody().getAngle());
                weapon.setAngleToPlayer(weapon.getAngleToPlayer() + orbitSpeed);
        };


    }
}

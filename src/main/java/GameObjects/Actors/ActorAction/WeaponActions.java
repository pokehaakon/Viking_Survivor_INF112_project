package GameObjects.Actors.ActorAction;

import GameObjects.Actors.Player.Player;
import GameObjects.Weapon.Weapon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public abstract class WeaponActions {

    public static ActorAction<Weapon> orbit(Player player) {
        return (w) -> {
            float orbitRadius = 5.0f; // Radius of orbit
            float orbitSpeed = 1.0f; // Angular speed of orbit
            float angle = 0;
            while (true) {
                // Calculate orbit position
                float x = (float) (Math.cos(angle) * orbitRadius);
                float y = (float) (Math.sin(angle) * orbitRadius);

                // Set position of orbiting body relative to center body
                w.getBody().setTransform(new Vector2(x, y).add(player.getBody().getPosition()), w.getBody().getAngle());

                // Increase angle for next frame
                angle += orbitSpeed * 0.016f; // Adjust time step as needed
            }

        };
    }
}

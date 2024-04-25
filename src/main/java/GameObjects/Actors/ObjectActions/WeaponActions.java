package GameObjects.Actors.ObjectActions;

import Contexts.Context;
import Contexts.ReleaseCandidateContext;
import GameObjects.Actors.Player;
import GameObjects.Actors.Weapon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import static Contexts.ReleaseCandidateContext.CONSTANT_FPS;
import static Tools.RollingSum.millisToFrames;

public abstract class WeaponActions {

    /**
     * Weapon orbits player
     * @param orbitRadius
     * @param orbitSpeed
     * @param player the player to orbit
     * @param orbitInterval millis second between each orbit
     * @return a weapon action
     */
    public static Action<Weapon> orbitPlayer(ReleaseCandidateContext context, float orbitRadius, float orbitSpeed, Player player, float orbitInterval) {

        return (w) -> {
            if(context.getCurrentFrame() - w.getLastAttack() > millisToFrames(CONSTANT_FPS,orbitInterval) || w.getLastAttack() == 0) {
                if(!w.getBody().isActive()) {
                    w.getBody().setActive(true);
                }
                float x = (float) (Math.cos(w.getAngleToPlayer()) * orbitRadius);
                float y = (float) (Math.sin(w.getAngleToPlayer()) * orbitRadius);
                w.getBody().setTransform(new Vector2(x, y).add(player.getBody().getPosition()), w.getBody().getAngle());
                w.setAngleToPlayer(w.getAngleToPlayer() + orbitSpeed);
                if (w.getAngleToPlayer() >= 2 * Math.PI) {
                    w.getBody().setActive(false);
                    w.setLastAttack(context.getCurrentFrame());
                    w.setAngleToPlayer(0);

                }
            }

        };
    }

}

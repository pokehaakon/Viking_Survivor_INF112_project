package GameObjects.Actors.ActorAction;

import GameObjects.Actors.Player;
import GameObjects.Actors.Weapon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public abstract class WeaponActions {

    /**
     * Weapon orbits player
     * @param orbitRadius
     * @param orbitSpeed
     * @param player the player to orbit
     * @param orbitInterval millis second between each orbit
     * @return a weapon action
     */
    public static ActorAction<Weapon> orbitPlayer(float orbitRadius, float orbitSpeed, Player player, long orbitInterval) {

        return (w) -> {
            if(TimeUtils.millis() - w.getLastAttack() > orbitInterval) {
                if(!w.getBody().isActive()) {
                    w.getBody().setActive(true);
                }
                float x = (float) (Math.cos(w.getAngleToPlayer()) * orbitRadius);
                float y = (float) (Math.sin(w.getAngleToPlayer()) * orbitRadius);
                w.getBody().setTransform(new Vector2(x, y).add(player.getBody().getPosition()), w.getBody().getAngle());
                w.setAngleToPlayer(w.getAngleToPlayer() + orbitSpeed);
                if (w.getAngleToPlayer() >= 2 * Math.PI) { //TODO wrong round
                    System.out.println("Full round");
                    w.getBody().setActive(false);
                    w.setLastAttack(TimeUtils.millis());
                    w.setAngleToPlayer(0);

                }
            }

        };
    }

}

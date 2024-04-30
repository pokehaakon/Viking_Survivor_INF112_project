package GameObjects.ObjectActions;

import GameObjects.Actor;
import com.badlogic.gdx.math.Vector2;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.concurrent.atomic.AtomicLong;

import static VikingSurvivor.app.HelloWorld.SET_FPS;

public abstract class WeaponActions {

    /**
     * Weapon orbits player
     * @param orbitRadius in meters
     * @param orbitSpeed in radians / frame
     * @param actor the player to orbit
     * @param orbitInterval millisecond between each orbit,
     * @return a weapon action
     */
    public static Action orbitActor(float orbitRadius, float orbitSpeed, Actor actor, double orbitInterval, float startingAngle) {
        long frameInterval = (long) (orbitInterval * SET_FPS / 1000);
        AtomicLong framesSinceLastAttack = new AtomicLong(frameInterval);
        AtomicDouble angle = new AtomicDouble(2 * Math.PI + startingAngle + 1);
        return weapon -> {
            if (framesSinceLastAttack.getAndIncrement() >= frameInterval && !weapon.getBody().isActive()) {
                //start weapon again, (cooldown over)
                framesSinceLastAttack.set(0);
                weapon.getBody().setActive(true);
                angle.set(startingAngle);
            }

            if (!weapon.getBody().isActive()) return; //weapon is on cooldown

            //weapon.getBody().setActive(true);

            Vector2 newPos = actor.getBody().getPosition().cpy()
                    .sub(weapon.getBody().getPosition())
                    .setLength(orbitRadius)
                    .setAngleRad((float) angle.get())
                    .add(actor.getBody().getPosition());

            weapon.getBody()
                    .setTransform(
                            newPos,
                            weapon.getBody().getAngle()
                    );

            if (angle.get() >= 2 * Math.PI + startingAngle) {
                //System.out.println("Full round");
                weapon.getBody().setActive(false);
            }
            angle.addAndGet(orbitSpeed);

        };
    }

}

package GameObjects.ObjectActions;

import GameObjects.Actor;
import Tools.FilterTool;
import com.badlogic.gdx.math.Vector2;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


import static Simulation.ObjectContactListener.isInCategory;
import static VikingSurvivor.app.HelloWorld.SET_FPS;

public abstract class WeaponActions {


    public static final float DEFAULT_ORBIT_SPEED = 0.1f;
    private static float ORBIT_SPEED = DEFAULT_ORBIT_SPEED;

    private static Actor closestEnemy;
    private static boolean countDown = false;

    private static AtomicLong framesLeft;
    public static void setOrbitSpeed(float newSpeed,double duration) {
        ORBIT_SPEED = newSpeed;
        framesLeft = new AtomicLong((long) (duration*SET_FPS/1000));

        countDown = true;
    }

    private static void startCountDown(AtomicLong framesLeft) {
        System.out.println(framesLeft.get());
        if(framesLeft.getAndDecrement() <= 0) {
            countDown = false;
            ORBIT_SPEED = DEFAULT_ORBIT_SPEED;
        }
    }




    /**
     * Weapon orbits player
     * @param orbitRadius in meters

     * @param actor the player to orbit
     * @param orbitInterval millisecond between each orbit,
     * @return a weapon action
     */
    public static Action orbitActor(float orbitRadius, Actor actor, double orbitInterval, float startingAngle) {
        long frameInterval = (long) (orbitInterval * SET_FPS / 1000);
        AtomicLong framesSinceLastAttack = new AtomicLong(frameInterval);
        AtomicDouble angle = new AtomicDouble(2 * Math.PI + startingAngle + 1);

        return weapon -> {
            if(countDown) {
                startCountDown(framesLeft);
            }

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
            angle.addAndGet(ORBIT_SPEED);

        };
    }

    private static Actor getClosestEnemy(Actor actor, List<Actor> actors) {
        Vector2 actorPosition = actor.getBody().getPosition();
        Actor closestEnemy = null;
        float closestDistance = Integer.MAX_VALUE;
        for(Actor enemy : actors) {
            if(isInCategory(enemy.getBody(), FilterTool.Category.ENEMY)) {
                float newDistance = Vector2.dst(
                        actorPosition.x,actorPosition.y,
                        enemy.getBody().getPosition().x, enemy.getBody().getPosition().y);

                if(newDistance < closestDistance) {
                    closestEnemy = enemy;
                    closestDistance = newDistance;
                }

            }

        }

        return closestEnemy;
    }



    public static Action throwOnClosestEnemy(Actor actor, double interval, List<Actor> enemies) {
        long frameInterval = (long) (interval * SET_FPS / 1000);
        AtomicLong framesSinceLastThrow = new AtomicLong(frameInterval);
        AtomicDouble attack = new AtomicDouble(1);
        return (weapon) -> {

            if (framesSinceLastThrow.getAndIncrement() >= frameInterval && attack.get()==1 ) {
                attack.set(0);
                closestEnemy = getClosestEnemy(actor,enemies);
                System.out.println(closestEnemy.getBody().getPosition());
                weapon.setPosition(actor.getBody().getPosition());
                var vel = weapon.getBody().getLinearVelocity();
                vel
                        .set(closestEnemy.getBody().getPosition())
                        .sub(weapon.getBody().getWorldCenter())
                        .setLength(actor.getSpeed()+30);
                weapon.getBody().setLinearVelocity(vel);
            }

            if(closestEnemy.isUnderAttack() && closestEnemy.attackedBy(weapon)) {
                System.out.println("YEAH");
                framesSinceLastThrow.set(0);
                attack.set(1);
                weapon.setPosition(actor.getBody().getPosition());

            }

//            if(framesSinceLastThrow.getAndIncrement() >= frameInterval) {
//                framesSinceLastThrow.set(0);
//            }

        };
    }

}

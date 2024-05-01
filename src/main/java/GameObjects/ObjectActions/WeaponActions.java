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

    private static boolean alterWeaponSpeed = false;

    private static AtomicLong framesLeft;
    public static void setOrbitSpeed(float newSpeed,double duration) {
        ORBIT_SPEED = newSpeed;
        framesLeft = new AtomicLong((long) (duration*SET_FPS/1000));

        alterWeaponSpeed = true;
    }

    private static void startSpeedChangeCountDown(AtomicLong framesLeft) {
        if(framesLeft.getAndDecrement() <= 0) {
            alterWeaponSpeed = false;
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
            if(alterWeaponSpeed) {
                startSpeedChangeCountDown(framesLeft);
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

    private static boolean attackedByWeapon(Actor weapon, List<Actor> actors) {
        for(Actor actor : actors) {
            if((actor.attackedBy(weapon))) {
                return true;
            }
        }
        return false;
    }



    public static Action fireAtClosestEnemy(Actor actor, double interval, List<Actor> enemies, Vector2 boundSquare) {
        long frameInterval = (long) (interval * SET_FPS / 1000);
        AtomicLong framesSinceLastThrow = new AtomicLong(0);

        return (weapon) -> {;
            if (framesSinceLastThrow.getAndIncrement() <= 0 ) {
                if(!weapon.getBody().isActive()) {
                    weapon.getBody().setActive(true);
                }

                Actor closestEnemy = getClosestEnemy(actor,enemies);
                weapon.setPosition(actor.getBody().getPosition());

                var vel = weapon.getBody().getLinearVelocity();
                vel
                        .set(closestEnemy.getBody().getPosition())
                        .sub(weapon.getBody().getWorldCenter())
                        .setLength(weapon.getSpeed());

                weapon.getBody().setLinearVelocity(vel);
            }


            if(weapon.outOfBounds(actor,boundSquare) || attackedByWeapon(weapon,enemies)) {
                framesSinceLastThrow.set(-frameInterval);
                weapon.setPosition(actor.getBody().getPosition());
                weapon.getBody().setActive(false);
            }


//

        };
    }

}

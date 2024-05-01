package GameObjects.ObjectActions;

import GameObjects.Actor;
import Tools.FilterTool;
import com.badlogic.gdx.math.Vector2;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


import static GameObjects.ObjectActions.MovementActions.chaseActorCustomSpeed;
import static Simulation.ObjectContactListener.isInCategory;
import static VikingSurvivor.app.HelloWorld.SET_FPS;

public abstract class WeaponActions {


    public static final float DEFAULT_ORBIT_SPEED = 0.1f;
    private static float ORBIT_SPEED = DEFAULT_ORBIT_SPEED;

    private static boolean alterWeaponSpeed = false;
    private static float speedMultiplier = 1;
    //private static float speed;
    private static AtomicLong framesLeftOfChange = new AtomicLong(0);
    public static void setSpeed(double duration, float multiplier) {
        System.out.println(multiplier);
        speedMultiplier = multiplier;
        framesLeftOfChange = new AtomicLong((long)duration*SET_FPS/1000);
        alterWeaponSpeed = true;
    }





    /**
     * Weapon orbits player
     * @param orbitRadius in meters

     * @param actor the player to orbit
     * @param orbitInterval millisecond between each orbit,
     * @return a weapon action
     */
    public static Action orbitActor(float orbitSpeed,float orbitRadius, Actor actor, double orbitInterval, float startingAngle) {
        long frameInterval = (long) (orbitInterval * SET_FPS / 1000);
        AtomicLong framesSinceLastAttack = new AtomicLong(frameInterval);
        AtomicDouble angle = new AtomicDouble(2 * Math.PI + startingAngle + 1);

        float multip = 10;
        //long framesLeftOfChange = (long) (changeDuration*SET_FPS/1000);
        AtomicDouble orbSpeed = new AtomicDouble(orbitSpeed);
        //AtomicLong temporaryChange = new AtomicLong(framesLeftOfChange);

        return weapon -> {

            if(alterWeaponSpeed) {
                //System.out.println(speedMultiplier.get());
                orbSpeed.set(orbitSpeed*(float)speedMultiplier);
                //System.out.println(orbSpeed.get());
                if(framesLeftOfChange.getAndDecrement() <= 0) {
                    orbSpeed.set(orbitSpeed);
                    alterWeaponSpeed = false;
                }
            }

            if (framesSinceLastAttack.getAndIncrement() >= frameInterval && !weapon.getBody().isActive()) {
                //start weapon again, (cooldown over)
                if(alterWeaponSpeed) {
                    framesSinceLastAttack.set(frameInterval);
                }

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

            //System.out.println(speed.get());
            angle.addAndGet((float)orbSpeed.get());

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



    public static Action fireAtClosestEnemy(float speed,Actor actor, double interval, List<Actor> enemies, Vector2 boundSquare) {
        long frameInterval = (long) (interval * SET_FPS / 1000);
        AtomicLong framesSinceLastThrow = new AtomicLong(0);
        AtomicDouble projSpeed = new AtomicDouble(speed);

        return (weapon) -> {
            if(alterWeaponSpeed) {
                System.out.println("CRAZYYY");
                projSpeed.set(speed*speedMultiplier);
                if(framesLeftOfChange.getAndDecrement() <= 0) {
                    projSpeed.set(speed);
                    alterWeaponSpeed = false;
                }
            }

            if (framesSinceLastThrow.getAndIncrement() <= 0 ) {
                if(!weapon.getBody().isActive()) {
                    weapon.getBody().setActive(true);
                }
                Actor closestEnemy = getClosestEnemy(actor,enemies);
                weapon.setPosition(actor.getBody().getPosition());
                chaseActorCustomSpeed(closestEnemy,(float)projSpeed.get()).act(weapon);

            }

            if(weapon.outOfBounds(actor,boundSquare) || attackedByWeapon(weapon,enemies)){
                framesSinceLastThrow.set(alterWeaponSpeed ? 0 : -frameInterval);

                weapon.setPosition(actor.getBody().getPosition());
                weapon.getBody().setActive(false);
            }

        };
    }



}

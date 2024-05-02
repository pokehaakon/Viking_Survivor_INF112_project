package GameObjects.ObjectActions;

import GameObjects.Actor;
import Tools.FilterTool;
import com.badlogic.gdx.math.Vector2;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;


import static GameObjects.ObjectActions.MovementActions.chaseActor;
import static GameObjects.ObjectActions.MovementActions.chaseActorCustomSpeed;
import static Simulation.ObjectContactListener.isInCategory;
import static VikingSurvivor.app.HelloWorld.SET_FPS;

public abstract class WeaponActions {



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

        return weapon -> {
            //doPotentialActionChange(weapon);

            if (!weapon.getBody().isActive() && framesSinceLastAttack.getAndIncrement() >= frameInterval) {
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
            angle.addAndGet((float)orbitSpeed);

        };
    }

    /**
     * Finds the closest actor  by iteration through the list of actors
     * @param referenceActor the reference point
     * @param actors list of actors to iterate through. It filters the actors using the Category.Filter
     * @param category the category of actors to find
     * @return the closest actor
     */
    public static Actor getClosestActor(Actor referenceActor, List<Actor> actors, FilterTool.Category category) {
        Vector2 actorPosition = referenceActor.getBody().getPosition();
        Actor closestActor = null;
        float closestDistance = Integer.MAX_VALUE;
        for(Actor actor : actors) {
            if(isInCategory(actor.getBody(), category)) {
                float newDistance = Vector2.dst(
                        actorPosition.x,actorPosition.y,
                        actor.getBody().getPosition().x, actor.getBody().getPosition().y);

                if(newDistance < closestDistance) {
                    closestActor = actor;
                    closestDistance = newDistance;
                }
            }
        }
        return closestActor;
    }

    /**
     * Checks if an actor is attacked by the weapon. Useful for the fireAtClosestEnemy action
     * @param weapon the weapon in question
     * @param actors list of actors to iterate through
     * @return true if an enemy is attacked by the weapon, false otherwise
     */
    private static boolean attackedByWeapon(Actor weapon, List<Actor> actors) {
        for(Actor actor : actors) {
            if((actor.attackedBy(weapon))) {
                return true;
            }
        }
        return false;
    }


    /**
     * Fires at closest enemy. If weapon hits enemy or is out of bounds, the weapon returns to owners position
     * @param speed weapon speed
     * @param actor the actor which fires the weapon
     * @param interval duration between weapon returns to player's position and a new shot is taken
     * @param actors the list of actors to iterate through
     * @param boundSquare sets when the weapon is out of bounds
     * @return a weapon action
     */
    public static Action fireAtClosestEnemy(float speed,Actor actor, double interval, List<Actor> actors, Vector2 boundSquare) {
        long frameInterval = (long) (interval * SET_FPS / 1000);
        AtomicLong framesSinceLastThrow = new AtomicLong(0);
        //AtomicDouble projSpeed = new AtomicDouble(speed);

        return (weapon) -> {
            Actor closestEnemy = getClosestActor(actor,actors, FilterTool.Category.ENEMY);
            if (closestEnemy == null) return;

            if (framesSinceLastThrow.getAndIncrement() <= 0 ) {
                if(!weapon.getBody().isActive()) {
                    weapon.getBody().setActive(true);
                }

                weapon.setPosition(actor.getBody().getPosition());
                chaseActor(closestEnemy).act(weapon);
            }

            if(weapon.outOfBounds(actor,boundSquare) || attackedByWeapon(weapon,actors)){
                framesSinceLastThrow.set(-frameInterval);
                weapon.setPosition(actor.getBody().getPosition());
                weapon.getBody().setActive(false);
            }

        };
    }



}

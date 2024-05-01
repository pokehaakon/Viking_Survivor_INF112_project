package GameObjects.ObjectActions;

import GameObjects.Actor;
import Tools.Pool.ObjectPool;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static VikingSurvivor.app.HelloWorld.SET_FPS;

public abstract class KilledAction {

    static public Action doIfDefeated(Action action) {
        return (e) -> {
            if(e.getHP() <= 0) {
                action.act(e);
            }
        };
    }

    /**
     * Handles the despawning of enemies.
     * Enemy despawns when it is out of bounds or killed.
     * @return an ActorAction object
     */
    static public Action destroyIfDefeated() {
        return doIfDefeated(Actor::kill);
    }

    public static Action spawnPickups(
            double prob,
            String type,
            List<Actor> pickups,
            ObjectPool<Actor> pool,
            Action... pickupActions) {

        return doIfDefeated((actor) -> {

            Actor pickup = pool.get(type);
            pickup.setPosition(actor.getBody().getPosition());
            pickup.addAction(pickupActions);

            pickups.add(pickup);

        });
    }

    /**
     * If the actor is under attack, set a cool down period.
     * @param coolDownDuration in milliseconds
     //@param color color the object during the cool down. Default is white
     * @return an actor action object
     */
    public static Action coolDown(double coolDownDuration, Color color) {
        long frameInterval = (long) (coolDownDuration * SET_FPS /1000);
        AtomicLong framesSinceStart = new AtomicLong(0);
        return (actor) -> {
            if(!actor.isUnderAttack())return;

            if(framesSinceStart.getAndIncrement() >= frameInterval) {
                framesSinceStart.set(0);
                actor.setUnderAttack(false);
                actor.getAnimationHandler().setDrawColor(color);
                actor.getHitByIDs().clear();
            }
        };
    }


}

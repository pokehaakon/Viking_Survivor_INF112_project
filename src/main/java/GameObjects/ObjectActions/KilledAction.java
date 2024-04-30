package GameObjects.ObjectActions;

import GameObjects.Actor;
import Tools.Pool.ObjectPool;

import java.util.List;

public abstract class KilledAction {

    static public Action doIfDefeated(Action action) {
        return (e) -> {
            if(e.getHP() <= 0) {
                System.out.println("KILLED");
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
            List<Actor> drawableActors,
            ObjectPool<Actor> pool,
            Action... pickupActions) {

        return doIfDefeated((actor) -> {

            System.out.println("DROP PICKUP");
            Actor pickup = pool.get(type);
            pickup.setPosition(actor.getBody().getPosition());
            pickup.addAction(pickupActions);
            drawableActors.add(pickup);

        });
    }


}

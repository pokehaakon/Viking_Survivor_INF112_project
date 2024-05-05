package GameObjects.ObjectActions;

import GameObjects.Actor;
import com.badlogic.gdx.math.Vector2;

public abstract class MovementActions {

    /**
     * Moves enemy in straight line according to its velocity vector and speed
     *
     */
    public static Action moveInStraightLine(Vector2 vel) {
        var newVel = vel.cpy();
        return e -> e.getBody().setLinearVelocity(newVel.setLength(e.getSpeed()));
    }

    /**
     * Chases an Actor by moving towards its current position
     * @param actor object to be chased
     */
    public static Action chaseActor(Actor actor, float speed) {
        return (e) -> {
            var vel = e.getBody().getLinearVelocity();
            vel
                .set(actor.getBody().getPosition())
                .sub(e.getBody().getWorldCenter())
                .setLength(speed);
            e.getBody().setLinearVelocity(vel);

        };
    }





}

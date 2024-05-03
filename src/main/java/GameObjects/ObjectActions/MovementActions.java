package GameObjects.ObjectActions;

import GameObjects.Actor;
import com.badlogic.gdx.math.Vector2;

import static VikingSurvivor.app.Main.SCREEN_HEIGHT;

public abstract class MovementActions {


    //public static final double DESPAWN_RADIUS =  1.1 * SCREEN_HEIGHT;

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
    public static Action chaseActor(Actor actor) {
        return (e) -> {
            var vel = e.getBody().getLinearVelocity();
            vel
                .set(actor.getBody().getPosition())
                .sub(e.getBody().getWorldCenter())
                .setLength(e.getSpeed());
            e.getBody().setLinearVelocity(vel);

        };
    }
    public static Action chaseActorCustomSpeed(Actor actor, float speed) {
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

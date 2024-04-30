package GameObjects.Actors.ActorAction;

import GameObjects.Actors.Actor;
import com.badlogic.gdx.math.Vector2;

import static Simulation.Coordinates.SpawnCoordinates.randomPointOutsideScreenRect;

public abstract class OutOfBoundsActions {

    static private ActorAction doIfOutOfBounds(Actor centerActor, Vector2 boundSquare, ActorAction action) {
        return (Actor a) -> {
            Vector2 dxdy = centerActor
                    .getBody()
                    .getPosition()
                    .cpy()
                    .sub(a.getBody().getPosition());

            if (Math.abs(dxdy.x) > boundSquare.x / 2 || Math.abs(dxdy.y) > boundSquare.y / 2) {
                action.act(a);
            }
        };
    }

    /**
     * ActorAction which de-spawns the actor if it is out of bounds
     * @param centerActor the actor in the center of the bounds (usually the player)
     * @param boundSquare vector with width and height of the square
     */
    static public ActorAction deSpawnIfOutOfBounds(Actor centerActor, Vector2 boundSquare) {
        return doIfOutOfBounds(centerActor, boundSquare, Actor::kill);
    }

    /**
     * ActorAction which moves the actor if it is out of bounds
     * @param centerActor the actor in the center of the bounds (usually the player)
     * @param boundSquare vector with width and height of the square
     */
    static public ActorAction moveIfOutOfBounds(Actor centerActor, Vector2 boundSquare) {
        return doIfOutOfBounds(
                centerActor,
                boundSquare,
                (a) -> a.setPosition(
                        randomPointOutsideScreenRect(
                                centerActor
                                    .getBody()
                                    .getPosition()
                        )
                )
        );
    }

}

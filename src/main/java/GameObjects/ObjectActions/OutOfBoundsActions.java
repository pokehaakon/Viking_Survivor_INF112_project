package GameObjects.ObjectActions;

import GameObjects.Actor;
import com.badlogic.gdx.math.Vector2;

import static Simulation.Coordinates.SpawnCoordinates.randomPointOutsideScreenRect;

public abstract class OutOfBoundsActions {

    static private Action doIfOutOfBounds(Actor centerActor, Vector2 boundSquare, Action action) {
        return (Actor a) -> {
            if(a.outOfBounds(centerActor,boundSquare)) {
                action.act(a);
            }
        };
    }

    /**
     * ActorAction which de-spawns the actor if it is out of bounds
     * @param centerActor the actor in the center of the bounds (usually the player)
     * @param boundSquare vector with width and height of the square
     */
    static public Action deSpawnIfOutOfBounds(Actor centerActor, Vector2 boundSquare) {
        return doIfOutOfBounds(centerActor, boundSquare, Actor::kill);
    }

    /**
     * ActorAction which moves the actor if it is out of bounds
     * @param centerActor the actor in the center of the bounds (usually the player)
     * @param boundSquare vector with width and height of the square
     */
    static public Action moveIfOutOfBounds(Actor centerActor, Vector2 boundSquare) {
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

    public static Action newPositionIfOutOfBounds(Vector2 pos,Actor centerActor, Vector2 boundSquare) {
        return doIfOutOfBounds(
                centerActor,
                boundSquare,
                (a) -> a.setPosition(pos)
        );
    }

}

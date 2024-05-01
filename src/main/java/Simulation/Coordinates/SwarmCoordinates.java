package Simulation.Coordinates;

import GameObjects.Actor;
import GameObjects.ObjectActions.Action;
import Tools.Tuple;
import com.badlogic.gdx.math.Vector2;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static GameObjects.ObjectActions.MovementActions.moveInStraightLine;
import static Simulation.Coordinates.SpawnCoordinates.randomSpawnPoint;
import static Tools.Tuple.zip;


public abstract class SwarmCoordinates {

    /**
     * Generates a list of coordinates which forms a square grid
     * @param size number actors in the swarm
     * @param startPoint a random point which serves as a referance point for the swarm
     * @param spacing the distance between each actor
     * @return a list of Vector2 objects
     */
    public static List<Vector2> squareSwarm(int size, Vector2 startPoint, float spacing) {
        float sideLength = (float)Math.ceil(Math.sqrt(size));
        List<Vector2> swarmCoordinates = new ArrayList<>();
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                Vector2 newCor = new Vector2(startPoint.x + j * spacing, startPoint.y + i * spacing);
                swarmCoordinates.add(newCor);
            }
        }

        return swarmCoordinates;
    }

    /**
     * Generates a list of coordinates which forms a straight line and is normal to a given target
     * @param size number of actors in the swarm
     * @param spacing distance between the actors
     * @param centerPoint the center of the line
     * @param target position of the target of which the line is normal to
     * @return list of Vector2 objects
     */
    public static List<Vector2> lineSwarm(int size, float spacing, Vector2 centerPoint, Vector2 target) {
        List<Vector2> swarmCoordinates = new ArrayList<>();

        // Calculate the direction vector from centerPoint to targetCoordinate
        Vector2 direction = target
                .cpy()
                .sub(centerPoint)
                .nor();

        // Calculate the perpendicular vector (normal) to the direction vector
        Vector2 normal = direction
                .cpy()
                .rotate90(1);

        // Adjust the starting position based on the direction and normal vectors
        Vector2 start = normal
                .cpy()
                .scl(-(size - 1) / 2f * spacing)
                .add(centerPoint);

        // Create the swarm coordinates based on the adjusted starting position
        for (int i = 0; i < size; i++) {
            swarmCoordinates.add(new Vector2(start.x + i * spacing * normal.x, start.y + i * spacing * normal.y));
        }

        return swarmCoordinates;
    }

    /**
     * Generates the direction of the swarm movement, which is the vector between the swarm center and a given target.
     * @param target defines the direction vector
     * @param swarmType the type of swarm
     * @param swarmCoordinates the start coordinates of the swarm
     * @return a Vector2 representing the swarm direction
     */
    public static Vector2 swarmDirection(Vector2 target, SpawnCoordinates.SwarmType swarmType, List<Vector2> swarmCoordinates) {
        Vector2 swarmDirection = new Vector2();
        Vector2 swarmCenter;

        if(swarmType == SpawnCoordinates.SwarmType.LINE) {
            swarmCenter = swarmCoordinates.get(swarmCoordinates.size()/2);
        }
        else {
            swarmCenter = swarmCoordinates.get((int)Math.ceil(Math.sqrt(swarmCoordinates.size())));
        }

        swarmDirection.add(target).sub(swarmCenter);
        return swarmDirection;
    }

    /**
     * Generates the desired swarm coordinates.
     * @param swarmType the type of swarm
     * @param size number of actors in swarm
     * @param spacing distance between actors
     * @param target the position which the swarm moves towards
     * @return a list of Vector2 which represents the coordinates
     */
     public static List<Vector2> getSwarmCoordinates(Vector2 startPoint, SpawnCoordinates.SwarmType swarmType, int size, float spacing, Vector2 target) {
        if(swarmType == SpawnCoordinates.SwarmType.LINE) {
            return SwarmCoordinates.lineSwarm(size,spacing,startPoint, target);
        }
        else {
            return SwarmCoordinates.squareSwarm(size,startPoint,spacing);
        }
    }

    /**
     * Creates a swarm, a hoard of actors which moves in a straight line.
     * @param swarmType the desired swarm type
     * @param swarmMembers list of Actor object which constitutes the swarm
     * @param center center of spawn circle
     * @param spawnRadius radius of spawn circle
     * @param spacing space between actor
     * @return a List of Actor objects
     */
    public static List<Actor> createSwarm(SpawnCoordinates.SwarmType swarmType, List<Actor> swarmMembers, Vector2 center, double spawnRadius, int spacing, float speedMultiplier) {
        //List<Actor> swarm = new ArrayList<>(swarmMembers);
        Vector2 startPoint = randomSpawnPoint(center, spawnRadius);
        List<Vector2> swarmCoordinates = getSwarmCoordinates(startPoint, swarmType, swarmMembers.size(), spacing, center);
        Vector2 swarmDirection = SwarmCoordinates.swarmDirection(center, swarmType, swarmCoordinates);

        for (var actorCordPair : zip(swarmMembers, swarmCoordinates)) {
            Actor actor = actorCordPair.getValue0();
            actor.setPosition(actorCordPair.getValue1());
            actor.addAction(moveInStraightLine(swarmDirection));
            actor.setSpeed(speedMultiplier);
        }

        return swarmMembers;
    }

    public static Pair<Supplier<Action>, Supplier<Vector2>> swarmInitializerPair(SpawnCoordinates.SwarmType swarmType, int size, Vector2 center, double spawnRadius, float spacing, float speedMultiplier) {
        Vector2 startPoint = randomSpawnPoint(center, spawnRadius);
        List<Vector2> swarmCoordinates = getSwarmCoordinates(startPoint, swarmType, size, spacing, center);
        Vector2 swarmDirection = SwarmCoordinates.swarmDirection(center, swarmType, swarmCoordinates);
        var iter = swarmCoordinates.iterator();
        var action = moveInStraightLine(swarmDirection);

        return Tuple.of(() -> action, iter::next);
    }



}

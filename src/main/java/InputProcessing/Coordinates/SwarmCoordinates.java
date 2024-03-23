package InputProcessing.Coordinates;

import Actors.Enemy.SwarmType;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public abstract class SwarmCoordinates {

    /**
     * Generates a list of coordinates which forms a square grid
     * @param size number enemies in the swarm
     * @param startPoint a random point which serves as a referance point for the swarm
     * @param spacing the distance between each enemy
     * @return a list of Vector2 objects
     */
    public static List<Vector2> squareSwarm(int size, Vector2 startPoint, int spacing) {
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
     * @param size number of enemies in the swarm
     * @param spacing distance between the enemies
     * @param centerPoint the center of the line
     * @param target position of the target of which the line is normal to
     * @return list of Vector2 objects
     */
    public static List<Vector2> lineSwarm(int size, int spacing, Vector2 centerPoint, Vector2 target) {
        List<Vector2> swarmCoordinates = new ArrayList<>();

        // Calculate the direction vector from centerPoint to targetCoordinate
        Vector2 direction = new Vector2(target.x - centerPoint.x, target.y - centerPoint.y).nor();

        // Calculate the perpendicular vector (normal) to the direction vector
        Vector2 normal = new Vector2(-direction.y, direction.x);

        // Adjust the starting position based on the direction and normal vectors
        float startX = centerPoint.x - (size - 1) / 2f * spacing * normal.x;
        float startY = centerPoint.y - (size - 1) / 2f * spacing * normal.y;

        // Create the swarm coordinates based on the adjusted starting position
        for (int i = 0; i < size; i++) {
            swarmCoordinates.add(new Vector2(startX + i * spacing * normal.x, startY + i * spacing * normal.y));
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
    public static Vector2 swarmDirection(Vector2 target, SwarmType swarmType, List<Vector2> swarmCoordinates) {
        Vector2 swarmDirection = new Vector2();
        Vector2 swarmCenter;

        if(swarmType == SwarmType.LINE) {
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
     * @param size number of enemies in swarm
     * @param spacing distance between enemies
     * @param target the position which the swarm moves towards
     * @return a list of Vector2 which represents the coordinates
     */
    public static List<Vector2> getSwarmCoordinates(SwarmType swarmType, int size, int spacing, Vector2 target) {
        Vector2 startPoint = RandomCoordinates.randomPoint(target);
        if(swarmType == SwarmType.LINE) {
            return SwarmCoordinates.lineSwarm(size,spacing,startPoint, target);
        }
        else {
            return SwarmCoordinates.squareSwarm(size,startPoint,spacing);
        }
    }
}

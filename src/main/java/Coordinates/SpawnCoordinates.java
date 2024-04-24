package Coordinates;

import GameObjects.GameObject;
import GameObjects.StaticObjects.Terrain;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

import static VikingSurvivor.app.Main.SCREEN_HEIGHT;
import static VikingSurvivor.app.Main.SCREEN_WIDTH;

public abstract class SpawnCoordinates extends Vector2 {



    /**

     Generates a random point on a circular region defined by a center and radius.
     @param center the center point around which the circular region is defined
     @param radius radius of spawn circle
     @return a Vector2 representing a randomly generated point within the circular region
     */
    public static Vector2 randomSpawnPoint(Vector2 center, double radius) {

        double angle = Math.random() * 2 * Math.PI;

        //double angle = Math.toRadians(Math.random() * 360);
        double x = center.x + radius * Math.cos(angle);
        double y = center.y + radius * Math.sin(angle);

        return new Vector2((float)x,(float)y);
    }


    /**
     * Generates a list of spawn points, forming a square around the screen. If the number of desired spawn points exceeds
     * the number of available spawn points, then the method simply returns the available spawn points.
     * @param n number of spawn points to generate
     * @param distX distance between center and line of points on left and right side of screen
     * @param distY distance between center and line of points on top and bottom of screen
     * @param distanceBetweenPoints minimum distance between the spawn points
     * @param center player's position
     * @param occupiedSpawns list of Vector2 positions, which is the occupied spawns
     * @return a list of Vector2 objects
     */
    public static List<Vector2> fixedSpawnPoints(int n,float distX, float distY, float distanceBetweenPoints, Vector2 center, List<Vector2> occupiedSpawns) {
        List<Vector2> spawnPoints = new ArrayList<>();

        float startX = center.x - distX;
        float startY = center.y - distY;
        float endX = center.x + distX;
        float endY = center.y + distY;


        int numPointsX = (int) Math.ceil((2*distX)/ distanceBetweenPoints) +1 ;
        int numPointsY = (int) Math.floor((2*distY) / distanceBetweenPoints) ;

        // adding points to top and bottom
        for(int i = 0; i < numPointsX; i++) {
            // to bottom
            Vector2 bottom = new Vector2(startX + i * distanceBetweenPoints, startY);
            if(availableSpawn(bottom, occupiedSpawns,distanceBetweenPoints)) {
                spawnPoints.add(bottom);
            }
            Vector2 top = new Vector2(startX + i * distanceBetweenPoints, endY);
            // to top
            if(availableSpawn(top, occupiedSpawns,distanceBetweenPoints)) {
                spawnPoints.add(top);
            }
        }

        //adding points to left and right side
        for(int i = 1; i < numPointsY; i++) {

            // left side
            Vector2 left = new Vector2(startX, startY + i * distanceBetweenPoints);
            if(availableSpawn(left, occupiedSpawns,distanceBetweenPoints)) {
                spawnPoints.add(left);
            }

            // right side
            Vector2 right = new Vector2(endX, startY + i * distanceBetweenPoints);
            if(availableSpawn(right, occupiedSpawns,distanceBetweenPoints)) {
                spawnPoints.add(right);
            }
        }

        Collections.shuffle(spawnPoints);

        if(n <= spawnPoints.size()){
            return spawnPoints.subList(0,n);
        }

        return spawnPoints;

    }


    /**
     * Checks if spawn point is available - if it is greater than a certain distance from another spawn
     * @param potentialSpawn the spawn point to check
     * @param occupiedPositions a list of occupied spawn positions
     * @param minDistanceBetween distance threshold between the potential spawn and the occupied spawn
     * @return true if spawn is available
     */
    public static boolean availableSpawn(Vector2 potentialSpawn, List<Vector2> occupiedPositions, float minDistanceBetween) {
        if(minDistanceBetween <= 0) {
            throw new IllegalArgumentException("Distance between must be greater than zero!");
        }
        boolean available = true;
        for(Vector2 pos : occupiedPositions) {
            if (distance(pos, potentialSpawn) < minDistanceBetween) {
                available = false;
                break;
            }
        }
        return available;
    }

    /**
     * Calculates distance between two vector2 objects
     * @param a
     * @param b
     * @return a float
     */
    private static float distance(Vector2 a, Vector2 b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return (float)Math.sqrt(dx * dx + dy * dy);
    }



    public static <T extends GameObject<E>,E extends Enum<E>> List<Vector2> getOccupiedPositions(List<T> spawnedGameObjects) {
        List<Vector2> positions = new ArrayList<>();
        for(T obj : spawnedGameObjects) {
            positions.add(obj.getBody().getPosition());
        }
        return positions;
    }


}


package Coordinates;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Coordinates.SpawnCoordinates.fixedSpawnPoints;
import static org.junit.jupiter.api.Assertions.*;

class SpawnCoordinatesTest {


    List<Vector2> occupiedPositions;

    float minDistance;

    Vector2 potentialSpawn;

    @BeforeEach
    void setUp() {
         occupiedPositions = new ArrayList<>();
        //potentialSpawn = Arrays.asList(new Vector2(0,0),new Vector2(0,100));
        //occupiedPositions = Arrays.asList(new Vector2(0,0), new Vector2(100,))



    }

    @Test
    void testAvailableSpawn_EmptyOccupiedPositions() {;
        Vector2 potentialSpawn = new Vector2(0,0);
        assertTrue(SpawnCoordinates.availableSpawn(potentialSpawn,occupiedPositions,200));
    }

    @Test
    void availableSpawn_tooClose() {
        occupiedPositions.add(new Vector2(0,0));
        occupiedPositions.add(new Vector2(6,0));
        potentialSpawn = new Vector2(4,0);
        minDistance = 5f;
        assertFalse(SpawnCoordinates.availableSpawn(potentialSpawn,occupiedPositions,minDistance));
    }

    @Test
    void availableSpawn_minDistance() {
        occupiedPositions.add(new Vector2(0,0));
        potentialSpawn = new Vector2(4,0);
        minDistance = 4f;
        assertTrue(SpawnCoordinates.availableSpawn(potentialSpawn,occupiedPositions,minDistance));
    }

    @Test
    void listSize() {
        // testing when number of desired points is less than available coordinates
        List<Vector2> myPoints = fixedSpawnPoints(1,1,1,1,new Vector2(0,0),occupiedPositions);
        assertTrue(myPoints.size() == 1);
    }
    @Test
    void maksNumberOfAvailablePoints() {
        // if n is greater than number of available points, the function should simply return the list of available points
        List<Vector2> myPoints1 = fixedSpawnPoints(10,1,1,1,new Vector2(0,0),occupiedPositions);

        List<Vector2> myPoints2 = fixedSpawnPoints(20,1,1,1,new Vector2(0,0),occupiedPositions);
        assertTrue(myPoints1.size() == myPoints2.size());
    }

    @ParameterizedTest
    @MethodSource("spawnData")
    void correctPoints(float distX, float distY, float distanceBetweenPoints, List<Vector2> occupiedPositions, List<Vector2> expectedPoints) {
        List<Vector2> myPoints = fixedSpawnPoints(100,distX,distY,distanceBetweenPoints,new Vector2(0,0),occupiedPositions);
        for(Vector2 pos : myPoints) {
            assertTrue(expectedPoints.contains(pos));
        }
        assertEquals(myPoints.size(),expectedPoints.size());

    }



    @Test
    void availableSpawn() {


    }

    @Test
    void getOccupiedPositions() {
    }

    private static List<Object[]> spawnData() {
        return Arrays.asList(
                // no occupied positions
                new Object[]{1,1,1,
                        new ArrayList<>(),
                        Arrays.asList(
                        new Vector2(-1, 1),
                        new Vector2(-1, 0),
                        new Vector2(-1, -1),
                        new Vector2(0, 1),
                        new Vector2(0,-1),
                        new Vector2(1,1),
                        new Vector2(1,0),
                        new Vector2(1,-1)
                )},
                // two occupied positions
                new Object[]{1,1,1,
                        Arrays.asList(new Vector2(1,0.5f),new Vector2(1,-0.5f)),
                        Arrays.asList(
                                new Vector2(-1, 1),
                                new Vector2(-1, 0),
                                new Vector2(-1, -1),
                                new Vector2(0, 1),
                                new Vector2(0,-1)
                )},


                new Object[]{1,1,1.5f,
                        new ArrayList<>(),
                        Arrays.asList(
                                new Vector2(-1, -1),
                                new Vector2(0.5f, -1),
                                new Vector2(2, -1),
                                new Vector2(-1, 1),
                                new Vector2(0.5f,1),
                                new Vector2(2,1)
                        )}
        );
    }
}
package InputProcessing.Coordinates;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpawnCoordinatesTest {

    @Test
    void randomSpawnPoint() {

        Vector2 center = new Vector2(0,0);
        double radius = 100;
        for (int i = 0; i < 50; i++) {
            Vector2 spawnPoint = SpawnCoordinates.randomSpawnPoint(center, radius);
            assertTrue(spawnPoint.x >= -100 && spawnPoint.x <= 100); // Within x range
            assertTrue(spawnPoint.y >= -100 && spawnPoint.y <= 100); // Within y range
            double dist = Math.sqrt(spawnPoint.x * spawnPoint.x + spawnPoint.y * spawnPoint.y);

            // give it some slack
            assertTrue(dist >= 99.9);
            assertTrue(dist <= 100.1);
        }

    }
}
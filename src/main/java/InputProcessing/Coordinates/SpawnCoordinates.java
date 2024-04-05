package InputProcessing.Coordinates;

import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import static VikingSurvivor.app.Main.SCREEN_HEIGHT;

public abstract class SpawnCoordinates extends Vector2 {



    public SpawnCoordinates(float x, float y) {
        super(x,y);
    }


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



}


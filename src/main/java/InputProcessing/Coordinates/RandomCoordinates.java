package InputProcessing.Coordinates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public abstract class RandomCoordinates extends Vector2 {

    public static final double SPAWN_RADIUS = (double)0.7*Gdx.graphics.getWidth();
    public static final double DESPAWN_RADIUS = (double) 1.1*Gdx.graphics.getWidth();

    public RandomCoordinates(float x, float y) {
        super(x,y);
    }


    /**

     Generates a random point within a circular region defined by an inner and outer radius around a specified center point.
     @param center the center point around which the circular region is defined
     @return a Vector2 representing a randomly generated point within the circular region
     */
    public static Vector2 randomPoint(Vector2 center) {

        double angle = Math.toRadians(Math.random() * 360);
        double randomRadius = SPAWN_RADIUS + Math.random() * (DESPAWN_RADIUS - SPAWN_RADIUS);
        double x = center.x + SPAWN_RADIUS * Math.cos(angle);
        double y = center.y + SPAWN_RADIUS * Math.sin(angle);

        return new Vector2((float)x,(float)y);
    }

    /**
     *
     * @param num number of points
     * @param center the center point around which the circular region is defined
     * @return a list of random points outside of screen
     */
    public static List<Vector2> randomPoints(int num, Vector2 center) {
        List<Vector2> points = new ArrayList<>();
        for(int i = 0; i < num; i++) {
            points.add(randomPoint(center));
        }
        return points;
    }




}


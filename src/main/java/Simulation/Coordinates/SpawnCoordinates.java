package Simulation.Coordinates;

import Contexts.ReleaseCandidateContext;
import com.badlogic.gdx.math.Vector2;

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

    public static Vector2 randomPointOutsideScreenRect(Vector2 center) {
        //Vector2 V = ReleaseCandidateContext.SPAWN_RECT.cpy().add(ReleaseCandidateContext.DE_SPAWN_RECT).scl(0.5f);
        float W = ReleaseCandidateContext.SPAWN_RECT.x + ReleaseCandidateContext.DE_SPAWN_RECT.x;
        float H = ReleaseCandidateContext.SPAWN_RECT.y + ReleaseCandidateContext.DE_SPAWN_RECT.y;


        float len = (float) Math.random() * (W + H);
        W = W/2;
        H = H/2;
        Vector2 pos = new Vector2(-W/2, -H/2).add(center);

        if(len < W) {
           return pos.add(len, 0);
        }
        len -= W;
        if(len < H) {
            return pos.add(W, len);
        }
        len -= H;
        if(len < W) {
            return pos.add(W - len, H);
        }
        len -= W;
        return pos.add(0, H - len);

    }

    public enum SwarmType {
        SQUARE,
        LINE
    }
}


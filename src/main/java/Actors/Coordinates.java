package Actors;

import Actors.Player.PlayerExample;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Coordinates extends Vector2 {

    public Coordinates(float x, float y) {

        super(x,y);
    }

    /**
     * Creating a list of coordinates for the swarm
     * @param numMembers
     * @param startPoint
     * @return a list of coordinates that forms a square grid
     */
    public static List<Vector2> squareSwarm(int numMembers, Vector2 startPoint, int spacing) {
        float sideLength = (float)Math.ceil(Math.sqrt(numMembers));
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
     *
     * @param numMembers
     * @param centerPoint
     * @param spacing
     * @return list coordinates that forms a line
     */
    public static List<Vector2> lineSwarm(int numMembers,Vector2 centerPoint, int spacing) {
        List<Vector2> swarmCoordinates = new ArrayList<>();

        //want the normal of the line to always face the middle of the screen
        Vector2 target = new Vector2((float)Gdx.graphics.getWidth()/2,(float)Gdx.graphics.getHeight()/2);


        // Calculate the direction vector from centerPoint to targetCoordinate
        Vector2 direction = new Vector2(target.x - centerPoint.x, target.y - centerPoint.y).nor();

        // Calculate the perpendicular vector (normal) to the direction vector
        Vector2 normal = new Vector2(-direction.y, direction.x);

        // Adjust the starting position based on the direction and normal vectors
        float startX = centerPoint.x - (numMembers - 1) / 2f * spacing * normal.x;
        float startY = centerPoint.y - (numMembers - 1) / 2f * spacing * normal.y;

        // Create the swarm coordinates based on the adjusted starting position
        for (int i = 0; i < numMembers; i++) {
            swarmCoordinates.add(new Vector2(startX + i * spacing * normal.x, startY + i * spacing * normal.y));
        }

        return swarmCoordinates;
    }


    /**
     *
     * @return a random coordinates outside of screen
     */
    public static Vector2 randomPoint() {

        Vector2 center = new Vector2((float)Gdx.graphics.getWidth()/2, (float)Gdx.graphics.getHeight()/2);
        double innerRadius = (float)Gdx.graphics.getWidth()/2;
        double outerRadius = 0.7*Gdx.graphics.getWidth();
        double angle = Math.toRadians(Math.random() * 360);
        double randomRadius = innerRadius + Math.random() * (outerRadius - innerRadius);
        double x = center.x + randomRadius * Math.cos(angle);
        double y = center.y + randomRadius * Math.sin(angle);

        return new Vector2((float)x,(float)y);

    }




}


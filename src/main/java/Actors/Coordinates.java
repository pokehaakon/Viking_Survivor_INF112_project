package Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Coordinates extends Vector2 {

//    public static final List<Vector2> spawnPoints = Arrays.asList(
//
//            // along left side of screen
//            new Vector2((float)-0.5*Gdx.graphics.getWidth(),0),
//            new Vector2((float)-0.5*Gdx.graphics.getWidth(), (float)0.5*Gdx.graphics.getHeight()),
//            new Vector2((float)-0.5*Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight()),
//
//            // along top of screen
//            new Vector2(0, (float)1.5*Gdx.graphics.getHeight()),
//            new Vector2((float)0.5*Gdx.graphics.getWidth(), (float)1.5*Gdx.graphics.getHeight()),
//            new Vector2((float)Gdx.graphics.getWidth(), (float)1.5*Gdx.graphics.getHeight()),
//
//
//            // along right side of screen
//            new Vector2((float)1.5*Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight()),
//            new Vector2((float)1.5*Gdx.graphics.getWidth(), (float)0.5*Gdx.graphics.getHeight()),
//            new Vector2((float)1.5*Gdx.graphics.getWidth(), 0),
//
//            //along bottom of screen
//            new Vector2(0, (float)-0.5*Gdx.graphics.getHeight()),
//            new Vector2((float)0.5*Gdx.graphics.getWidth(), (float)-0.5*Gdx.graphics.getHeight()),
//            new Vector2((float)Gdx.graphics.getWidth(), (float)-0.5*Gdx.graphics.getHeight())
//    );


    public Coordinates(float x, float y) {
        super(x,y);
    }

    /**
     *
     * @return random start coordinate from the spawn points list
     */
    public static Vector2 random() {
        Random random = new Random();
        int randomIndex = random.nextInt(spawnPoints().size());
        return spawnPoints().get(randomIndex);
    }

    /**
     * Creating a list of coordinates for the swarm
     * @param numMembers
     * @param startPoint
     * @return
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
    public static List<Vector2> lineSwarm(int numMembers, Vector2 startPoint, int spacing) {
        List<Vector2> swarmCoordinates = new ArrayList<>();

    }
    private static List<Vector2> spawnPoints() {
        return Arrays.asList(

                // along left side of screen
                new Vector2((float)-0.5*Gdx.graphics.getWidth(),0),
                new Vector2((float)-0.5*Gdx.graphics.getWidth(), (float)0.5*Gdx.graphics.getHeight()),
                new Vector2((float)-0.5*Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight()),

                // along top of screen
                new Vector2(0, (float)1.5*Gdx.graphics.getHeight()),
                new Vector2((float)0.5*Gdx.graphics.getWidth(), (float)1.5*Gdx.graphics.getHeight()),
                new Vector2((float)Gdx.graphics.getWidth(), (float)1.5*Gdx.graphics.getHeight()),


                // along right side of screen
                new Vector2((float)1.5*Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight()),
                new Vector2((float)1.5*Gdx.graphics.getWidth(), (float)0.5*Gdx.graphics.getHeight()),
                new Vector2((float)1.5*Gdx.graphics.getWidth(), 0),

                //along bottom of screen
                new Vector2(0, (float)-0.5*Gdx.graphics.getHeight()),
                new Vector2((float)0.5*Gdx.graphics.getWidth(), (float)-0.5*Gdx.graphics.getHeight()),
                new Vector2((float)Gdx.graphics.getWidth(), (float)-0.5*Gdx.graphics.getHeight())
        );
    }




}


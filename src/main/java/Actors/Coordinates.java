package Actors;

import Actors.Enemy.EnemyTypes.EnemyType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Coordinates extends Vector2 {

    public static final List<Vector2> spawnPoints = Arrays.asList(
            new Vector2((float)-0.5*Gdx.graphics.getWidth(),0),
            new Vector2((float)-0.5*Gdx.graphics.getWidth(), (float)0.5*Gdx.graphics.getHeight()),
            new Vector2((float)-0.5*Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight()),

            new Vector2(0, (float)1.5*Gdx.graphics.getHeight()),
            new Vector2((float)0.5*Gdx.graphics.getWidth(), (float)1.5*Gdx.graphics.getHeight()),
            new Vector2((float)Gdx.graphics.getWidth(), (float)1.5*Gdx.graphics.getHeight()),

            new Vector2((float)1.5*Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight()),
            new Vector2((float)1.5*Gdx.graphics.getWidth(), (float)0.5*Gdx.graphics.getHeight()),
            new Vector2((float)1.5*Gdx.graphics.getWidth(), 0),

            new Vector2(0, (float)-0.5*Gdx.graphics.getHeight()),
            new Vector2((float)0.5*Gdx.graphics.getWidth(), (float)-0.5*Gdx.graphics.getHeight()),
            new Vector2((float)Gdx.graphics.getWidth(), (float)-0.5*Gdx.graphics.getHeight())
    );


    public Coordinates(float x, float y) {
        super(x,y);
    }
    public static Vector2 random() {
        Random random = new Random();
        int randomIndex = random.nextInt(spawnPoints.size());
        return spawnPoints.get(randomIndex);
    }


    public static List<Vector2> swarm(int hordeSize,Vector2 startPoint) {
        Random random = new Random();
        double sideLength = Math.sqrt(hordeSize);
        List<Vector2> hordeCoordinates = new ArrayList<>();

        int randomDistance = 60;

        for (int i = 0; i < Math.floor(sideLength); i++) {
            for (int j = 0; j < Math.ceil(sideLength); j++) {
                Vector2 newCor = new Vector2(startPoint.x + j * randomDistance, startPoint.y + i * randomDistance);
                hordeCoordinates.add(newCor);
            }

        }
        return hordeCoordinates;

        //test this: number of coordinates in list
    }



}


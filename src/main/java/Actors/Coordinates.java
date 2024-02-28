package Actors;

import Actors.Enemy.EnemyTypes.EnemyType;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Coordinates {
    Random random;
    public int x;
    public int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public static Coordinates random() {
        int minX = - Gdx.graphics.getWidth();
        int maxX = 2*Gdx.graphics.getWidth();
        int minY = -Gdx.graphics.getHeight();
        int maxY = 2*Gdx.graphics.getHeight();

        int randomX = new Random().nextInt(maxX - minX + 1) + minX;
        int randomY = new Random().nextInt(maxY - minY + 1) + minY;
        return new Coordinates(randomX, randomY);
    }



    public static List<Coordinates> swarm(int hordeSize,Coordinates startPoint) {
        Random random = new Random();
        double sideLength = Math.sqrt(hordeSize);
        List<Coordinates> hordeCoordinates = new ArrayList<>();

        int distanceBetween = 60;

        for (int i = 0; i < Math.floor(sideLength); i++) {
            for (int j = 0; j < Math.ceil(sideLength); j++) {
                int randomSpacing = random.nextInt( 60 -30) + 30;
                Coordinates newCor = new Coordinates(startPoint.x + j * distanceBetween, startPoint.y + i * distanceBetween);
                hordeCoordinates.add(newCor);
            }

        }
        if(hordeSize%2 == 0) {
            return hordeCoordinates;
        }
        else{
            return hordeCoordinates.subList(0, hordeSize);
        }

        //test this: number of coordinates in list

    }

}


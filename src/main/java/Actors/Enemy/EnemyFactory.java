package Actors.Enemy;

import Actors.Enemy.Enemies.Enemy1;
import Actors.Enemy.Enemies.Enemy2;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EnemyFactory{

    private List<Enemy> enemyList = new ArrayList<>();

    public Enemy create(String type){

        Coordinates randomCor = randomCoordinates(0, Gdx.graphics.getWidth(),0,Gdx.graphics.getHeight());
        if(type == null) {
            return null;
        }
        Enemy enemy;
        switch (type.toUpperCase()) {
            case "ENEMY1" -> {
                enemy = new Enemy1(randomCor.x, randomCor.y);
                enemyList.add(enemy);
                 // Add the created enemy to the list
                return enemy;
            }
            case "ENEMY2" -> {
                enemy = new Enemy2(randomCor.x, randomCor.y);
                 // Add the created enemy to the list
                enemyList.add(enemy);
                return enemy;
            }
            default -> throw new IllegalArgumentException("Invalid enemy type");
        }
    }

    public void createRandom() {
        List<String> enemyList = Arrays.asList("enemy1", "enemy2");
        String chosenEnemy = enemyList.get(new Random().nextInt(enemyList.size()));
        create(chosenEnemy);

    }

    private static Coordinates randomCoordinates(int minX, int maxX, int minY, int maxY) {

        int randomX = new Random().nextInt(maxX - minX + 1) + minX;
        int randomY = new Random().nextInt(maxY - minY + 1) + minY;

        return new Coordinates(randomX, randomY);
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }

}

class Coordinates {
    public int x;
    public int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
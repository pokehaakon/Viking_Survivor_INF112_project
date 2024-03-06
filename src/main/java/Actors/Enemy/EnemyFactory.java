package Actors.Enemy;


import Actors.Coordinates;
import Actors.Enemy.EnemyTypes.*;
import Actors.Stats;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

import static Actors.Enemy.Enemy.SWARM_SPEED_MULTIPLIER;


public class EnemyFactory implements IEnemyFactory{


    private final static List<String> enemyTypes = Arrays.asList("ENEMY1", "ENEMY2");



    private static Enemy createEnemyType(String type, int x, int y){

        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Enemy enemy = switch (type.toUpperCase()) {
            case "ENEMY1" -> new Enemy(Stats.enemy1(),"img.png", x,y);
            case "ENEMY2" -> new Enemy(Stats.enemy2(),"img_3.png",x,y);
            default -> throw new IllegalArgumentException("Invalid enemy type");
        };
        return enemy;
    }

    public static List<Enemy> createEnemies(int count, String type) {

        List<Enemy> enemies = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Enemy newEnemy = createEnemyType(type, (int)Coordinates.random().x, (int)Coordinates.random().y);
            enemies.add(newEnemy);
        }
        return enemies;
    }


    //things to test: number of enemies, total and in map, null values, remove function, not real types etc... 0 at start etc, minus 1 after destroyed


    private static String randomEnemyType() {
        Random random = new Random();
        int randomIndex = random.nextInt(enemyTypes.size());
        return enemyTypes.get(randomIndex);
    }


    public static List<Enemy> createRandomEnemies(int count) {
        List<Enemy> enemyList = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            Enemy enemy = createEnemyType(randomEnemyType(), (int)Coordinates.random().x, (int)Coordinates.random().y);
            enemyList.add(enemy);
        }
        return enemyList;
    }


    public static List<Enemy> createSwarm(int count, String type) {
        List<Enemy> swarm = new ArrayList<>();
        List<Vector2> swarmPoints = Coordinates.squareSwarm(count, new Vector2(0,0), 60);

        for(int i = 0; i < count; i++) {
            Enemy enemy = createEnemyType(type,(int)swarmPoints.get(i).x , (int)swarmPoints.get(i).y);
            makeSwarmMember(enemy);
            swarm.add(enemy);
        }
        return swarm;
    }

    /**
     * turns enemy into a swarm member by changing state and speed
     * @param enemy
     */
    private static void makeSwarmMember(Enemy enemy) {
        enemy.state = EnemyState.SWARM_MEMBER;
        enemy.speedX *= SWARM_SPEED_MULTIPLIER;
        enemy.speedY *= SWARM_SPEED_MULTIPLIER;
    }

}


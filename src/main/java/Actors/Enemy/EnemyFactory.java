package Actors.Enemy;


import Actors.Coordinates;
import Actors.Enemy.EnemyTypes.*;
import Actors.Player.PlayerExample;
import Actors.Stats;
import com.badlogic.gdx.math.Vector2;

import java.util.*;


public class EnemyFactory implements IEnemyFactory{

    private final static List<String> enemyTypes = Arrays.asList("ENEMY1", "ENEMY2");


    @Override
    public Enemy createEnemyType(String type, float x, float y){

        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Enemy enemy = switch (type.toUpperCase()) {
            case "ENEMY1" -> new Enemy(Stats.enemy1(),Sprites.ENEMY_1_PNG, x,y, 100, 100);
            case "ENEMY2" -> new Enemy(Stats.enemy2(),Sprites.ENEMY_2_PNG,x,y, 100, 100);
            default -> throw new IllegalArgumentException("Invalid enemy type");
        };
        return enemy;
    }

    @Override
    public List<Enemy> createEnemies(int count, String type) {

        List<Enemy> enemies = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Vector2 startPoint = Coordinates.randomPoint();
            Enemy newEnemy = createEnemyType(type, startPoint.x, startPoint.y);
            enemies.add(newEnemy);
        }
        return enemies;
    }


    private  String randomEnemyType() {
        Random random = new Random();
        int randomIndex = random.nextInt(enemyTypes.size());
        return enemyTypes.get(randomIndex);
    }


    @Override
    public  List<Enemy> createRandomEnemies(int count) {
        List<Enemy> enemyList = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            Vector2 startPoint = Coordinates.randomPoint();
            Enemy enemy = createEnemyType(randomEnemyType(), startPoint.x, startPoint.y);
            enemyList.add(enemy);
        }
        return enemyList;
    }

    @Override
    public Swarm createSwarm(int numMembers, String enemyType, SwarmType swarmType) {
        Swarm swarm = new Swarm();
        Vector2 startPoint = Coordinates.randomPoint();
        List<Vector2> swarmPoints;

        if(swarmType == SwarmType.SQUARE) {
            swarm.setSwarmType(SwarmType.SQUARE);
            swarmPoints = Coordinates.squareSwarm(numMembers,startPoint, 60);
        }
        else if(swarmType == SwarmType.LINE){
            swarm.setSwarmType(SwarmType.LINE);
            swarmPoints = Coordinates.lineSwarm(numMembers,startPoint, 60, new Vector2(PlayerExample.x, PlayerExample.y));
        }
        else{
            throw new IllegalArgumentException("Cannot find swarm type");
        }

        for(int i = 0; i < numMembers; i++) {
            Enemy enemy = createEnemyType(enemyType,swarmPoints.get(i).x , swarmPoints.get(i).y);
            swarm.add(enemy);
        }
        return swarm;
    }


}


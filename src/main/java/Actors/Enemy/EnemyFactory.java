package Actors.Enemy;


import Actors.Coordinates;
import Actors.Enemy.EnemyTypes.*;
import Actors.Stats;
import com.badlogic.gdx.math.Vector2;

import java.util.*;



public class EnemyFactory implements IEnemyFactory{

    private List<Enemy> createdEnemies;
    private final EnemyType[] enemySelection;
    private final Random random = new Random();



    private Map<EnemyType, Integer> enemyTypeMap;
    public EnemyFactory() {
        createdEnemies = new ArrayList<>();
        enemySelection = EnemyType.values();
        enemyTypeMap = new HashMap<>();

        for (EnemyType enemyType : enemySelection) {
            enemyTypeMap.put(enemyType, 0);
        }
    }

    @Override
    public Enemy createEnemyType(EnemyType type, int x, int y){

        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Enemy enemy = switch (type) {
            case ENEMY1 -> new Enemy1(x,y,Stats.enemy1());
            case ENEMY2 -> new Enemy2(x,y,Stats.enemy2());
            default -> throw new IllegalArgumentException("Invalid enemy type");
        };
        addEnemyToInventory(enemy);
        return enemy;
    }

    private void addEnemyToInventory(Enemy enemy) {
        createdEnemies.add(enemy);
        enemyTypeMap.put(enemy.enemyType, enemyTypeMap.get(enemy.enemyType)+1);
    }

    //things to test: number of enemies, total and in map, null values, remove function, not real types etc... 0 at start etc, minus 1 after destroyed


    private EnemyType randomEnemyType() {
        int randomIndex = random.nextInt(enemySelection.length);
        return enemySelection[randomIndex];
    }

    @Override
    public void createRandomEnemies(int count) {

        for(int i = 0; i < count; i++) {
            Vector2 random = Coordinates.random();
            createEnemyType(randomEnemyType(), (int)random.x, (int)random.y);
        }
    }

    @Override
    public List<Enemy> getCreatedEnemies() {
        return createdEnemies;
    }


    public void createSwarm(int count, EnemyType type) {
        List<Vector2> swarmPoints = Coordinates.swarm(count, Coordinates.random());
        System.out.println(swarmPoints.size());
        for(int i = 0; i < count; i++) {
            Enemy enemy = createEnemyType(type,(int)swarmPoints.get(i).x , (int)swarmPoints.get(i).y);
            enemy.makeSwarmMember();
        }
    }


    @Override
    public int getEnemyTypeCount(EnemyType type) {
        return enemyTypeMap.get(type);
    }

    @Override
    public void removeDestroyedEnemies() {
        Iterator<Enemy> iterator = createdEnemies.iterator();
        while(iterator.hasNext()) {
            Enemy enemy =  iterator.next();
            EnemyType type = enemy.enemyType;
            if(enemy.isDestroyed()) {
                iterator.remove();
                enemyTypeMap.put(type, enemyTypeMap.get(type)-1);
            }
        }

    }


    @Override
    public int getEnemyCount() {
        return createdEnemies.size();
    }


}


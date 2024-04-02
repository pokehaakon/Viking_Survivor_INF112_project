package Actors.Enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.*;

public class EnemyPool {

    private final Map<String, Queue<Enemy>> enemyPool;

    private static final List<String> ENEMY_TYPES = Arrays.asList(
            "ENEMY1",
            "ENEMY2"
    );


    private final Random random;
    private final World world;

    /**
     * An enemy pool is a hash map of enemy types as keys and linked list of Enemy objects as values
     * When we start the game, we create and store a desired amount of each enemy type
     * We use this pool to recirculate enemies, so we don't have to create new instances every time an enemy spawns
     * @param world the current world
     * @param poolSize number of Enemy objects for each enemy type
     */
    public EnemyPool(World world, int poolSize) {
        this.world = world;
        enemyPool = new HashMap<>();
        random = new Random();

        for(String enemyType : ENEMY_TYPES) {
            createEnemyPool(enemyType,poolSize);
        }
    }

    private void createEnemyPool(String enemyType, int size) {


        Queue<Enemy> pool = new LinkedList<>();
        for(Enemy enemy : EnemyFactory.create(size, enemyType)) {

            enemy.addToWorld(world, new Vector2());
            enemy.getBody().setActive(false);
            pool.add(enemy);
        }

        enemyPool.put(enemyType, pool);
    }

    private Enemy getRandomEnemy() {
        if (enemyPool.isEmpty()) {
            return null;
        }
        String randomEnemyType = ENEMY_TYPES.get(random.nextInt(ENEMY_TYPES.size()));
        return getEnemy(randomEnemyType);
    }

    private Enemy getEnemy(String enemyType) {
        Queue<Enemy> pool = enemyPool.get(enemyType);
        if (!pool.isEmpty()) {
            return pool.poll();
        }
        else {
            Enemy enemy = EnemyFactory.create(enemyType);
            enemy.addToWorld(world, new Vector2());
            return enemy;
        }
    }

    /**
     * Polls random enemies from the enemy pool and activate their bodies
     * @param num number of enemies to obtain
     * @return a list of Enemy objects
     */
    public List<Enemy> getRandomEnemies(int num) {
        List<Enemy> enemies = new ArrayList<>();
        for(int i = 0; i < num; i++) {
            Enemy enemy = getRandomEnemy();
            enemy.getBody().setActive(true);
            enemies.add(enemy);
        }
        return enemies;
    }

    /**
     * Polls enemies from the enemy pool and activate their bodies
     * @param enemyType desired enemy type
     * @param num number of enemies to obtain
     * @return a list of Enemy objects
     */
    public List<Enemy> getEnemies(String enemyType, int num) {
        System.out.println(enemyPool.get(enemyType).size());
        List<Enemy> enemies = new ArrayList<>();
        for(int i = 0; i < num; i++) {
            Enemy enemy = getEnemy(enemyType);
            enemy.getBody().setActive(true);
            enemies.add(enemy);
        }
        return enemies;
    }


    /**
     * Returns enemy to enemy pool, deactivates their bodies, resets its actions and its destroyed-tag.
     * @param enemy the Enemy object
     */
    public void returnEnemy(Enemy enemy) {
        Queue<Enemy> pool = enemyPool.get(enemy.getEnemyType());
        if (pool != null) {
            enemy.getBody().setActive(false);
            enemy.revive();
            enemy.resetActions();

            // Return the enemy to the pool
            pool.add(enemy);
        }
    }


    public Map<String, Queue<Enemy>> getEnemyPool() {
        return enemyPool;
    }

}




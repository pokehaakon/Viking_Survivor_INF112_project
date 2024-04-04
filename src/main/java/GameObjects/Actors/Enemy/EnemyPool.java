package GameObjects.Actors.Enemy;

import GameObjects.Factories.EnemyFactory;
import GameObjects.GameObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.*;

public class EnemyPool {

    private final Map<String, Queue<Enemy>> enemyPool;

    public static final List<String> ENEMY_TYPES = Arrays.asList(
            "ENEMY1",
            "ENEMY2"
    );


    private final Random random;
    private final World world;

    private EnemyFactory enemyFactory;

    /**
     * An enemy pool is a hash map of enemy types as keys and linked list of Enemy objects as values
     * When we start the game, we create and store a desired amount of each enemy type
     * We use this pool to recirculate enemies, so we don't have to create new instances every time an enemy spawns
     * @param world the current world
     * @param poolSize number of Enemy objects for each enemy type
     */
    public EnemyPool(World world, int poolSize, EnemyFactory enemyFactory) {
        if(poolSize <= 0) {
            throw new IllegalArgumentException("Pool size must be greater than zero!");
        }

        this.enemyFactory = enemyFactory;
        this.world = world;
        enemyPool = new HashMap<>();
        random = new Random();

        for(String enemyType : ENEMY_TYPES) {
            createEnemyPool(enemyType,poolSize);
        }
    }

    private void createEnemyPool(String enemyType, int size) {

        Queue<Enemy> pool = new LinkedList<>();
        for(GameObject e : enemyFactory.create(size, enemyType)) {
            Enemy enemy = (Enemy) e;
            enemy.getBody().setActive(false);
            pool.add(enemy);
        }
        enemyPool.put(enemyType, pool);
    }

    private Enemy getRandomEnemy() {
        String randomEnemyType = ENEMY_TYPES.get(random.nextInt(ENEMY_TYPES.size()));
        return getEnemy(randomEnemyType);
    }

    private Enemy getEnemy(String enemyType) {
        Queue<Enemy> pool = enemyPool.get(enemyType);
        if (!pool.isEmpty()) {
            Enemy enemy = pool.poll();
            enemy.getBody().setActive(true);
            return enemy;
        }
        else {
            GameObject e = enemyFactory.create(enemyType);
            Enemy enemy = (Enemy) e;
            enemy.getBody().setActive(true);
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
            enemies.add(enemy);
        }
        return enemies;
    }


    /**
     * Returns enemy to enemy pool, deactivates their bodies, resets its actions and its destroyed-tag.
     //* @param enemy the Enemy object
     */
//    public void returnEnemy(Enemy enemy) {
//        Queue<Enemy> pool = enemyPool.get(enemy.getEnemyType());
//        if (pool != null) {
//            enemy.getBody().setActive(false);
//            enemy.revive();
//            enemy.resetActions();
//
//            // Return the enemy to the pool
//            pool.add(enemy);
//        }
//    }


    public Map<String, Queue<Enemy>> getEnemyPool() {
        return enemyPool;
    }

}




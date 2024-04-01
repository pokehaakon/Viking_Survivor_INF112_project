package Actors.Enemy;

import java.util.*;

public class EnemyPool {

    private final EnemyFactory enemyFactory;

    private final Map<String, Queue<Enemy>> enemyPool;

    private static final List<String> enemyTypes = Arrays.asList(
            "ENEMY1",
            "ENEMY2"
    );

    private final Random random;

    public EnemyPool(EnemyFactory enemyFactory) {
        this.enemyFactory = enemyFactory;
        enemyPool = new HashMap<>();
        random = new Random();

        for(String enemyType : enemyTypes) {
            createEnemyPool(enemyType, 200);
        }
    }

    private void createEnemyPool(String enemyType, int size) {
        Queue<Enemy> pool = new LinkedList<>(enemyFactory.createEnemies(size, enemyType));

        enemyPool.put(enemyType, pool);
    }

    private Enemy getRandomEnemy() {
        if (enemyPool.isEmpty()) {
            return null;
        }
        String randomEnemyType = enemyTypes.get(random.nextInt(enemyTypes.size()));
        // Obtain an enemy from the corresponding pool
        return getEnemies(randomEnemyType);
    }

    private Enemy getEnemies(String enemyType) {
        Queue<Enemy> pool = enemyPool.get(enemyType);
        if (!pool.isEmpty()) {
            return pool.poll();

        }
        else {
            return enemyFactory.createEnemyType(enemyType);
        }
    }

    public List<Enemy> getRandomEnemies(int num) {
        List<Enemy> enemies = new ArrayList<>();
        for(int i = 0; i < num; i++) {
            enemies.add(getRandomEnemy());
        }
        return enemies;
    }
    public List<Enemy> getEnemies(String enemyType, int num) {
        List<Enemy> enemies = new ArrayList<>();
        for(int i = 0; i < num; i++) {
            enemies.add(getEnemies(enemyType));
        }
        return enemies;
    }


    public void add(Enemy enemy, String enemyType) {
        Queue<Enemy> pool = enemyPool.get(enemyType);
        if (pool != null) {
            // Return the enemy to the pool
            pool.add(enemy);
        }
    }

}




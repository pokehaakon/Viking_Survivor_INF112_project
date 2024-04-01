package Actors.Enemy;

import java.util.*;

public class EnemyPool {

    private final EnemyFactory enemyFactory;

    private final Map<String, Queue<Enemy>> enemyPool;

    private static final List<String> ENEMY_TYPES = Arrays.asList(
            "ENEMY1",
            "ENEMY2"
    );

    private final Random random;

    public EnemyPool(EnemyFactory enemyFactory) {
        this.enemyFactory = enemyFactory;
        enemyPool = new HashMap<>();
        random = new Random();

        for(String enemyType : ENEMY_TYPES) {
            createEnemyPool(enemyType, 50);
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
        String randomEnemyType = ENEMY_TYPES.get(random.nextInt(ENEMY_TYPES.size()));
        // Obtain an enemy from the corresponding pool
        return getEnemy(randomEnemyType);
    }

    private Enemy getEnemy(String enemyType) {
        Queue<Enemy> pool = enemyPool.get(enemyType);
        if (!pool.isEmpty()) {
            return pool.poll();
        }
        else {
            return enemyFactory.createEnemyType(enemyType);
        }
    }

    public List<Enemy> activateRandomEnemies(int num) {
        List<Enemy> enemies = new ArrayList<>();
        for(int i = 0; i < num; i++) {
            Enemy enemy = getRandomEnemy();
            enemy.getBody().setActive(true);
            enemies.add(enemy);
        }
        return enemies;
    }
    public List<Enemy> activateEnemies(String enemyType, int num) {
        List<Enemy> enemies = new ArrayList<>();
        for(int i = 0; i < num; i++) {
            Enemy enemy = getEnemy(enemyType);
            enemy.getBody().setActive(true);
            enemies.add(enemy);
        }
        return enemies;
    }


    public void deActivateEnemy(Enemy enemy, String enemyType) {
        Queue<Enemy> pool = enemyPool.get(enemyType);
        if (pool != null) {
            enemy.getBody().setActive(false);
            // Return the enemy to the pool
            pool.add(enemy);
        }
    }

}




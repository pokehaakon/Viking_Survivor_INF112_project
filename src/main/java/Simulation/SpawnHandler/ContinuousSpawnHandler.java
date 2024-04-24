package Simulation.SpawnHandler;

import GameObjects.Actors.Enemy;
import GameObjects.ObjectTypes.EnemyType;
import GameObjects.Pool.ObjectPool;
import GameObjects.Pool.SmallPool;
import Simulation.ISpawnHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static Simulation.SpawnActions.spawnEnemy;
import static Tools.ListTools.findPrefix;
import static Tools.ListTools.removeDestroyed;

public class ContinuousSpawnHandler implements ISpawnHandler {
    private final SmallPool<Enemy> pool;
    private final int size, maxSpawnEachFrame;
    private final List<Enemy> spawnedEnemies;
    private final List<Enemy> activeEnemies;
    private final Consumer<Enemy> initializer;

    public ContinuousSpawnHandler(List<String> args, EnemyType enemyType, Consumer<Enemy> initializer, ObjectPool<Enemy, EnemyType> objectPool, List<Enemy> activeEnemies) {
        pool = objectPool.getObjectPool().get(enemyType);
        this.size = Integer.parseInt(findPrefix("size:", args));
        this.maxSpawnEachFrame = Integer.parseInt(findPrefix("maxSpawnEachFrame:", args));
        spawnedEnemies = new ArrayList<>(size);
        this.initializer = initializer;
        this.activeEnemies = activeEnemies;
    }

    /**
     * Spawns up to 'maxSpawnEachFrame' enemies if they are needed
     */
    @Override
    public void act(long frame) {
        removeDestroyed(spawnedEnemies, pool, true);
        int i = 0;
        int spawned = 0;
        Enemy enemy;
        while (i < size) {
            if (spawnedEnemies.size() == i) spawnedEnemies.add(null);
            if (spawnedEnemies.get(i++) != null) continue;
            enemy = pool.get();
            spawnedEnemies.set(i - 1, enemy);
            initializer.accept(enemy);
            activeEnemies.add(enemy);
            if (++spawned == maxSpawnEachFrame) break;
        }
    }
}

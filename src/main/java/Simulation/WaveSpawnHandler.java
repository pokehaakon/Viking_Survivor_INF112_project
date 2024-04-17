package Simulation;

import GameObjects.Actors.Enemy;
import GameObjects.ObjectTypes.EnemyType;
import GameObjects.Pool.ObjectPool;
import GameObjects.Pool.SmallPool;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static Tools.ListTools.findPrefix;
import static Tools.ListTools.removeDestroyed;

public class WaveSpawnHandler implements ISpawnHandler {

    private final SmallPool<Enemy> pool;
    private final int size, maxSpawnEachFrame;
    private int spawnedEnemies;
    private final List<Enemy> activeEnemies;
    private final Consumer<Enemy> initializer;

    public WaveSpawnHandler(List<String> args, EnemyType enemyType, Consumer<Enemy> initializer, ObjectPool<Enemy, EnemyType> objectPool, List<Enemy> activeEnemies) {
        pool = objectPool.getObjectPool().get(enemyType);
        this.size = Integer.parseInt(findPrefix("size:", args));
        this.maxSpawnEachFrame = Integer.parseInt(findPrefix("maxSpawnEachFrame:", args));
        spawnedEnemies = 0;
        this.initializer = initializer;
        this.activeEnemies = activeEnemies;
    }


    @Override
    public void act(long frame) {
        //removeDestroyed(spawnedEnemies, pool, true);
        int i = 0;
        int spawned = 0;
        Enemy enemy;
        while (spawnedEnemies < size) {
            enemy = pool.get();
            spawnedEnemies++;
            initializer.accept(enemy);
            activeEnemies.add(enemy);
            if (++spawned == maxSpawnEachFrame) break;
        }
    }
}

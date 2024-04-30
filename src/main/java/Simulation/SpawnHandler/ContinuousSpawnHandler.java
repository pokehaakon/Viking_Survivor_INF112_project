package Simulation.SpawnHandler;

import GameObjects.Actor;
import Simulation.ISpawnHandler;
import Tools.Pool.ObjectPool;
import Tools.Pool.SmallPool;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static Tools.ListTools.findPrefix;
import static Tools.ListTools.removeDestroyed;

public class ContinuousSpawnHandler implements ISpawnHandler {
    private final SmallPool<Actor> pool;
    private final int size, maxSpawnEachFrame;
    private final List<Actor> spawnedEnemies;
    private final List<Actor> activeEnemies;
    private final Consumer<Actor> initializer;

    public ContinuousSpawnHandler(List<String> args, String actorName, Consumer<Actor> initializer, ObjectPool<Actor> objectPool, List<Actor> activeEnemies) {
        pool = objectPool.getSmallPool(actorName);
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
        Actor actor;
        while (i < size) {
            if (spawnedEnemies.size() == i) spawnedEnemies.add(null);
            if (spawnedEnemies.get(i++) != null) continue;
            actor = pool.get();
            spawnedEnemies.set(i - 1, actor);
            initializer.accept(actor);
            activeEnemies.add(actor);
            if (++spawned == maxSpawnEachFrame) break;
        }
    }
}

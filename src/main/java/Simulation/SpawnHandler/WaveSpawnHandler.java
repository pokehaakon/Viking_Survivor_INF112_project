package Simulation.SpawnHandler;

import GameObjects.Actors.Actor;
import GameObjects.Pool.ObjectPool;
import GameObjects.Pool.SmallPool;
import Simulation.ISpawnHandler;

import java.util.List;
import java.util.function.Consumer;

import static Tools.ListTools.findPrefix;
import static Tools.ListTools.removeDestroyed;

public class WaveSpawnHandler implements ISpawnHandler {

    private final SmallPool<Actor> pool;
    private final int size, maxSpawnEachFrame;
    private int spawnedEnemies;
    private final List<Actor> activeEnemies;
    private final Consumer<Actor> initializer;

    public WaveSpawnHandler(List<String> args, String actorName, Consumer<Actor> initializer, ObjectPool<Actor> objectPool, List<Actor> activeEnemies) {
        pool = objectPool.getSmallPool(actorName);
        this.size = Integer.parseInt(findPrefix("size:", args));
        this.maxSpawnEachFrame = Integer.parseInt(findPrefix("maxSpawnEachFrame:", args));
        spawnedEnemies = 0;
        this.initializer = initializer;
        this.activeEnemies = activeEnemies;
    }


    @Override
    public void act(long frame) {
        //removeDestroyed(spawnedEnemies, pool, true);
        int spawned = 0;
        Actor actor;
        while (spawnedEnemies < size) {
            actor = pool.get();
            spawnedEnemies++;
            initializer.accept(actor);
            activeEnemies.add(actor);
            if (++spawned == maxSpawnEachFrame) break;
        }
    }
}

package Simulation.SpawnHandler;

import GameObjects.Actors.Actor;
import GameObjects.ObjectTypes.SwarmType;
import GameObjects.Pool.ObjectPool;
import GameObjects.Pool.SmallPool;
import Simulation.ISpawnHandler;
import com.badlogic.gdx.math.Vector2;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static Simulation.Coordinates.SwarmCoordinates.swarmInitializerPair;
import static Tools.ListTools.findPrefix;
import static Tools.ListTools.findPrefixOptional;
import static VikingSurvivor.app.Main.SCREEN_HEIGHT;
import static VikingSurvivor.app.Main.SCREEN_WIDTH;

public class SwarmSpawnHandler implements ISpawnHandler {

    private final SmallPool<Actor> pool;
    private final int size;
    private int delay;
    private final List<Actor> activeEnemies;
    private final Consumer<Actor> initializer;
    private SwarmType swarmType;

    private float speedMultiplier;
    private Supplier<Vector2> centerSupplier;

    public SwarmSpawnHandler(List<String> args, String actorName, Consumer<Actor> initializer, Supplier<Vector2> centerSupplier, ObjectPool<Actor> objectPool, List<Actor> activeEnemies) {
        pool = objectPool.getSmallPool(actorName);

        this.size = Integer.parseInt(findPrefix("size:", args));
        //this.maxSpawnEachFrame = Integer.parseInt(findPrefix("maxSpawnEachFrame:", args));
        this.delay = findPrefixOptional("delay:", args)
                .map(Integer::parseInt)
                .orElse(0);
        this.speedMultiplier = findPrefixOptional("speedMultiplier:", args)
                .map(Float::parseFloat)
                .orElse(1f);
        this.swarmType = SwarmType.valueOf(findPrefix("type:", args));

        //spawnedEnemies = 0;
        this.initializer = initializer;
        this.activeEnemies = activeEnemies;
        this.centerSupplier = centerSupplier;
    }

    @Override
    public void act(long frame) {
        if (delay-- != 0) return;

        List<Actor> enemies = pool.get(size);
        float spacing = enemies.get(0).bodyFeatures.shape().getRadius() * 2;
        var pair = swarmInitializerPair(swarmType, size, centerSupplier.get(), Math.max(SCREEN_WIDTH, SCREEN_HEIGHT), spacing, speedMultiplier);

        for (var actor :  enemies) {
            actor.addAction(pair.getValue0().get());
            initializer.accept(actor);
            actor.setPosition(pair.getValue1().get());
        }

        activeEnemies.addAll(enemies);
    }
}

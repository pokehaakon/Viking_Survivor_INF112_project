package Simulation.SpawnHandler;

import GameObjects.Actor;
import Simulation.Coordinates.SpawnCoordinates;
import Simulation.ISpawnHandler;
import Tools.Pool.ObjectPool;
import Tools.Pool.SmallPool;
import com.badlogic.gdx.math.Vector2;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static Contexts.GameContext.SPAWN_RECT;
import static Simulation.Coordinates.SwarmCoordinates.swarmInitializerPair;
import static Tools.ListTools.findPrefix;
import static Tools.ListTools.findPrefixOptional;
import static VikingSurvivor.app.HelloWorld.SET_FPS;
import static VikingSurvivor.app.Main.SCREEN_HEIGHT;
import static VikingSurvivor.app.Main.SCREEN_WIDTH;

public class SwarmSpawnHandler implements ISpawnHandler {

    private final SmallPool<Actor> pool;
    private final int size;
    private final List<Actor> activeEnemies;
    private final Consumer<Actor> initializer;
    private final SpawnCoordinates.SwarmType swarmType;
    private final float speedMultiplier;
    private final Supplier<Vector2> centerSupplier;
    private int delay;

    public SwarmSpawnHandler(List<String> args, String actorName, Consumer<Actor> initializer, Supplier<Vector2> centerSupplier, ObjectPool<Actor> objectPool, List<Actor> activeEnemies) {
        pool = objectPool.getSmallPool(actorName);

        this.size = Integer.parseInt(findPrefix("size:", args));
        //this.maxSpawnEachFrame = Integer.parseInt(findPrefix("maxSpawnEachFrame:", args));
        this.delay = Math.round(findPrefixOptional("delay:", args)
                .map(Float::parseFloat)
                .orElse(0f) * SET_FPS);
        this.speedMultiplier = findPrefixOptional("speedMultiplier:", args)
                .map(Float::parseFloat)
                .orElse(1f);
        this.swarmType = SpawnCoordinates.SwarmType.valueOf(findPrefix("type:", args));

        //spawnedEnemies = 0;
        this.initializer = initializer;
        this.activeEnemies = activeEnemies;
        this.centerSupplier = centerSupplier;
    }

    @Override
    public void act(long frame) {
        if (delay-- != 0) return;

        List<Actor> enemies = pool.get(size);
        float spacing = enemies.get(0).getBody().getFixtureList().get(0).getShape().getRadius() * 2;
        var pair = swarmInitializerPair(swarmType, size, centerSupplier.get(), Math.min(SPAWN_RECT.x, SPAWN_RECT.y), spacing, speedMultiplier);

        for (Actor actor :  enemies) {
            actor.addAction(pair.getValue0().get());
            initializer.accept(actor);
            float oldSpeed = actor.getSpeed();
            actor.addDieAction(e -> e.setSpeed(oldSpeed));
            actor.setSpeed(oldSpeed * speedMultiplier);
            actor.setPosition(pair.getValue1().get());
        }

        activeEnemies.addAll(enemies);
    }
}

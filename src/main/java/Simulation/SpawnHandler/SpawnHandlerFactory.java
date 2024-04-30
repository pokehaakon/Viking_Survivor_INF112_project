package Simulation.SpawnHandler;

import GameObjects.Actor;
import GameObjects.GameObject;
import Parsing.SpawnType;
import Simulation.ISpawnHandler;
import Tools.Pool.ObjectPool;

import java.util.List;

import static Contexts.ReleaseCandidateContext.DE_SPAWN_RECT;
import static GameObjects.ObjectActions.KilledAction.destroyIfDefeated;
import static GameObjects.ObjectActions.MovementActions.chaseActor;
import static GameObjects.ObjectActions.OutOfBoundsActions.deSpawnIfOutOfBounds;
import static Simulation.Coordinates.SpawnCoordinates.randomPointOutsideScreenRect;

public class SpawnHandlerFactory {

    private final ObjectPool<Actor> actorPool;
    private final ObjectPool<GameObject> terrainPool;

    private final List<Actor> activeEnemies;
    private final List<GameObject> activeTerrain;
    private final Actor player;

    public SpawnHandlerFactory(Actor player,
                               ObjectPool<Actor> actorPool,
                               ObjectPool<GameObject> terrainPool,
                               List<Actor> activeEnemies,
                               List<GameObject> activeTerrain
        ) {

        this.player = player;
        this.actorPool = actorPool;
        this.terrainPool = terrainPool;
        this.activeEnemies = activeEnemies;
        this.activeTerrain = activeTerrain;
    }

    public ISpawnHandler create(String actorName, SpawnType spawnType, List<String> args) {
        return switch (spawnType) {
            case SWARM -> new SwarmSpawnHandler(
                    args,
                    actorName,
                    e -> e.addAction(destroyIfDefeated(), deSpawnIfOutOfBounds(player, DE_SPAWN_RECT)),
                    () -> player.getBody().getPosition(),
                    actorPool,
                    activeEnemies
            );

            case BOSS -> new BossSpawnHandler( //TODO

            );
            case WAVE -> new WaveSpawnHandler(
                    args,
                    actorName,
                    e -> {
                        e.addAction(chaseActor(player), destroyIfDefeated(), deSpawnIfOutOfBounds(player, DE_SPAWN_RECT));
                        e.setPosition(randomPointOutsideScreenRect(player.getBody().getPosition()));
                    },
                    actorPool,
                    activeEnemies
            );
            case CONTINUOUS -> new ContinuousSpawnHandler(
                args,
                    actorName,
                e -> {
                    e.addAction(chaseActor(player), destroyIfDefeated(), deSpawnIfOutOfBounds(player, DE_SPAWN_RECT));
                    e.setPosition(randomPointOutsideScreenRect(player.getBody().getPosition()));
                },
                actorPool,
                activeEnemies
            );
        };
    }
}

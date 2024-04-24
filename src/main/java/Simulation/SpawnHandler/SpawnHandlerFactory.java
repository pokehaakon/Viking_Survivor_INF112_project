package Simulation.SpawnHandler;

import GameObjects.Actors.Enemy;
import GameObjects.Actors.Player;
import GameObjects.ObjectTypes.EnemyType;
import GameObjects.ObjectTypes.TerrainType;
import GameObjects.Pool.ObjectPool;
import GameObjects.StaticObjects.Terrain;
import Parsing.SpawnType;
import Simulation.ISpawnHandler;

import java.util.List;

import static Simulation.Coordinates.SpawnCoordinates.randomPointOutsideScreenRect;
import static GameObjects.Actors.ActorAction.EnemyActions.*;
import static Simulation.Coordinates.SwarmCoordinates.swarmInitializerPair;

public class SpawnHandlerFactory {

    private final ObjectPool<Enemy> enemyPool;
    private final ObjectPool<Terrain> terrainPool;

    private final List<Enemy> activeEnemies;
    private final List<Terrain> activeTerrain;
    private final Player player;

    public SpawnHandlerFactory(Player player,
                               ObjectPool<Enemy> enemyPool,
                               ObjectPool<Terrain> terrainPool,
                               List<Enemy> activeEnemies,
                               List<Terrain> activeTerrain
        ) {

        this.player = player;
        this.enemyPool = enemyPool;
        this.terrainPool = terrainPool;
        this.activeEnemies = activeEnemies;
        this.activeTerrain = activeTerrain;
    }

    public ISpawnHandler create(String enemyName, SpawnType spawnType, List<String> args) {
        return switch (spawnType) {
            case SWARM -> new SwarmSpawnHandler(
                    args,
                    enemyName,
                    e -> {
                        e.setActions(destroyIfDefeated(), destroyIfOutOfBounds(player));
                    },
                    () -> player.getBody().getPosition(),
                    enemyPool,
                    activeEnemies
            );

            case BOSS -> new BossSpawnHandler( //TODO

            );
            case WAVE -> new WaveSpawnHandler(
                    args,
                    enemyName,
                    e -> {
                        e.setActions(chasePlayer(player), destroyIfDefeated(), destroyIfOutOfBounds(player));
                        e.setPosition(randomPointOutsideScreenRect(player.getBody().getPosition()));
                    },
                    enemyPool,
                    activeEnemies
            );
            case CONTINUOUS -> new ContinuousSpawnHandler(
                args,
                enemyName,
                e -> {
                    e.setActions(chasePlayer(player), destroyIfDefeated(), destroyIfOutOfBounds(player));
                    e.setPosition(randomPointOutsideScreenRect(player.getBody().getPosition()));
                },
                enemyPool,
                activeEnemies
            );
        };
    }
}

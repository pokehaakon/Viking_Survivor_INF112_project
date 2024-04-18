package Simulation;

import GameObjects.Actors.Enemy;
import GameObjects.Actors.Player;
import GameObjects.Animations.AnimationRendering.AnimationLibrary;
import GameObjects.ObjectTypes.EnemyType;
import GameObjects.ObjectTypes.TerrainType;
import GameObjects.Pool.ObjectPool;
import GameObjects.StaticObjects.Terrain;
import Parsing.SpawnType;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import static Coordinates.SpawnCoordinates.randomPointOutsideScreenRect;
import static GameObjects.Actors.ActorAction.EnemyActions.*;

public class SpawnHandlerFactory {

    private final ObjectPool<Enemy, EnemyType> enemyPool;
    private final ObjectPool<Terrain, TerrainType> terrainPool;

    private final List<Enemy> activeEnemies;
    private final List<Terrain> activeTerrain;
    private final Player player;
    private final AnimationLibrary animationLibrary;

    public SpawnHandlerFactory(Player player,
                               ObjectPool<Enemy, EnemyType> enemyPool,
                               ObjectPool<Terrain, TerrainType> terrainPool,
                               List<Enemy> activeEnemies,
                               List<Terrain> activeTerrain,
                               AnimationLibrary animationLibrary
        ) {

        this.player = player;
        this.enemyPool = enemyPool;
        this.terrainPool = terrainPool;
        this.activeEnemies = activeEnemies;
        this.activeTerrain = activeTerrain;
        this.animationLibrary = animationLibrary;
    }

    public ISpawnHandler create(EnemyType enemyType, SpawnType spawnType, List<String> args) {
        return switch (spawnType) {
            case SWARM -> null; //TODO
            case BOSS -> new BossSpawnHandler( //TODO

            );
            case WAVE -> new WaveSpawnHandler(args,
                    enemyType,
                    e -> {
                        e.setActions(chasePlayer(player), destroyIfDefeated(), destroyIfOutOfBounds(player));
                        e.setPosition(randomPointOutsideScreenRect(player.getBody().getPosition()));
                        e.renderAnimations(animationLibrary);
                    },
                    enemyPool,
                    activeEnemies
            );
            case CONTINUOUS -> new ContinuousSpawnHandler(
                args,
                enemyType,
                e -> {
                    e.setActions(chasePlayer(player), destroyIfDefeated(), destroyIfOutOfBounds(player));
                    e.setPosition(randomPointOutsideScreenRect(player.getBody().getPosition()));
                    e.renderAnimations(animationLibrary);
                },
                enemyPool,
                activeEnemies
            );
        };
    }
}

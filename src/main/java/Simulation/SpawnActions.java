package Simulation;

import Contexts.ReleaseCandidateContext;
import Simulation.Coordinates.SwarmCoordinates;
import GameObjects.Actors.ActorAction.ActorAction;
import GameObjects.Actors.Enemy;
import GameObjects.Actors.Player;
import GameObjects.ObjectTypes.EnemyType;
import GameObjects.ObjectTypes.SwarmType;
import GameObjects.Pool.ObjectPool;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import static Simulation.Coordinates.SpawnCoordinates.randomSpawnPoint;
import static GameObjects.Actors.ActorAction.EnemyActions.*;

public abstract class SpawnActions {

    public static Enemy spawnEnemy(ObjectPool<Enemy> enemyPool, Vector2 position, String enemyName, List<ActorAction<Enemy>> actions) {
        Enemy enemy = enemyPool.get(enemyName);
        enemy.setPosition(position);
        enemy.setActions(actions);

        return enemy;
    }

    public static List<Enemy> spawnEnemies(int num, ObjectPool<Enemy> enemyPool, List<Vector2> positions, String enemyName, List<ActorAction<Enemy>> actions) {
        List<Enemy> ls = new ArrayList<>(num);
        int i = 0;
        for(Enemy enemy : enemyPool.get(enemyName, num)) {
            enemy.setPosition(positions.get(i++));
            enemy.setActions(actions);
            ls.add(enemy);
        }
        return ls;
    }

    public static List<Enemy> spawnSwarm(int num, ObjectPool<Enemy> enemyPool, Vector2 position, String enemyName, SwarmType swarmType, Player player, int spacing, int speedMultiplier) {
        Vector2 center = player.getBody().getPosition();
        Vector2 startPoint = randomSpawnPoint(center, ReleaseCandidateContext.SPAWN_RADIUS);

        List<Vector2> positions = SwarmCoordinates.getSwarmCoordinates(startPoint, SwarmType.LINE, num, spacing, center);
        List<ActorAction<Enemy>> actions = List.of(destroyIfOutOfBounds(player), destroyIfDefeated());

        return spawnEnemies(num, enemyPool, positions, enemyName, actions);

    }

//    public static List<Enemy> spawnRandomEnemies(ObjectPool<Enemy, EnemyType> enemyPool, Vector2 position, int num, List<ActorAction<Enemy>> actions, AnimationLibrary animationLibrary) {
//        List<Enemy> ls = new ArrayList<>(num);
//        for(Enemy enemy : enemyPool.getRandom(num)) {
//            enemy.setPosition(SpawnCoordinates.randomSpawnPoint(position, ReleaseCandidateContext.SPAWN_RADIUS));
//            for(ActorAction action : actions) {
//                enemy.setAction(action);
//            }
//            enemy.renderAnimations(animationLibrary);
//            ls.add(enemy);
//            //enemies.add(enemy);
//        }
//        //lastSpawnTime = TimeUtils.millis();
//        return ls;
//    }

//    public void spawnSwarm(ObjectPool<Enemy, EnemyType> enemyPool, Vector2 position, EnemyType enemyType, SwarmType swarmType, int size, int spacing, int speedMultiplier, AnimationLibrary animationLibrary) {
//        List<Enemy> swarmMembers = enemyPool.get(enemyType, size);
//        List<Enemy> swarm = SwarmCoordinates.createSwarm(swarmType, swarmMembers, position, ReleaseCandidateContext.SPAWN_RADIUS, size, spacing, speedMultiplier);
//        for(Enemy enemy : swarm) {
//            enemy.setAction(moveInStraightLine());
//            enemy.setAction(destroyIfDefeated(player));
//            enemy.renderAnimations(animationLibrary);
//            enemies.add(enemy);
//        }
//
//        lastSwarmSpawnTime = TimeUtils.millis();
//    }

//    public void spawnTerrain(TerrainType type) {
//        Terrain terrain = context.getTerrainPool().get(type);
//        terrain.renderAnimations(context.getAnimationLibrary());
//        terrain.setPosition(SpawnCoordinates.randomSpawnPoint(player.getBody().getPosition(), ReleaseCandidateContext.SPAWN_RADIUS));
//        context.getDrawableTerrain().add(terrain);
//        lastSpawnTime = TimeUtils.millis();
//    }
}

package Simulation;

public abstract class SpawnActions {

//    public static Actor spawnEnemy(ObjectPool<Actor> enemyPool, Vector2 position, String enemyName, List<ActorAction> actions) {
//        Actor enemy = enemyPool.get(enemyName);
//        enemy.setPosition(position);
//        enemy.addAction(actions);
//
//        return enemy;
//    }
//
//    public static List<Actor> spawnEnemies(int num, ObjectPool<Actor> enemyPool, List<Vector2> positions, String enemyName, List<ActorAction> actions) {
//        List<Actor> ls = new ArrayList<>(num);
//        int i = 0;
//        for(Actor enemy : enemyPool.get(enemyName, num)) {
//            enemy.setPosition(positions.get(i++));
//            enemy.addAction(actions);
//            ls.add(enemy);
//        }
//        return ls;
//    }
//
//    public static List<Actor> spawnSwarm(int num, ObjectPool<Actor> enemyPool, Vector2 position, String enemyName, SwarmType swarmType, Actor player, int spacing, int speedMultiplier) {
//        Vector2 center = player.getBody().getPosition();
//        Vector2 startPoint = randomSpawnPoint(center, ReleaseCandidateContext.SPAWN_RADIUS);
//
//        List<Vector2> positions = SwarmCoordinates.getSwarmCoordinates(startPoint, SwarmType.LINE, num, spacing, center);
//        List<ActorAction> actions = List.of(deSpawnIfOutOfBounds(player, DE_SPAWN_RECT), destroyIfDefeated());
//
//        return spawnEnemies(num, enemyPool, positions, enemyName, actions);
//
//    }

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

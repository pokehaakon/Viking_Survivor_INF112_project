package Simulation;

import GameObjects.Actors.ActorAction.ActorAction;
import GameObjects.Actors.Enemy.Enemy;
import GameObjects.Actors.ObjectTypes.EnemyType;
import GameObjects.Actors.ObjectTypes.SwarmType;
import GameObjects.Actors.ObjectTypes.TerrainType;
import GameObjects.Actors.Player.Player;
import GameObjects.ObjectPool;
import GameObjects.Terrain.Terrain;
import InputProcessing.Contexts.MVPContext;
import InputProcessing.Coordinates.SpawnCoordinates;
import InputProcessing.Coordinates.SwarmCoordinates;
import InputProcessing.KeyStates;
import Tools.RollingSum;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;

import static GameObjects.Actors.ActorAction.EnemyActions.destroyIfDefeated;
import static GameObjects.Actors.ActorAction.EnemyActions.moveInStraightLine;
import static GameObjects.Actors.Stats.Stats.SWARM_SPEED_MULTIPLIER;

public class Simulation implements Runnable {
    private Lock renderLock;
    private KeyStates keyStates;
    private World world;
    private RollingSum updateTime, UPS;
    private boolean quit = false;
    private Set<Body> toBeKilled;
    private boolean paused = false;
    private long frame = 0;
    public final int SET_UPS = 60;
    private final MVPContext context;
    long lastSpawnTime;
    private Player player;
    private ObjectPool<Enemy, EnemyType> enemyPool;
    private List<Enemy> enemies;
    private AtomicLong synchronizer;

    private long lastSwarmSpawnTime;

    public Simulation(MVPContext context) {
        this.context = context;
        renderLock = context.getRenderLock();
        keyStates = context.getKeyStates();
        world = context.getWorld();
        updateTime = context.getUpdateTime();
        UPS = context.getUPS();
        toBeKilled = context.getToBoKilled();
        synchronizer = context.getSynchronizer();
        player = context.getPlayer();
        enemyPool = context.getEnemyPool();
        enemies = context.getDrawableEnemies();
    }

    @Override
    public void run() {

        long dt = 1_000_000_000/SET_UPS; // sec / update * 10^9 nanosec / sec
        long lastFrameStart = System.nanoTime();


        while (!quit) {
            while(paused) {
                synchronized (this) {try {this.wait();} catch (InterruptedException ignored) {}}
            }



            lastFrameStart = System.nanoTime();
            long t0 = System.nanoTime();

            if (keyStates.getState(KeyStates.GameKey.QUIT)) {
                stopSim();
            }


            for (Enemy enemy : context.getDrawableEnemies()) {
                enemy.doAction();
            }

            context.getPlayer().doAction();

            if (TimeUtils.millis() - lastSpawnTime > 1000) {
                spawnSwarm(EnemyType.ENEMY1, SwarmType.LINE,10,100, SWARM_SPEED_MULTIPLIER);
                spawnTerrain(TerrainType.TREE);
            }

            doSpinSleep(lastFrameStart, dt);
            UPS.add(System.nanoTime() - lastFrameStart);
            while (frame > synchronizer.get()){continue;}
            renderLock.lock();
            world.step(1/(float) 60, 10, 10);

            removeDestroyedEnemies();
            //context.updateActorActions();
            //context.removeDestroyedEnemies();


//        for (Body b : toBeKilled) {
//            world.destroyBody(b);
//        }
//
//        toBeKilled.clear();

            renderLock.unlock();
            long simTimeToUpdate = System.nanoTime() - t0;

            updateTime.add(simTimeToUpdate);
            frame++;
        }
    }

    private void doSpinSleep(long lastFrameStart, long dt) {
        while(lastFrameStart + dt - System.nanoTime() > 0) {}
    }

    /**
     * Despawns destroyed enemies by returning them to enemy pool and removing them from the spawned enemy list
     */
    public void removeDestroyedEnemies() {
        int i = 0;
        for (Enemy e : enemies) {
            if (e.isDestroyed()) {
                enemyPool.returnToPool(e);
            } else {
                enemies.set(i++, e);
            }
        }
        enemies.subList(i, enemies.size()).clear();
    }

    private void spawnEnemies(EnemyType enemyType, int num, List<ActorAction> actions) {
        for(Enemy enemy: enemyPool.get(enemyType, num)) {
            enemy.setPosition(SpawnCoordinates.randomSpawnPoint(player.getBody().getPosition(), MVPContext.SPAWN_RADIUS));
            for(ActorAction action : actions) {
                enemy.setAction(action);
            }
            enemies.add(enemy);
        }
    }

    private void spawnRandomEnemies(int num, List<ActorAction> actions) {
        for(Enemy enemy : enemyPool.getRandom(num)) {
            enemy.setPosition(SpawnCoordinates.randomSpawnPoint(player.getBody().getPosition(), MVPContext.SPAWN_RADIUS));
            for(ActorAction action : actions) {
                enemy.setAction(action);
            }
            enemies.add(enemy);
        }
        lastSpawnTime = TimeUtils.millis();
    }

    private void spawnTerrain(TerrainType type) {
        Terrain terrain = context.getTerrainPool().get(type);
        terrain.setPosition(SpawnCoordinates.randomSpawnPoint(player.getBody().getPosition(), MVPContext.SPAWN_RADIUS));
        context.getDrawableTerrain().add(terrain);
        lastSwarmSpawnTime = TimeUtils.millis();
    }


    private void spawnSwarm(EnemyType enemyType, SwarmType swarmType, int size, int spacing, int speedMultiplier) {
        List<Enemy> swarmMembers = enemyPool.get(enemyType, size);
        List<Enemy> swarm = SwarmCoordinates.createSwarm(swarmType, swarmMembers, player.getBody().getPosition(), MVPContext.SPAWN_RADIUS, size, spacing, speedMultiplier);
        for(Enemy enemy : swarm) {
            enemy.setAction(moveInStraightLine());
            enemy.setAction(destroyIfDefeated(player));
            enemies.add(enemy);
        }

        lastSpawnTime = TimeUtils.millis();
    }

    public void stopSim() {
        quit = true;
    }

    public void pause() {
        paused = true;
    }

    public void unpause() {
        paused = false;
    }
    public long getFrameNumber() {
        return frame;
    }

}



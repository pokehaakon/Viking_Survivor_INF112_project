package Simulation;

import GameObjects.Actors.ObjectActions.Action;
import GameObjects.Actors.ObjectActions.EnemyActions;
import GameObjects.Actors.Enemy;
import GameObjects.Actors.ObjectActions.PickupActions;
import GameObjects.Actors.Pickups;
import GameObjects.GameObject;
import GameObjects.ObjectTypes.EnemyType;
import GameObjects.ObjectTypes.PickupType;
import GameObjects.ObjectTypes.SwarmType;
import GameObjects.ObjectTypes.TerrainType;
import GameObjects.Actors.Player;
import GameObjects.Pool.ObjectPool;
import GameObjects.StaticObjects.Terrain;
import GameObjects.Actors.Weapon;
import Contexts.ReleaseCandidateContext;
import Coordinates.SpawnCoordinates;
import Coordinates.SwarmCoordinates;
import InputProcessing.KeyStates;
import Tools.RollingSum;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;

import static GameObjects.Actors.ObjectActions.EnemyActions.*;
import static VikingSurvivor.app.Main.SCREEN_HEIGHT;
import static VikingSurvivor.app.Main.SCREEN_WIDTH;

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
    private final ReleaseCandidateContext context;
    long lastSpawnTime;
    long lastPickupSpawnTime;
    private Player player;
    private ObjectPool<Enemy, EnemyType> enemyPool;
    private ObjectPool<Pickups, PickupType> pickupPool;
    private List<Enemy> enemies;
    private List<Pickups> pickups;
    private AtomicLong synchronizer;

    private Weapon mainWeapon;

    private long lastSwarmSpawnTime;

    private long lastOrbit;

    private float ORBIT_INTERVAL = 1000;

    List<Weapon> weapons;

    ObjectPool<Terrain,TerrainType> terrainPool;

    List<Terrain> terrain;



    public Simulation(ReleaseCandidateContext context) {
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
        weapons = context.getDrawableWeapons();
        pickupPool = context.getPickupsPool();
        pickups = context.getDrawablePickups();


        terrainPool = context.getTerrainPool();
        terrain = context.getDrawableTerrain();
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


            for (Enemy enemy : enemies) {
                enemy.doAction();
            }

            for(Weapon weapon : weapons) {
                weapon.doAction();
            }
            for(Terrain terrain : context.getDrawableTerrain()) {
                if(terrain.outOfBounds(player.getBody().getPosition(),DESPAWN_RADIUS)) {
                    terrain.destroy();
                }
            }


            context.getPlayer().doAction();

            // random spawning for now
            if (TimeUtils.millis() - lastSpawnTime > 10000) {
                spawnRandomEnemies(5, Arrays.asList(EnemyActions.destroyIfDefeated(player),EnemyActions.chasePlayer(player), coolDown(500)));
                spawnFixedTerrain(50,0.5f*SCREEN_WIDTH+200,0.5f*SCREEN_HEIGHT+200,200,TerrainType.TREE);
                //spawnTerrain(TerrainType.TREE);


            }
            if(TimeUtils.millis() - lastSwarmSpawnTime > 15000) {
                //spawnSwarm(EnemyType.RAVEN,SwarmType.LINE,10,60,5);
            }


            // If an enemy is defeated, spawn a pickuporb
            for (Enemy enemy : enemies) {
                if (enemy.isDestroyed()) {
                    spawnPickups(PickupType.PICKUPORB, enemy.getBody().getPosition(), List.of(PickupActions.giveHP(player, 10)));
                }
            }

            // If a pickup is picked up, remove it from the list of pickups
            removePickedUpPickups();


            doSpinSleep(lastFrameStart, dt);
            UPS.add(System.nanoTime() - lastFrameStart);
            while (frame > synchronizer.get()){continue;}
            renderLock.lock();

            world.step(1/(float) 60, 10, 10);


            removeDestroyedEnemies();

            removeDestroyedTerrain();


            renderLock.unlock();
            long simTimeToUpdate = System.nanoTime() - t0;


            updateTime.add(simTimeToUpdate);

            frame++;

            if(player.HP <= 0) {
                context.gameOver();
                stopSim();
            }

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

    public void removePickedUpPickups() {
        int i = 0;
        for (Pickups p : pickups) {
            if (p.isPickedUp()) {
                pickupPool.returnToPool(p);
            } else {
                pickups.set(i++, p);
            }
        }
        pickups.subList(i, pickups.size()).clear();
    }
    public void removeDestroyedTerrain() {
        int i = 0;
        for (Terrain t : terrain) {
            if (t.isDestroyed()) {
                terrainPool.returnToPool(t);
            } else {
                terrain.set(i++, t);
            }
        }
        terrain.subList(i, terrain.size()).clear();
    }
    private void spawnEnemies(EnemyType enemyType, int num, List<Action> actions) {
        for(Enemy enemy: enemyPool.get(enemyType, num)) {
            enemy.setPosition(SpawnCoordinates.randomSpawnPoint(player.getBody().getPosition(), ReleaseCandidateContext.SPAWN_RADIUS));
            for(Action action : actions) {
                enemy.setAction(action);
            }

            enemy.renderAnimations(context.getAnimationLibrary());
            enemies.add(enemy);
        }
        lastSpawnTime = TimeUtils.millis();
    }

    private void spawnRandomEnemies(int num, List<Action> actions) {
        for(Enemy enemy : enemyPool.getRandom(num)) {
            enemy.setPosition(SpawnCoordinates.randomSpawnPoint(player.getBody().getPosition(), ReleaseCandidateContext.SPAWN_RADIUS));
            for(Action action : actions) {
                enemy.setAction(action);
            }
            enemy.renderAnimations(context.getAnimationLibrary());
            enemies.add(enemy);
        }
        lastSpawnTime = TimeUtils.millis();
    }

    private void spawnTerrain(TerrainType type) {
        Terrain terrain = context.getTerrainPool().get(type);
        terrain.renderAnimations(context.getAnimationLibrary());
        terrain.setPosition(SpawnCoordinates.randomSpawnPoint(player.getBody().getPosition(), ReleaseCandidateContext.SPAWN_RADIUS));
        context.getDrawableTerrain().add(terrain);
        lastSpawnTime = TimeUtils.millis();
    }
    private void spawnFixedTerrain(int num,float distX, float distY,float minDistanceBetween, TerrainType type) {
        List<Vector2> occupiedPositions = SpawnCoordinates.getOccupiedPositions(context.getDrawableTerrain());
        System.out.println(context.getDrawableTerrain().size());
        List<Vector2> availableSpawns = SpawnCoordinates.fixedSpawnPoints(num, distX,distY,minDistanceBetween,player.getBody().getPosition(),occupiedPositions);

        for(Vector2 spawn: availableSpawns) {
            Terrain terrain = context.getTerrainPool().get(type);
            terrain.renderAnimations(context.getAnimationLibrary());
            terrain.setPosition(spawn);
            context.getDrawableTerrain().add(terrain);
        }


        lastSpawnTime = TimeUtils.millis();

    }

    private void spawnPickups(PickupType type, Vector2 position, List<Action<Pickups>> actions) {
        Pickups pickup = context.getPickupsPool().get(type);
        pickup.renderAnimations(context.getAnimationLibrary());
        pickup.setPosition(position);
        for(Action<Pickups> action:actions) {
            pickup.setAction(action);
        }
        context.getDrawablePickups().add(pickup);
        lastPickupSpawnTime = TimeUtils.millis();
    }


    private void spawnSwarm(EnemyType enemyType, SwarmType swarmType, int size, int spacing, int speedMultiplier) {
        List<Enemy> swarmMembers = enemyPool.get(enemyType, size);
        List<Enemy> swarm = SwarmCoordinates.createSwarm(swarmType, swarmMembers, player.getBody().getPosition(), ReleaseCandidateContext.SPAWN_RADIUS, size, spacing, speedMultiplier);
        for(Enemy enemy : swarm) {
            enemy.setAction(moveInStraightLine());
            enemy.setAction(destroyIfDefeated(player));
            enemy.renderAnimations(context.getAnimationLibrary());
            enemies.add(enemy);
        }

        lastSwarmSpawnTime = TimeUtils.millis();
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

    public void orbitWeapon() {

        if(TimeUtils.millis() - lastOrbit > ORBIT_INTERVAL) {
            mainWeapon.doAction();
        }
        if (mainWeapon.getAngleToPlayer() >= 2 * Math.PI) {
            mainWeapon.getBody().setActive(false);
            mainWeapon.setAngleToPlayer(0);
            lastOrbit = TimeUtils.millis();

        }

    }

}



package Simulation;

import Contexts.ReleaseCandidateContext;
import GameObjects.Actor;
import GameObjects.GameObject;
import GameObjects.ObjectActions.Action;
import GameObjects.ObjectActions.WeaponActions;
import InputProcessing.KeyStates;
import Simulation.Coordinates.SpawnCoordinates;
import Tools.Pool.ObjectPool;
import Tools.RollingSum;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;

import static GameObjects.ObjectActions.KilledAction.destroyIfDefeated;
import static GameObjects.ObjectActions.KilledAction.spawnPickups;
import static GameObjects.ObjectActions.MovementActions.chaseActor;
import static GameObjects.ObjectActions.PickupActions.giveHP;
import static GameObjects.ObjectActions.PickupActions.setOrbitSpeed;
import static Tools.ListTools.removeDestroyed;

public class Simulation implements Runnable {
    public static final AtomicLong EXP = new AtomicLong();
    public static final int SET_UPS = 60;

    private final Lock renderLock;
    private final KeyStates keyStates;
    private final World world;
    private final RollingSum updateTime;
    private final RollingSum UPS;
    private final ReleaseCandidateContext context;
    private final Actor player;
    private final ObjectPool<Actor> actorPool;
    private final ObjectPool<GameObject> objectPool;
    private final List<Actor> actors;
    private final List<GameObject> objects;
    private final AtomicLong synchronizer;
    private final GameWorld gameWorld;


    private boolean quit = false;
    private boolean paused = false;
    private long frame = 0;

    //temp
    private long lastSpawnTime;



    private List<Actor> spawnedPickups;

    private List<Actor> drawableEnemies;
    public Simulation(ReleaseCandidateContext context) {
        this.context = context;
        renderLock = context.getRenderLock();
        keyStates = context.getKeyStates();
        world = context.getWorld();
        updateTime = context.getUpdateTime();
        UPS = context.getUPS();
        synchronizer = context.getSynchronizer();
        player = context.getPlayer();
        actorPool = context.getActorPool();
        actors = context.getDrawableActors();

        objectPool = context.getObjectPool();
        objects = context.getDrawableObjects();
        gameWorld = context.getGameWorld();

        spawnedPickups = context.getPickup();

        drawableEnemies = context.getDrawableEnemies();
    }

    @Override
    public void run() {

        long dt = 1_000_000_000/SET_UPS; // sec / update * 10^9 nanosec / sec
        long lastFrameStart;


        while (!quit) {
            while(paused) {
                synchronized (this) {try {this.wait();} catch (InterruptedException ignored) {}}
            }

            lastFrameStart = System.nanoTime();
            long t0 = System.nanoTime();

            if (keyStates.getState(KeyStates.GameKey.QUIT)) stopSim();


            context.getPlayer().doAction();

            for (Actor actor : actors) actor.doAction();

            for(Actor enemy:drawableEnemies) enemy.doAction();


            // random spawning for now
            if (TimeUtils.millis() - lastSpawnTime > 5000) {
                //spawnRandomEnemies(5,Arrays.asList(ActorActions.destroyIfDefeated(player),ActorActions.chasePlayer(player), coolDown(500)));
                spawnTerrain("TREE");
                spawnEnemies("RAVEN",10,
                        chaseActor(player),
                        spawnPickups(1,"HP_PICKUP",spawnedPickups,context.getActorPool(),giveHP(player,10), setOrbitSpeed(5000,0.4f)),destroyIfDefeated()
                        );
            }

            if (frame == 10) {
                var a = actorPool.get("KNIFE");
                a.addAction(WeaponActions.throwOnClosestEnemy(player,500,actors));
                //a.addAction(WeaponActions.orbitActor(10,  player, 0, 0));
                actors.add(a);
            }



            doSpinSleep(lastFrameStart, dt);
            UPS.add(System.nanoTime() - lastFrameStart);
            while (frame > synchronizer.get()){continue;}
            renderLock.lock();

            gameWorld.act(frame);

            world.step(1/(float) 60, 10, 10);


            removeDestroyed(actors, actorPool, true);
            removeDestroyed(objects, objectPool, true);

            removeDestroyed(spawnedPickups,actorPool,true);

            renderLock.unlock();
            long simTimeToUpdate = System.nanoTime() - t0;


            updateTime.add(simTimeToUpdate);

            frame++;

            if(player.getHP() <= 0) {
                context.gameOver();
                stopSim();
            }

            }

    }

    private void doSpinSleep(long lastFrameStart, long dt) {
        while(lastFrameStart + dt - System.nanoTime() > 0) {}
    }


    private void spawnTerrain(String TerrainName) {
        GameObject terrain = context.getObjectPool().get(TerrainName);
        terrain.setPosition(SpawnCoordinates.randomSpawnPoint(player.getBody().getPosition(), ReleaseCandidateContext.SPAWN_RADIUS));
        context.getDrawableObjects().add(terrain);
        lastSpawnTime = TimeUtils.millis();
    }

    private void spawnEnemies(String type, int n, Action... actions) {
        for(Actor enemy : context.getActorPool().get(type, n)) {
            enemy.setPosition(SpawnCoordinates.randomSpawnPoint(player.getBody().getPosition(), 200));
            enemy.addAction(actions);
            //drawableEnemies.add(enemy);
            context.getDrawableActors().add(enemy);
            lastSpawnTime = TimeUtils.millis();
        }
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



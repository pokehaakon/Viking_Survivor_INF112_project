package Simulation;

import GameObjects.Actors.Actor;
import GameObjects.GameObject;
import GameObjects.Pool.ObjectPool;
import Contexts.ReleaseCandidateContext;
import Simulation.Coordinates.SpawnCoordinates;
import InputProcessing.KeyStates;
import Tools.RollingSum;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;

import static Tools.ListTools.removeDestroyed;

public class Simulation implements Runnable {
    public static final AtomicLong EXP = new AtomicLong();

    private Lock renderLock;
    private KeyStates keyStates;
    private World world;
    private RollingSum updateTime, UPS;
    private boolean quit = false;
    private boolean paused = false;
    private long frame = 0;
    public final int SET_UPS = 60;
    private final ReleaseCandidateContext context;
    long lastSpawnTime;
    private Actor player;
    private ObjectPool<Actor> actorPool;
    private ObjectPool<GameObject> objectPool;
    private List<Actor> actors;
    private List<GameObject> objects;
    private AtomicLong synchronizer;

    private float ORBIT_INTERVAL = 1000;

    private GameWorld gameWorld;


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


            // random spawning for now
            if (TimeUtils.millis() - lastSpawnTime > 5000) {
                //spawnRandomEnemies(5,Arrays.asList(ActorActions.destroyIfDefeated(player),ActorActions.chasePlayer(player), coolDown(500)));
                spawnTerrain("TerrainType:TREE");
            }


            // If an actor is defeated, spawn a pickup orb
//            for (Actor actor : actors) {
//                if (actor.isDestroyed()) {
//                    spawnPickups("PickupType:PICKUPORB", actor.getBody().getPosition());
//                }
//            }

            // If a pickup is picked up, remove it from the list of pickups
            //removePickedUpPickups();
            //removeDestroyedEnemies();


            doSpinSleep(lastFrameStart, dt);
            UPS.add(System.nanoTime() - lastFrameStart);
            while (frame > synchronizer.get()){continue;}
            renderLock.lock();

            gameWorld.act(frame);

            world.step(1/(float) 60, 10, 10);


            removeDestroyed(actors, actorPool, true);
            removeDestroyed(objects, objectPool, true);


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



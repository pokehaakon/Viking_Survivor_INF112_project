package Simulation;

import Contexts.GameContext;
import GameObjects.Actor;
import GameObjects.GameObject;
import GameObjects.ObjectActions.PickupActions;
import GameObjects.ObjectActions.WeaponActions;
import InputProcessing.KeyStates;
import Simulation.Coordinates.SpawnCoordinates;
import Tools.FilterTool;
import Tools.Pool.ObjectPool;
import Tools.RollingSum;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;


import static Contexts.GameContext.SPAWN_RECT;
import static Tools.ListTools.removeDestroyed;
import static VikingSurvivor.app.HelloWorld.SET_FPS;
import static VikingSurvivor.app.HelloWorld.millisToFrames;
import static com.badlogic.gdx.scenes.scene2d.ui.Table.Debug.actor;

public class Simulation implements Runnable {
    public static final AtomicLong EXP = new AtomicLong();
    public static final int SET_UPS = 60;
    Random random;
    private final Lock renderLock;
    private final KeyStates keyStates;
    private final World world;
    private final RollingSum updateTime;
    private final RollingSum UPS;
    private final GameContext context;
    private final Actor player;
    private final ObjectPool<Actor> actorPool;
    private final ObjectPool<GameObject> objectPool;
    private final List<Actor> actors;
    private final List<GameObject> objects;
    private final AtomicLong synchronizer;
    private final GameWorld gameWorld;

    private static final List<Float> DISTANCES_BETWEEN_TERRAIN = List.of(30f,60f,90f);

    private boolean quit = false;
    private boolean paused = false;
    private long frame = 0;


    private final static float TERRAIN_SPAWN_FRAME_INTERVAL = (float) ((1000) * SET_FPS) / 1000;
    private float LAST_TERRAIN_SPAWN_FRAME;


    public Simulation(GameContext context) {
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

        random = new Random();
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



            for(int i = 0; i < actors.size();i++) {
                Actor actor = actors.get(i);
                actor.doAction();
            }




            // terrain spawn
            if (frame - LAST_TERRAIN_SPAWN_FRAME >= TERRAIN_SPAWN_FRAME_INTERVAL) {
                spawnTerrain("TREE", 3, DISTANCES_BETWEEN_TERRAIN);
                spawnTerrain("ROCK_1",3, DISTANCES_BETWEEN_TERRAIN);
                spawnTerrain("ROCK_2",5, DISTANCES_BETWEEN_TERRAIN);
                LAST_TERRAIN_SPAWN_FRAME = frame;
            }

            // spawn weapon
            if (frame == 0) {
                Actor weapon = actorPool.get("KNIFE");
                weapon.getAnimationHandler().rotate(25f);
                weapon.addAction(
                        WeaponActions.fireAtClosestActor(
                                FilterTool.Category.ENEMY,
                                player.getSpeed() + weapon.getSpeed(),
                                player,
                                millisToFrames(100),
                                actors,
                                SPAWN_RECT));

                actors.add(weapon);
            }



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


    private void spawnTerrain(String terrainName, int num, List<Float> distancesBetweenSpawn) {

        float randomDistance = distancesBetweenSpawn.get(random.nextInt(distancesBetweenSpawn.size()));
        List<Vector2> occupiedSpawns =  SpawnCoordinates.getOccupiedPositions(objects);
        List<Vector2> availableSpawns = SpawnCoordinates.fixedSpawnPoints(num, GameContext.SPAWN_RECT,randomDistance,player.getBody().getPosition(),occupiedSpawns);

        for(Vector2 spawn: availableSpawns) {
            GameObject terrain = objectPool.get(terrainName);
            terrain.setPosition(spawn);;

            objects.add(terrain);

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



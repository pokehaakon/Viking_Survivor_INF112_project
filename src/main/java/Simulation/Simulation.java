package Simulation;

import Contexts.GameContext;
import GameObjects.Actor;
import GameObjects.GameObject;
import GameObjects.ObjectActions.WeaponActions;
import InputProcessing.KeyStates;
import Simulation.Coordinates.SpawnCoordinates;
import Tools.ExcludeFromGeneratedCoverage;
import Tools.FilterTool;
import com.badlogic.gdx.math.Vector2;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;


import static Contexts.GameContext.SPAWN_RECT;
import static Tools.ListTools.removeDestroyed;
import static VikingSurvivor.app.HelloWorld.SET_FPS;
import static VikingSurvivor.app.HelloWorld.millisToFrames;

public class Simulation implements Runnable {
    static public final AtomicLong EXP = new AtomicLong();
    static public final int SET_UPS = 60;

    static private final List<Float> DISTANCES_BETWEEN_TERRAIN = List.of(30f,60f,90f);
    static private final float TERRAIN_SPAWN_FRAME_INTERVAL = (float) ((1000) * SET_FPS) / 1000;
    static private final long DELTA_TIME_FRAME = 1_000_000_000/SET_UPS; // sec / update * 10^9 nanosec / sec

    private final Random random = new Random();
    private final GameContext context;

    private boolean quit = false;
    private boolean paused = false;
    private long frame = 0;
    private long LAST_TERRAIN_SPAWN_FRAME;


    public Simulation(GameContext context) {
        this.context = context;
    }

    @ExcludeFromGeneratedCoverage(reason = "not really testable :)")
    @Override
    public void run() {
        long lastFrameStart;


        while (!quit) {
            while(paused) {
                synchronized (this) {try {this.wait();} catch (InterruptedException ignored) {}}
            }

            lastFrameStart = System.nanoTime();
            long t0 = System.nanoTime();

            if (context.keyStates.getState(KeyStates.GameKey.QUIT)) stopSim();



            for(int i = 0; i < context.actors.size();i++) {
                Actor actor = context.actors.get(i);
                actor.doAction();
                actor.updateDirectionState();
                actor.updateAnimationState();
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
                    Actor weapon = context.actorPool.get("KNIFE");
                    weapon.getAnimationHandler().rotate(25f);
                    weapon.setPosition(context.player.getPosition());
                    weapon.addAction(
                            WeaponActions.fireAtClosestActor(
                                    FilterTool.Category.ENEMY,
                                    context.player.getSpeed() + weapon.getSpeed(),
                                    context.player,
                                    millisToFrames(200),
                                    context.actors,
                                    SPAWN_RECT));

                    context.actors.add(weapon);


            }



            doSpinSleep(lastFrameStart, DELTA_TIME_FRAME);
            context.UPS.add(System.nanoTime() - lastFrameStart);

            while (frame > context.synchronizer.get()){continue;}
            context.renderLock.lock();

            context.gameWorld.act(frame);

            context.world.step(1/(float) 60, 10, 10);


            removeDestroyed(context.actors, context.actorPool, true);
            removeDestroyed(context.objects, context.objectPool, true);

            //context.synchronizer.decrementAndGet();
            context.renderLock.unlock();
            long simTimeToUpdate = System.nanoTime() - t0;


            context.UpdateTime.add(simTimeToUpdate);

            frame++;

            if(context.player.getHP() <= 0) {
                context.gameOver();
                stopSim();
            }

        }

    }

    private void doSpinSleep(long lastFrameStart, long dt) {
        while(lastFrameStart + dt - System.nanoTime() > 0) {continue;}
    }


    private void spawnTerrain(String terrainName, int num, List<Float> distancesBetweenSpawn) {

        float randomDistance = distancesBetweenSpawn.get(random.nextInt(distancesBetweenSpawn.size()));
        List<Vector2> occupiedSpawns =  SpawnCoordinates.getOccupiedPositions(context.objects);
        List<Vector2> availableSpawns = SpawnCoordinates.fixedSpawnPoints(num, GameContext.SPAWN_RECT,randomDistance,context.player.getBody().getPosition(),occupiedSpawns);

        for(Vector2 spawn: availableSpawns) {
            GameObject terrain = context.objectPool.get(terrainName);
            terrain.setPosition(spawn);

            context.objects.add(terrain);

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
}



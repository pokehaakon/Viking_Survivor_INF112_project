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


    private boolean quit = false;
    private boolean paused = false;
    private long frame = 0;

    //temp
    private long lastSpawnTime;



    private List<Actor> tempPickups;

    private List<Actor> drawableEnemies;
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

        tempPickups = new ArrayList<>();
        random = new Random();
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


            int actorLength = actors.size();
            for(int i = 0; i < actorLength;i++) {
                Actor actor = actors.get(i);
//                if(isInCategory(actor.getBody(), FilterTool.Category.PICKUP)) {
//                    continue;
//                }
                actor.doAction();
            }



            //gameWorld.act(frame);

            // random spawning for now
            if (TimeUtils.millis() - lastSpawnTime > 1000) {
//                Actor pickup = actorPool.get("SKULL_PICKUP");
//                //pickup.addAction(giveHP(player,10), setWeaponSpeed(10000,10));
//                pickup.addAction(PickupActions.startTemporaryActionChange(FilterTool.Category.WEAPON,5000,actors,WeaponActions.orbitActor(0.4f,10,  player, 0, 0)));
                //pickup.addAction(changeAction(actors,FilterTool.Category.WEAPON,WeaponActions.fireAtClosestEnemy(50,player,1000,actors, new Vector2(200,200))));
                //pickup.setPosition(new Vector2(player.getBody().getPosition().x+50,player.getBody().getPosition().y +20));
                //actors.add(pickup);

                //spawnRandomEnemies(5,Arrays.asList(ActorActions.destroyIfDefeated(player),ActorActions.chasePlayer(player), coolDown(500)));
                spawnTerrain("TREE", 3);
                spawnTerrain("ROCK_1",3);
                spawnTerrain("ROCK_2",5);
//                spawnEnemies("ORC",10,
//                        chaseActor(player),
//                        spawnPickupsIfKilled(1,"HP_PICKUP", tempPickups,context.getActorPool(),giveHP(player,10)),destroyIfDefeated()
//                );
            }

            if (frame == 0) {
                Actor weapon = actorPool.get("KNIFE");
                weapon.getAnimationHandler().rotate(25f);
                weapon.addAction(WeaponActions.fireAtClosestActor(FilterTool.Category.ENEMY, player.getSpeed() + weapon.getSpeed(), player, 100, actors, SPAWN_RECT));
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

            actors.addAll(tempPickups);
            tempPickups.clear();

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


    private void spawnTerrain(String TerrainName, int num) {
        List<Float> distances = List.of(30f,60f,90f);
        float randomDistance = distances.get(random.nextInt(distances.size()));
        List<Vector2> occupiedSpawns =  SpawnCoordinates.getOccupiedPositions(objects);
        List<Vector2> availableSpawns = SpawnCoordinates.fixedSpawnPoints(num, GameContext.SPAWN_RECT,randomDistance,player.getBody().getPosition(),occupiedSpawns);

        for(Vector2 spawn: availableSpawns) {
            GameObject terrain = objectPool.get(TerrainName);
            terrain.setPosition(spawn);;

            objects.add(terrain);

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



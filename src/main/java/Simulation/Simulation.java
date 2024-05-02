package Simulation;

import Contexts.ReleaseCandidateContext;
import GameObjects.Actor;
import GameObjects.GameObject;
import GameObjects.ObjectActions.Action;
import GameObjects.ObjectActions.WeaponActions;
import InputProcessing.KeyStates;
import Simulation.Coordinates.SpawnCoordinates;
import Tools.FilterTool;
import Tools.Pool.ObjectPool;
import Tools.RollingSum;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;


import static GameObjects.ObjectActions.KilledAction.*;
import static GameObjects.ObjectActions.MovementActions.chaseActor;
import static GameObjects.ObjectActions.PickupActions.giveHP;
import static GameObjects.ObjectActions.PickupActions.setWeaponSpeed;
import static Simulation.ObjectContactListener.isInCategory;
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



    private List<Actor> tempPickups;

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

        tempPickups = context.getPickup();

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

            for (Actor actor : actors) {

                //
                if(isInCategory(actor.getBody(), FilterTool.Category.PICKUP)) {
                    continue;
                }
                actor.doAction();
            }



            // random spawning for now
            if (TimeUtils.millis() - lastSpawnTime > 10000) {
                Actor pickup = actorPool.get("XP_PICKUP");
                pickup.addAction(giveHP(player,10), setWeaponSpeed(10000,10));
                pickup.setPosition(new Vector2(player.getBody().getPosition().x+50,player.getBody().getPosition().y +20));
                actors.add(pickup);

                //spawnRandomEnemies(5,Arrays.asList(ActorActions.destroyIfDefeated(player),ActorActions.chasePlayer(player), coolDown(500)));
                spawnTerrain("TREE", 5);
                spawnEnemies("ORC",10,
                        chaseActor(player),
                        spawnPickupsIfKilled(1,"HP_PICKUP", tempPickups,context.getActorPool(),giveHP(player,10), setWeaponSpeed(5000,10)),destroyIfDefeated());
            }

            if (frame == 10) {
                Actor a = actorPool.get("KNIFE");
                a.getAnimationHandler().rotate(20f);
                //TODO why isnt the weapon showing???
                //a.addAction(WeaponActions.fireAtClosestEnemy(50,player,1000,actors, new Vector2(200,200)));
                a.addAction(WeaponActions.orbitActor(0.1f,10,  player, 0, 0));
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
        List<Vector2> occupiedSpawns =  SpawnCoordinates.getOccupiedPositions(objects);
        List<Vector2> availableSpawns = SpawnCoordinates.fixedSpawnPoints(num, new Vector2(200,200),200,player.getBody().getPosition(),occupiedSpawns);

        for(Vector2 spawn: availableSpawns) {
            GameObject terrain = objectPool.get(TerrainName);
            terrain.setPosition(spawn);;

            objects.add(terrain);

        }


        lastSpawnTime = TimeUtils.millis();
    }

    private void spawnEnemies(String type, int n, Action... actions) {
        for(Actor enemy : context.getActorPool().get(type, n)) {
            enemy.setPosition(SpawnCoordinates.randomSpawnPoint(player.getBody().getPosition(), 200));
            enemy.addAction(actions);
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



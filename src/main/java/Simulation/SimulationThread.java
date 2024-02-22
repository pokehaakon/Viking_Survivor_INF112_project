package Simulation;

import InputProcessing.GameKey;
import InputProcessing.KeyStates;
import Tools.RollingSum;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Set;
import java.util.concurrent.locks.Lock;

public class SimulationThread extends Thread {
    private Lock renderLock;
    private KeyStates keyStates;
    private World world;
    private RollingSum updateTime, UPS;
    private Body player;
    private Array<Body> enemies;
    private int SET_UPS = 60;
    private boolean quit = false;
    private Set<Body> toBeKilled;
    private boolean paused = false;

    public SimulationThread(Lock renderLock, KeyStates keyStates, World world, Set<Body> toBeKilled, RollingSum updateTime, RollingSum UPS, Body player) {
        this.renderLock = renderLock;
        this.keyStates = keyStates;
        this.world = world;
        this.updateTime = updateTime;
        this.UPS = UPS;
        this.toBeKilled = toBeKilled;

        Array<Body> a = new Array<>();
        world.getBodies(a);
        this.player = player;
        enemies = new Array<>();

        for (Body b : a) {
            if (b == player) {
                continue;
            }
            enemies.add(b);
        }

    }

    @Override
    public void run() {

        long dt = 1_000_000_000/SET_UPS; // sec / update * 10^9 nanosec / sec
        long lastFrameStart = System.nanoTime();


        while (!quit) {
            while(paused) {
                synchronized (this) {try {this.wait();} catch (InterruptedException ignored) {}}
            }
            doSleep(lastFrameStart, dt);
            UPS.add(System.nanoTime() - lastFrameStart);

            renderLock.lock();
            lastFrameStart = System.nanoTime();
            long simTimeToUpdate = doSimStep();

            renderLock.unlock();

            updateTime.add(simTimeToUpdate);
        }
    }

    private void doSleep(long lastFrameStart, long dt) {
        long sleepTime = lastFrameStart + dt - System.nanoTime(); //time to sleep in nanoseconds
        //if sleepTime non-Positive updates take to long for the set UPS!
        if (sleepTime >= 0) {
            try {Thread.sleep(sleepTime/1_000_000, (int) (sleepTime % 1_000_000));} catch (InterruptedException ignored) {}
        }
    }

    private long doSimStep() {
        long t0 = System.nanoTime();

        Vector2 vel = new Vector2(0, 0);
        int dy = 1, dx = 1;
        vel.x = 0;
        vel.y = 0;

        if (keyStates.getState(GameKey.UP)) {
            vel.y += dy;
        }
        if (keyStates.getState(GameKey.DOWN)) {
            vel.y += -dy;
        }
        if (keyStates.getState(GameKey.LEFT)) {
            vel.x += -dx;
        }
        if (keyStates.getState(GameKey.RIGHT)) {
            vel.x += dx;
        }
        if (keyStates.getState(GameKey.QUIT)) {
            stopSim();
        }
        vel.setLength(60*2);

        player.setLinearVelocity(vel);

        //calculation for enemies
        Vector2 eVel = new Vector2(0, 0);
        Vector2 playerPos = player.getWorldCenter();
        for (Body enemy : enemies) {
            eVel.x = 0;
            eVel.y = 0;
            eVel.add(playerPos).sub(enemy.getWorldCenter());

            eVel.setLength(60 * 0.3f);
            enemy.setLinearVelocity(eVel);
        }

        world.step(1/(float) SET_UPS, 5, 5);

        for (Body b : toBeKilled) {
            world.destroyBody(b);
        }
        toBeKilled.clear();

        return System.nanoTime() - t0;
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

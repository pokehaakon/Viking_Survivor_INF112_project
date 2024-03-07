package Simulation;

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
    private boolean quit = false;
    private Set<Body> toBeKilled;
    private boolean paused = false;
    private long frame = 0;
    public final int SET_UPS = 60;

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
            //doSleep(lastFrameStart, dt);
            doSpinSleep(lastFrameStart, dt);
            UPS.add(System.nanoTime() - lastFrameStart);

            renderLock.lock();
            lastFrameStart = System.nanoTime();
            long simTimeToUpdate = doSimStep();

            renderLock.unlock();

            updateTime.add(simTimeToUpdate);
            frame++;
        }
    }

    private void doSleep(long lastFrameStart, long dt) {
        long sleepTime = lastFrameStart + dt - System.nanoTime(); //time to sleep in nanoseconds
        //if sleepTime non-Positive updates take to long for the set UPS!
        if (sleepTime > 0) {
            try {Thread.sleep(sleepTime/1_000_000, (int) (sleepTime % 1_000_000));} catch (InterruptedException ignored) {}
        }
    }

    private void doSpinSleep(long lastFrameStart, long dt) {
        while(lastFrameStart + dt - System.nanoTime() > 0) {}
    }

    private long doSimStep() {
        long t0 = System.nanoTime();

        Vector2 vel = new Vector2(0, 0);
        int dy = 1, dx = 1;
        vel.x = 0;
        vel.y = 0;

        if (keyStates.getState(KeyStates.GameKey.UP)) {
            vel.y += dy;
        }
        if (keyStates.getState(KeyStates.GameKey.DOWN)) {
            vel.y += -dy;
        }
        if (keyStates.getState(KeyStates.GameKey.LEFT)) {
            vel.x += -dx;
        }
        if (keyStates.getState(KeyStates.GameKey.RIGHT)) {
            vel.x += dx;
        }
        if (keyStates.getState(KeyStates.GameKey.QUIT)) {
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

        world.step(1/(float) SET_UPS, 10, 10);

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
    public long getFrameNumber() {
        return frame;
    }
}

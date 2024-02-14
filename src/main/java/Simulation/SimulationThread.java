package Simulation;

import InputProcessing.GameKey;
import InputProcessing.KeyStates;
import Tools.RollingSum;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.concurrent.locks.Lock;

public class SimulationThread extends Thread {
    private Lock renderLock;
    private KeyStates keyStates;
    private World world;
    private RollingSum updateTime, UPS;
    private Body player, enemy;
    private int SET_UPS = 60;
    private boolean quit = false;

    public SimulationThread(Lock renderLock, KeyStates keyStates, World world, RollingSum updateTime, RollingSum UPS, Body player) {
        this.renderLock = renderLock;
        this.keyStates = keyStates;
        this.world = world;
        this.updateTime = updateTime;
        this.UPS = UPS;

        Array<Body> a = new Array<>();
        world.getBodies(a);
        this.player = player;

        for (Body b : a.iterator()) {
            if (b == player) {
                continue;
            }
            enemy = b;

        }

        enemy.setFixedRotation(true);
        player.setFixedRotation(true);
    }

    @Override
    public void run() {

        long dt = 1_000_000_000/SET_UPS; // sec / update * 10^9 nanosec / sec
        long lastFrameStart = System.nanoTime();


        while (!quit) {
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

        Vector2 eVel = new Vector2(0, 0);
        //player.getWorldCenter();
        eVel.add(player.getWorldCenter()).sub(enemy.getWorldCenter());

        eVel.setLength(60 * 0.5f);
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
        vel.setLength(60);

        player.setLinearVelocity(vel);
        enemy.setLinearVelocity(eVel);
        world.step(1/(float) SET_UPS, 5, 5);

        return System.nanoTime() - t0;
    }

    public void stopSim() {
        quit = true;
    }
}

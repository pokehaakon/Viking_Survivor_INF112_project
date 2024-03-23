package Simulation;

import InputProcessing.Contexts.MVPContext;
import InputProcessing.KeyStates;
import Tools.RollingSum;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Set;
import java.util.concurrent.locks.Lock;

public class SimulationThread extends Thread {
    private Lock renderLock;
    private KeyStates keyStates;
    private World world;
    private RollingSum updateTime, UPS;

    private boolean quit = false;
    private Set<Body> toBeKilled;
    private boolean paused = false;
    private long frame = 0;
    public final int SET_UPS = 60;

    private final MVPContext context;

    public SimulationThread(MVPContext context) {
        this.context = context;
        renderLock = context.getRenderLock();
        keyStates = context.getKeyStates();
        world = context.getWorld();
        updateTime = context.getUpdateTime();
        UPS = context.getUPS();
        toBeKilled = context.getToBoKilled();

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

        if (keyStates.getState(KeyStates.GameKey.QUIT)) {
            stopSim();
        }

        //context.updateActors();

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



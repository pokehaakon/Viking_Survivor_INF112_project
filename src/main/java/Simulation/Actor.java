package Simulation;

import java.util.function.Consumer;

public abstract class Actor implements IGameObject {
    private boolean destroyed = false;
    private final String name;

    public Actor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void destroy() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    void handleDamage(int dmg) {
        //TODO
    }

    void doMove(Consumer<Actor> a)  { //?
        //TODO
    }

    //enum collisionDetected(obj 1, obj2) //TODO
}

package GameObjects.Pool;

import GameObjects.Factories.IFactory;
import GameObjects.GameObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.*;


public class ObjectPool<T extends GameObject> {
    private final IFactory<T> factory;
    private final Map<String, SmallPool<T>> objectPool;
    private final Random random;

    private int poolSize;
    private final World world;

    /**
     * An object pool is a hash map of object types as keys and linked list of GameObjects as values
     * When we start the game, we create and store a desired amount of each object type
     * We use this pool to recirculate objects, so we don't have to create new instances every time an object spawns
     * @param factory desired object factory
     * @param poolSize number of objects to create of each object type
     */
    public ObjectPool(World world, IFactory<T> factory, int poolSize) {
        this.world = world;
        this.factory = factory;
        this.poolSize = poolSize;
        this.objectPool = new HashMap<>();
        //this.objectPool = new HashMap<>();
        this.random = new Random();

        if (poolSize <= 0) {
            throw new IllegalArgumentException("Pool size must be greater than zero!");
        }

    }

    private void createObjectPool(String name, int size) {
        objectPool.put(name, new SmallPool<>(world, () -> factory.create(name), size));
    }


    public T get(String name) {
        createSmallPoolIfAbsent(name);
        return objectPool.get(name).get();
    }

    /**
     * Polls GameObjects from the object pool and activate their bodies
     * @param name desired object type
     * @param num number of objects to obtain
     * @return a list of GameObjects
     */
    public List<T> get(String name, int num) {
        createSmallPoolIfAbsent(name);
        return objectPool.get(name).get(num);
    }

    /**
     * Returns object to object pool, deactivates their bodies and resets its destroyed-tag. Ready to be spawned again.
     * @param object the object to return
     */
    public void returnToPool(T object) {
        createSmallPoolIfAbsent(object.getType());
        objectPool.get(object.getType()).returnToPool(object);
    }

    public Map<String, SmallPool<T>> getObjectPool() {
        return objectPool;
    }

    public SmallPool<T> getSmallPool(String name) {
        createSmallPoolIfAbsent(name);
        return objectPool.get(name);
    }

    public void setPosition(Vector2 vector2) {
        for (SmallPool<T> pool : objectPool.values()) {
            for (T object : pool.getPool()) {
                object.setPosition(vector2);
            }
        }
    }

    private void createSmallPoolIfAbsent(String name) {
        if (objectPool.containsKey(name)) return;
        objectPool.put(
                name,
                new SmallPool<>(
                        world,
                        () -> factory.create(name),
                        poolSize
                )
        );
    }
}

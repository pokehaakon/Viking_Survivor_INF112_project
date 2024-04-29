package GameObjects.Pool;

import GameObjects.Factories.IFactory;
import GameObjects.GameObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import javax.lang.model.type.UnknownTypeException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;


public final class ObjectPool<T extends GameObject> implements IPool<T> {
    private final IFactory<T> factory;
    private final Map<String, IPool<T>> objectPool;
    private final Random random;

    private int poolSize;
    private final World world;
    private final Consumer<String> superFactoryUpdater;

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
        this.superFactoryUpdater = (key) -> {}; // no-op
        this.objectPool = new HashMap<>();
        //this.objectPool = new HashMap<>();
        this.random = new Random();


        if (poolSize <= 0) {
            throw new IllegalArgumentException("Pool size must be greater than zero!");
        }
    }

    public ObjectPool(World world, IFactory<T> factory, int poolSize, Function<IPool<T>, Consumer<String>> superFactoryUpdaterCreator ) {
        this.world = world;
        this.factory = factory;
        this.poolSize = poolSize;
        this.objectPool = new HashMap<>();
        superFactoryUpdater = superFactoryUpdaterCreator.apply(this);
        //this.objectPool = new HashMap<>();
        this.random = new Random();

        if (poolSize <= 0) {
            throw new IllegalArgumentException("Pool size must be greater than zero!");
        }
    }

    @SuppressWarnings("unchecked")
    public <R extends T> ObjectPool<R> createSubPool(IFactory<R> factory) {
        return new ObjectPool<>(world, factory, poolSize, (objectPool1) -> (s) -> this.objectPool.put(s, (IPool<T>) objectPool1));
    }

    @Override
    public T get(String name) {
        createSmallPoolIfAbsent(name);
        return objectPool.get(name).get(name);
    }

    /**
     * Polls GameObjects from the object pool and activate their bodies
     * @param name desired object type
     * @param num number of objects to obtain
     * @return a list of GameObjects
     */
    @Override
    public List<T> get(String name, int num) {
        createSmallPoolIfAbsent(name);
        return objectPool.get(name).get(name, num);
    }

    /**
     * Returns object to object pool, deactivates their bodies and resets its destroyed-tag. Ready to be spawned again.
     * @param object the object to return
     */
    @Override
    public void returnToPool(T object) {
        createSmallPoolIfAbsent(object.getType());
        objectPool.get(object.getType()).returnToPool(object);
    }

    public Map<String, IPool<T>> getObjectPool() {
        return objectPool;
    }

    @Override
    public SmallPool<T> getSmallPool(String name) {
        createSmallPoolIfAbsent(name);
        return objectPool.get(name).getSmallPool(name);
    }

    private void createSmallPoolIfAbsent(String name) {
        if (objectPool.containsKey(name)) return;
        objectPool.put(
                name,
                new SmallPool<>(
                        world,
                        () -> factory.create(name),
                        poolSize,
                        name
                )
        );
    }
}

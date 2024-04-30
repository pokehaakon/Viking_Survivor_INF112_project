package Tools.Pool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ObjectPool<T extends Poolable> implements IPool<T> {
    private final Function<String, T> factory;
    private final Map<String, IPool<T>> objectPool = new HashMap<>();
    private final Consumer<String> superFactoryUpdater;

    /**
     * A pool of gameObjects, where the objects are either created using the given factory, are taken from a pool of
     * already created objects. We use 'returnToPool' to add the objects back into the pool.
     *
     * @param factory The factory that creates the objects
     */
    public ObjectPool(Function<String, T> factory) {
        this.factory = factory;
        this.superFactoryUpdater = (key) -> {}; // no-op
    }

    /**
     * Same as the public constructor {@link #ObjectPool(Function)}, but where we can give a constructor function
     * 'superPoolUpdaterConstructor' that takes 'this' pool on construction and returns a consumer taking a String.
     * When this pool "learns" to create a new Object, it uses this function to register it with its super pool.
     *
     * @param factory The factory that creates the objects
     * @param superPoolUpdaterCreator The super pool register creator function
     */
    private ObjectPool(Function<String, T> factory, Function<IPool<T>, Consumer<String>> superPoolUpdaterCreator ) {
        this.factory = factory;
        superFactoryUpdater = superPoolUpdaterCreator.apply(this);
    }

    /**
     * Creates a new sub-pool of this pool, with another factory. This pool will be able to create/give all of the
     * object the new pool is able to create.
     * @param factory the factory for the new sub-pool
     * @return the new sub-pool
     * @param <R> the type of objects of the new pool, must be sub-type of the type of this pool
     */
    @SuppressWarnings("unchecked")
    public <R extends T> ObjectPool<R> createSubPool(Function<String, R> factory) {
        return new ObjectPool<>(factory, (objectPool1) -> (s) -> this.objectPool.put(s, (IPool<T>) objectPool1));
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
        createSmallPoolIfAbsent(object.getName());
        objectPool.get(object.getName()).returnToPool(object);
    }

    /**
     * Get the SmallPool that can create 'name', creates a new one if it does not already exist
     * @param name the name of the objects
     * @return A SmallPool that contains 'name' Objects
     */
    @Override
    public SmallPool<T> getSmallPool(String name) {
        createSmallPoolIfAbsent(name);
        return objectPool.get(name).getSmallPool(name);
    }

    private void createSmallPoolIfAbsent(String name) {
        if (objectPool.containsKey(name)) return;
        superFactoryUpdater.accept(name);
        objectPool.put(
                name,
                new SmallPool<>(
                        () -> factory.apply(name),
                        name
                )
        );
    }
}

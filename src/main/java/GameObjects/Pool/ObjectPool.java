package GameObjects.Pool;

import GameObjects.Factories.IFactory;
import GameObjects.GameObject;
import com.badlogic.gdx.physics.box2d.World;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ObjectPool<T extends GameObject> implements IPool<T> {
    private final IFactory<T> factory;
    private final Map<String, IPool<T>> objectPool = new HashMap<>();
    private final World world;
    private final Consumer<String> superFactoryUpdater;

    /**
     * A pool of gameObjects, where the objects are either created using the given factory, are taken from a pool of
     * already created objects. We use 'returnToPool' to add the objects back into the pool.
     *
     * @param world The world where the objects are created
     * @param factory The factory that creates the objects
     */
    public ObjectPool(World world, IFactory<T> factory) {
        this.world = world;
        this.factory = factory;
        this.superFactoryUpdater = (key) -> {}; // no-op
    }

    /**
     * Same as the public constructor {@link #ObjectPool(World, IFactory)}, but where we can give a constructor function
     * 'superPoolUpdaterConstructor' that takes 'this' pool on construction and returns a consumer taking a String.
     * When this pool "learns" to create a new Object, it uses this function to register it with its super pool.
     *
     * @param world The world where the objects are created
     * @param factory The factory that creates the objects
     * @param superPoolUpdaterCreator The super pool register creator function
     */
    private ObjectPool(World world, IFactory<T> factory, Function<IPool<T>, Consumer<String>> superPoolUpdaterCreator ) {
        this.world = world;
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
    public <R extends T> ObjectPool<R> createSubPool(IFactory<R> factory) {
        return new ObjectPool<>(world, factory, (objectPool1) -> (s) -> this.objectPool.put(s, (IPool<T>) objectPool1));
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
                        world,
                        () -> factory.create(name),
                        name
                )
        );
    }
}

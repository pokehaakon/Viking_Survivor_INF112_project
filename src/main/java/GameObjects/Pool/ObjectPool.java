package GameObjects.Pool;

import GameObjects.Factories.IFactory;
import GameObjects.GameObject;
import com.badlogic.gdx.physics.box2d.World;

import java.util.*;


public class ObjectPool<T extends GameObject<E>, E extends Enum<E>> {
    private final IFactory<T, E> factory;
    private final Map<E, SmallPool<T>> objectPool;
    private final List<E> objectTypes;
    private final Random random;

    private final World world;

    /**
     * An object pool is a hash map of object types as keys and linked list of GameObjects as values
     * When we start the game, we create and store a desired amount of each object type
     * We use this pool to recirculate objects, so we don't have to create new instances every time an object spawns
     * @param factory desired object factory
     * @param objectTypes  list of the different types of each object
     * @param poolSize number of objects to create of each object type
     */
    public ObjectPool(World world, IFactory<T, E> factory, List<E> objectTypes, int poolSize) {
        this.world = world;
        this.factory = factory;
        this.objectTypes = objectTypes;
        this.objectPool = new HashMap<>();
        this.random = new Random();

        if (poolSize <= 0) {
            throw new IllegalArgumentException("Pool size must be greater than zero!");
        }

        for (E objectType : objectTypes) {
            createObjectPool(objectType, poolSize);
        }
    }

    private void createObjectPool(E type, int size) {
        objectPool.put(type, new SmallPool<>(world, () -> factory.create(type), size));
    }

    public T getRandom() {
        E randomObjectType = objectTypes.get(random.nextInt(objectTypes.size()));
        return get(randomObjectType);
    }

    public T get(E type) {
        return objectPool.get(type).get();
    }

    /**
     * Polls random GameObject-objects from the object pool and activate their bodies
     * @param num number of objects to obtain
     * @return a list of GameObjects
     */
    public List<T> getRandom(int num) {
        List<T> objects = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            T obj = getRandom();
            objects.add(obj);
        }
        return objects;
    }

    /**
     * Polls GameObjects from the object pool and activate their bodies
     * @param type desired object type
     * @param num number of objects to obtain
     * @return a list of GameObjects
     */
    public List<T> get(E type, int num) {
        return objectPool.get(type).get(num);
    }

    /**
     * Returns object to object pool, deactivates their bodies and resets its destroyed-tag. Ready to be spawned again.
     * @param object the object to return
     */
    public void returnToPool(T object) {
        objectPool.get(object.getType()).returnToPool(object);
    }

    public Map<E, SmallPool<T>> getObjectPool() {
        return objectPool;
    }
}

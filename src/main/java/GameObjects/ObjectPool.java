package GameObjects;

import GameObjects.Factories.IFactory;
import com.badlogic.gdx.physics.box2d.World;

import java.util.*;


public class ObjectPool<T extends GameObject> {
    private final IFactory<T> factory;
    private final Map<String, Queue<T>> objectPool;
    private final List<String> objectTypes;
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
    public ObjectPool(World world, IFactory<T> factory, List<String> objectTypes, int poolSize) {
        this.world = world;
        this.factory = factory;
        this.objectTypes = objectTypes;
        this.objectPool = new HashMap<>();
        this.random = new Random();

        if (poolSize <= 0) {
            throw new IllegalArgumentException("Pool size must be greater than zero!");
        }

        for (String objectType : objectTypes) {
            createObjectPool(objectType, poolSize);
        }
    }

    private void createObjectPool(String type, int size) {
        Queue<T> pool = new LinkedList<>();
        for (T obj : factory.create(size, type)) {
            obj.addToWorld(world);
            obj.getBody().setActive(false);
            pool.add(obj);
        }
        objectPool.put(type, pool);
    }

    public T getRandom() {
        String randomObjectType = objectTypes.get(random.nextInt(objectTypes.size()));
        return get(randomObjectType);
    }

    public T get(String type) {
        Queue<T> pool = objectPool.get(type);
        T obj;
        if (!pool.isEmpty()) {
            obj = pool.poll();
        }
        else {
            obj = factory.create(type);
            obj.addToWorld(world);
        }
        obj.getBody().setActive(true);
        return obj;

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
    public List<T> get(String type, int num) {
        List<T> objects = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            T obj = get(type);
            objects.add(obj);
        }
        return objects;
    }

    /**
     * Returns object to object pool, deactivates their bodies and resets its destroyed-tag. Ready to be spawned again.
     * @param object the object to return
     */
    public void returnToPool(T object) {
        Queue<T> pool = objectPool.get(object.getType());
        if (pool != null) {
            object.getBody().setActive(false);
            object.revive();
            pool.add(object);
        }
    }

    public Map<String, Queue<T>> getObjectPool() {
        return objectPool;
    }
}

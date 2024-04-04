package GameObjects;

import GameObjects.Factories.IFactory;

import java.util.*;

public class ObjectPool<T extends GameObject> {
    private final IFactory<T> factory;
    private final Map<String, Queue<T>> objectPool;
    private final List<String> objectTypes;
    private final Random random;

    public ObjectPool(IFactory<T> factory, List<String> objectTypes, int poolSize) {
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
        }
        obj.getBody().setActive(true);
        return obj;

    }

    public List<T> getRandom(int num) {
        List<T> objects = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            T obj = getRandom();
            objects.add(obj);
        }
        return objects;
    }

    public List<T> get(String type, int num) {
        List<T> objects = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            T obj = get(type);
            objects.add(obj);
        }
        return objects;
    }

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

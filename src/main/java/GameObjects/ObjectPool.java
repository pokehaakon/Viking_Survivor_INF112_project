package GameObjects;

import GameObjects.Actors.Enemy.Enemy;
import GameObjects.Factories.EnemyFactory;
import GameObjects.Factories.IFactory;
import com.badlogic.gdx.physics.box2d.World;

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

    public T getRandomObject() {
        String randomObjectType = objectTypes.get(random.nextInt(objectTypes.size()));
        return getObject(randomObjectType);
    }

    public T getObject(String type) {
        Queue<T> pool = objectPool.get(type);
        if (pool != null && !pool.isEmpty()) {
            T obj = pool.poll();
            obj.getBody().setActive(true);
            return obj;
        } else {
            T obj =  factory.create(type);
            obj.getBody().setActive(true);
            return obj;
        }
    }

    public List<T> getRandomObjects(int num) {
        List<T> objects = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            T obj = getRandomObject();
            objects.add(obj);
        }
        return objects;
    }

    public List<T> getObjects(String type, int num) {
        List<T> objects = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            T obj = getObject(type);
            objects.add(obj);
        }
        return objects;
    }

    public void returnObject(T object) {
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

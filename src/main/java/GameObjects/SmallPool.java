package GameObjects;

import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

public class SmallPool<T extends GameObject<?>> {

    private final World world;
    private final Supplier<T> factory;
    private final Queue<T> pool;

    public SmallPool(World world, Supplier<T> factory, int poolSize) {
        if (poolSize <= 0) {
            throw new IllegalArgumentException("Pool size must be greater than zero!");
        }

        this.world = world;
        this.factory = factory;
        this.pool = new ArrayDeque<>(poolSize);

        for (int i = 0; i<poolSize; i++) {
            T obj = factory.get();
            obj.addToWorld(world);
            returnToPool(obj);
        }
    }

    public T get() {
        T obj;
        if (pool.isEmpty()) {
            obj = factory.get();
            obj.addToWorld(world);
        } else {
            obj = pool.poll();
        }
        obj.getBody().setActive(true);
        return obj;
    }

    public List<T> get(int n) {
        List<T> ls = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            ls.add(get());
        }
        return ls;
    }

    public void returnToPool(T obj) {
        obj.getBody().setActive(false);
        obj.revive();
        pool.add(obj);
    }

    public int size() {
        return pool.size();
    }

    public boolean isEmpty() {
        return pool.isEmpty();
    }

    public Queue<T> getPool() {return pool;}
}

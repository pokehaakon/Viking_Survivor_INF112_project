package GameObjects.Pool;

import GameObjects.GameObject;
import com.badlogic.gdx.physics.box2d.World;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class SmallPool<T extends GameObject> implements IPool<T> {
    private final World world;
    private final Supplier<T> factory;
    private final Queue<T> pool;
    private final String name;

    public SmallPool(World world, Supplier<T> factory, int poolSize, String name) {
        if (poolSize <= 0) {
            throw new IllegalArgumentException("Pool size must be greater than zero!");
        }

        this.world = world;
        this.factory = factory;
        this.name = name;
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

    public List<T> get(int num) {
        List<T> ls = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            ls.add(get());
        }
        return ls;
    }

    @Override
    public T get(String name) {
        if (!Objects.equals(name, this.name))
            throw new IllegalArgumentException("Tried to get a '" + name + "' from a '" + this.name +"' smallPool!");
        return get();
    }

    @Override
    public List<T> get(String name, int num) {
        if (!Objects.equals(name, this.name))
            throw new IllegalArgumentException("Tried to get a '" + name + "' from a '" + this.name +"' smallPool!");
        return get(num);
    }

    @Override
    public void returnToPool(T obj) {
        obj.getBody().setActive(false);
        obj.revive();
        pool.add(obj);
    }

    @Override
    public SmallPool<T> getSmallPool(String name) {
        if (!Objects.equals(name, this.name))
            throw new IllegalArgumentException("Tried to get a '" + name + "' smallPool, but this is a '" + this.name +"' smallPool!");

        return this;
    }

//    public int size() {
//        return pool.size();
//    }

//    public boolean isEmpty() {
//        return pool.isEmpty();
//    }

//    public Queue<T> getPool() {return pool;}
}

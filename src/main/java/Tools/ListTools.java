package Tools;

import GameObjects.Actors.Enemy;
import GameObjects.GameObject;
import GameObjects.Pool.ObjectPool;
import GameObjects.Pool.SmallPool;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class ListTools {
    public static <T> void compactList(List<T> ls) {
        int j = 0;
        for (int i = 0; i < ls.size(); i++) {
            T v = ls.get(i);
            if (v == null) continue;
            ls.set(j++, v);
        }
    }

    public static <T extends GameObject<?>> void removeDestroyed(List<T> ls, ObjectPool<T, ?> pool, boolean compress) {
        int i = 0;
        for (var obj : ls) {
            if (obj.isDestroyed()) {
                pool.returnToPool(obj);
            } else {
                ls.set(i++, obj);
            }
        }
        if (compress) ls.subList(i, ls.size()).clear();
    }

    public static <T extends GameObject<?>> void removeDestroyed(List<T> ls, SmallPool<T> pool, boolean compress) {
        int i = 0;
        for (var obj : ls) {
            if (obj.isDestroyed()) {
                pool.returnToPool(obj);
            } else {
                ls.set(i++, obj);
            }
        }
        if (compress) ls.subList(i, ls.size()).clear();
    }

    public static String findPrefix(String prefix, List<String> strings) {
        return findPrefixOptional(prefix, strings).orElseThrow(() -> new NoSuchElementException("No string in list has prefix '" + prefix + "', in list: " + strings));
    }

    public static Optional<String> findPrefixOptional(String prefix, List<String> strings) {
        for (String string : strings) {
            if (string.startsWith(prefix)) return Optional.of(string.replaceFirst(prefix, ""));
        }
        return Optional.empty();
    }

}

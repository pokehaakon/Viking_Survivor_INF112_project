package GameObjects.Factories;

import GameObjects.GameObject;

import java.util.*;
import java.util.function.Supplier;

public abstract class ExperimentalAbstractFactory {
    static private final Set<Map<String, Supplier<GameObject>>> factoryList = new HashSet<>();

    static protected void addFactory(Map<String, Supplier<GameObject>> factories) {
        factoryList.add(factories);
    }

    static public GameObject create(String name) {
        for (var map : factoryList) {
            if (map.containsKey(name)) return map.get(name).get();
        }

        throw new IllegalArgumentException("Factory does not know how to create '" + name + "'");
    }

    static public void empty() {
        for (var factory : factoryList) {
            factory.clear();
        }
        factoryList.clear();
    }
}

package GameObjects.Factories;

import GameObjects.GameObject;
import GameObjects.ObjectTypes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ObjectFactory{

    private static final Map<String, Supplier<GameObject>> factoryMap = new HashMap<>();

    static {
        factoryMap.put("PlayerType", new PlayerFactory());
        factoryMap.put("EnemyType", new EnemyFactory());
        factoryMap.put("WeaponType", new WeaponFactory());
        factoryMap.put("TerrainType", new TerrainFactory());
        factoryMap.put("PickupType", new PickupsFactory());
    }


    public static <T extends GameObject> T create(String type) {

        IFactory<T> factory = getFactory(type);

        return factory.create(type);
    }

    public  static <T extends GameObject> List<T> create(int n, String type) {
        IFactory<T> factory = getFactory(type);
        List<T> list = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            list.add(factory.create(type));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private static <T extends GameObject> IFactory<T> getFactory(String type) {
        if(!factoryMap.containsKey(type)) {
            throw new IllegalArgumentException("The type " + type + " is not in the inventory");
        }

        return (IFactory<T>) factoryMap.get(type);

    }




}

//package GameObjects.Factories;
//
//import GameObjects.GameObject;
//import GameObjects.ObjectTypes.*;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Supplier;
//
//public class ObjectFactory{
//
//    private static final Map<String, IFactory<? extends GameObject<?>, ? extends Enum<?>>> factoryMap = new HashMap<>();
//
//
//
//    public ObjectFactory() {
//        // adding a map of ObjectType and corresponding factory
//        factoryMap.put(PlayerType.class.getSimpleName(), new PlayerFactory());
//        factoryMap.put(EnemyType.class.getSimpleName(), new EnemyFactory());
//        factoryMap.put(WeaponType.class.getSimpleName(), new WeaponFactory());
//        factoryMap.put(TerrainType.class.getSimpleName(), new TerrainFactory());
//        factoryMap.put(PickupType.class.getSimpleName(), new PickupsFactory());
//
//    }
//
//
//    public <T extends GameObject<E>,E extends Enum<E>> T create(E type) {
//
//        IFactory<T,E> factory = getFactory(type);
//
//        return factory.create(type);
//    }
//
//    public  <T extends GameObject<E>,E extends Enum<E>> List<T> create(int n, E type) {
//        IFactory<T,E> factory = getFactory(type);
//        List<T> list = new ArrayList<>();
//        for(int i = 0; i < n; i++) {
//            list.add(factory.create(type));
//        }
//        return list;
//    }
//
//    @SuppressWarnings("unchecked")
//    private <T extends GameObject<E>, E extends Enum<E>> IFactory<T,E> getFactory(Enum<?> type) {
//        if(!factoryMap.containsKey(type.getClass().getSimpleName())) {
//            throw new IllegalArgumentException("The type " + type + " is not in the inventory");
//        }
//
//        return (IFactory<T,E>) factoryMap.get(type.getClass().getSimpleName());
//
//    }
//
//
//
//
//}

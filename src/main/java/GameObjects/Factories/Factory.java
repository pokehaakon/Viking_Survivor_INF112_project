package GameObjects.Factories;
import GameObjects.GameObject;
import java.util.*;
import java.util.function.Supplier;

public abstract class Factory<T extends GameObject> implements IFactory<T>{
    protected final Map<String, Supplier<T>> supplierInventory = new HashMap<>();

    @Override
    public T create(String type) {

        if(Objects.isNull(supplierInventory.get(type))) {
            throw new NullPointerException("The factory does not contain the type " + type);
        }
        return supplierInventory.get(type).get();
    }
    @Override
    public List<T> create(String type, int n) {
        if(n <= 0) {
            throw new IllegalArgumentException("List size must be greater than zero!");
        }
        List<T> list = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            list.add(create(type));
        }
        return list;
    }

    @Override
    public void register(Supplier<T> supplier) {
        supplierInventory.put(supplier.get().getType(), supplier);
    }




}





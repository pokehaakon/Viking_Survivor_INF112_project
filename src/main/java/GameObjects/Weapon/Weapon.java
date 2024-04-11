package GameObjects.Weapon;

import GameObjects.Actors.ActorAction.ActorAction;
import GameObjects.Actors.ObjectTypes.WeaponType;
import GameObjects.Actors.Player.Player;
import GameObjects.SmallPool;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class Weapon implements IWeapon {
    protected final Player player;
    protected final List<WeaponBody> projectiles;
    protected final SmallPool<WeaponBody> pool;
    protected final WeaponType type;
    protected int activeProjectiles = 0;
    Consumer<WeaponBody> wakeFunction;
    public Weapon(Player player, List<WeaponBody> projectiles, SmallPool<WeaponBody> pool, WeaponType type, Consumer<WeaponBody> wakeFunction) {
        this.player = player;
        this.projectiles = projectiles;
        this.pool = pool;
        this.type = type;
        this.wakeFunction = wakeFunction;
    }

    protected final void addToProjectileList(WeaponBody... bodies) {
        activeProjectiles += bodies.length;
        for (WeaponBody b : bodies) {
            wakeFunction.accept(b);
            projectiles.add(b);
        }
    }

    @Override
    public void cleenUp() {
        if (activeProjectiles == 0) return;
        for (int i = 0; i<projectiles.size(); i++) {
            WeaponBody body = projectiles.get(i);
            if (body.getType() != type) continue;
            if (!body.isDestroyed()) continue;
            projectiles.set(i, null);
            pool.returnToPool(body);
            activeProjectiles--;
        }
    }
}

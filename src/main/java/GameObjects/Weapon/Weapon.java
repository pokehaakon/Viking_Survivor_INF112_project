package GameObjects.Weapon;

import GameObjects.Actors.ObjectTypes.WeaponType;
import GameObjects.Actors.Player.Player;

import java.util.Arrays;
import java.util.List;

public abstract class Weapon implements IWeapon {
    Player player;
    List<WeaponBody> projectiles;
    public Weapon(Player player, List<WeaponBody> projectiles) {
        this.player = player;
        this.projectiles = projectiles;
    }

    protected void addToProjectileList(WeaponBody... bodies) {
        projectiles.addAll(Arrays.stream(bodies).toList());
    }

}

package GameObjects.Actors;

import Rendering.Animations.AnimationRendering.AnimationHandler;
import GameObjects.ObjectTypes.PickupType;
import GameObjects.ObjectTypes.PlayerType;
import GameObjects.Actors.Stats.PlayerStats;
import GameObjects.BodyFeatures;

import java.util.ArrayList;
import java.util.List;

public class Player extends Actor<PlayerType> {
    public int level;
    public float XP;

    private PlayerStats stats;

    List<Weapon> weaponInventory;


    public Player(PlayerType type, AnimationHandler animationHandler, BodyFeatures bodyFeatures, float scale, PlayerStats stats) {
        super(type,animationHandler,bodyFeatures, scale);
        this.stats = stats;

        HP = stats.HP();
        speed = stats.speed();
        damage = stats.damage();
        armour = stats.armour();
        XP = stats.XP();
        level = 1;
        directionState = DirectionState.RIGHT;

        weaponInventory = new ArrayList<>();

    }



    public void setStats(PlayerStats newStats) {
        HP = newStats.HP();
        speed = newStats.speed();
        damage = newStats.damage();
        armour = newStats.armour();
        XP = newStats.XP();
    }

    public void addToInventory(Weapon weapon) {
        weapon.damage = this.damage;
        weaponInventory.add(weapon);
    }

    public List<Weapon> getInventory() {
        return weaponInventory;
    }


    public void pickup(Pickups pickup) {
        if(pickup.getType() == PickupType.PICKUPORB) {
            XP += 10;
        }
    }
}

package GameObjects.Actors;

import GameObjects.Animations.AnimationRendering.AnimationHandler;
import GameObjects.ObjectTypes.PickupType;
import GameObjects.ObjectTypes.PlayerType;
import GameObjects.Actors.Stats.PlayerStats;
import GameObjects.BodyFeatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player extends Actor<PlayerType> {
    public int level;
    public float XP;

    private PlayerStats stats;

    List<Weapon> weaponInventory;
    private float maxHP;


    public Player(PlayerType type, AnimationHandler animationHandler, BodyFeatures bodyFeatures, float scale, PlayerStats stats) {
        super(type,animationHandler,bodyFeatures, scale);
        this.stats = stats;

        HP = stats.HP();
        maxHP = 100;
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
        if(pickup.getType() == PickupType.XP_PICKUP) {
            XP += 10;
        }
        else if(pickup.getType() == PickupType.HP_PICKUP) {
            if (HP + 10 > maxHP) {
                HP = maxHP;
            }
            else
                HP += 10;
        }
    }
}

package GameObjects.Actors.Player;

import GameObjects.Actors.Actor;
import GameObjects.Actors.DirectionState;
import GameObjects.Actors.ObjectTypes.PlayerType;
import GameObjects.Actors.Stats.PlayerStats;
import GameObjects.BodyFeatures;
import GameObjects.Weapon.Weapon;
import GameObjects.AnimationRendering.AnimationRender;

import java.util.ArrayList;
import java.util.List;

public class Player extends Actor<PlayerType> {
    public int level;
    public float XP;

    private PlayerStats stats;

    List<Weapon> weaponInventory;


    public Player(PlayerType type,AnimationRender render, BodyFeatures bodyFeatures, float scale, PlayerStats stats) {
        super(type,render,bodyFeatures, scale);
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





}

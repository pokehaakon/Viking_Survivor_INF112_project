package GameObjects.Actors.Player;

import GameObjects.Actors.Actor;
import GameObjects.Actors.ActorAction.ActorAction;
import GameObjects.Actors.ObjectTypes.PlayerType;
import GameObjects.Actors.ObjectTypes.WeaponType;
import GameObjects.Actors.Stats.PlayerStats;
import GameObjects.BodyFeatures;
//import GameObjects.Factories.WeaponFactory;
import GameObjects.Factories.WeaponItemFactory;
import GameObjects.Weapon.Weapon;
import GameObjects.Weapon.WeaponBody;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.*;

public class Player extends Actor<PlayerType> {
    public int level;
    public float XP;

    private PlayerStats stats;

    List<Weapon> inventory = new ArrayList<>();
    //WeaponFactory weaponFactory = new WeaponFactory();
    WeaponItemFactory weaponItemFactory = new WeaponItemFactory(this);



    public Player(String spritePath, BodyFeatures bodyFeatures, float scale, PlayerStats stats) {
        super(spritePath,bodyFeatures, scale);
        this.stats = stats;
        HP = stats.HP();
        speed = stats.speed();
        damage = stats.damage();
        armour = stats.armour();
        XP = stats.XP();
        level = 1;

        idle = true;
    }

    public Player() {
        idle = true;
        level = 1;

        addToInventory(WeaponType.KNIFE);
    }

    public List<WeaponBody> getProjectiles() {return weaponItemFactory.getProjectiles();}

    public void setStats(PlayerStats newStats) {
        HP = newStats.HP();
        speed = newStats.speed();
        damage = newStats.damage();
        armour = newStats.armour();
        XP = newStats.XP();
    }

    public void addToInventory(WeaponType weaponType) {
        inventory.add(weaponItemFactory.create(weaponType));
    }

    @Override
    public void doAction() {
        for(Weapon weapon : inventory) {
            weapon.attack();
        }

        super.doAction();
    }

    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {
        super.draw(batch, elapsedTime);

        for (WeaponBody w : weaponItemFactory.getProjectiles()) {
            w.draw(batch);
        }
    }




}

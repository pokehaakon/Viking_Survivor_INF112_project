package GameObjects.Actors.Player;

import GameObjects.Actors.Actor;
import GameObjects.Actors.ActorAction.ActorAction;
import GameObjects.Actors.ObjectTypes.PlayerType;
import GameObjects.Actors.ObjectTypes.WeaponType;
import GameObjects.Actors.Stats.PlayerStats;
import GameObjects.BodyFeatures;
import GameObjects.Factories.WeaponItemFactory;
import GameObjects.Weapon.Knife;
import GameObjects.Weapon.Weapon;
import GameObjects.Weapon.WeaponBody;
import TextureHandling.TextureHandler;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;
import java.util.List;

import static Tools.ListTools.compactList;

public class Player extends Actor<PlayerType> {
    public int level;
    public float XP;
    private PlayerStats stats;
    private final List<Weapon> weapons = new ArrayList<>();

    private WeaponItemFactory weaponItemFactory;
    private TextureHandler textureHandler;

    public Player(String spritePath, BodyFeatures bodyFeatures, float scale, PlayerStats stats, TextureHandler textureHandler) {
        super(spritePath, bodyFeatures, scale);
        this.stats = stats;

        HP = stats.HP();
        speed = stats.speed();
        damage = stats.damage();
        armour = stats.armour();
        XP = stats.XP();
        this.textureHandler = textureHandler;
        setUp();
    }

    public Player(TextureHandler textureHandler) {
        this.textureHandler = textureHandler;
        setUp();
    }

    private void setUp() {
        weaponItemFactory = new WeaponItemFactory(this, textureHandler);
        level = 1;
        idle = true;
    }

    public void setStats(PlayerStats newStats) {
        HP = newStats.HP();
        speed = newStats.speed();
        damage = newStats.damage();
        armour = newStats.armour();
        XP = newStats.XP();
    }

    public void cleenUp() {
        for (Weapon weapon : weapons) {
            weapon.attack();
        }

        for (Weapon weapon : weapons) {
            weapon.cleenUp();
        }

        compactList(getProjectileList());
    }

    @Override
    public void doAction(){

        for (WeaponBody body : getProjectileList()) {
            body.doAction();
        }

        super.doAction();
    }

    @Override
    public void draw(SpriteBatch batch, float delta) {
        super.draw(batch, delta);

        for(WeaponBody b : getProjectileList()) {
            if(b == null) continue;
            b.draw(batch);
        }
    }

    /**
     * Gives the list of all projectiles belonging to this player, this list is always the same!
     * @return List of projectiles
     */
    public List<WeaponBody> getProjectileList() {return weaponItemFactory.getProjectiles();}

    public void addWeapon(WeaponType weaponType) {
        System.out.println("Added weapon: " + weaponType);
        weapons.add(weaponItemFactory.create(weaponType));
    }
}

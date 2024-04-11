package GameObjects.Factories;

import GameObjects.Actors.ObjectTypes.WeaponType;
import GameObjects.Actors.Player.Player;
import GameObjects.Weapon.Knife;
import GameObjects.Weapon.Weapon;
import GameObjects.Weapon.WeaponBody;
import TextureHandling.TextureHandler;

import java.util.ArrayList;
import java.util.List;

public class WeaponItemFactory {
    private Player player;
    private List<WeaponBody> projectiles;
    //WeaponFactory factory;
    private TextureHandler textureHandler;

    public WeaponItemFactory(Player player, TextureHandler textureHandler) {
        this.player = player;
        this.textureHandler = textureHandler;
        projectiles = new ArrayList<>();
        //factory = new WeaponFactory();
    }

    public List<WeaponBody> getProjectiles() {return projectiles;}
    //public WeaponFactory getWeapondFactory() {return factory;}

    public Weapon create(WeaponType type) {
        return switch (type) {
            case PROJECTILE -> null;
            case KNIFE -> new Knife(player, projectiles, textureHandler);
        };
    }
}

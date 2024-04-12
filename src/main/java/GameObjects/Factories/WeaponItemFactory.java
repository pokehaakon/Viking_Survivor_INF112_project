//package GameObjects.Factories;
//
//import GameObjects.Actors.ObjectTypes.WeaponType;
//import GameObjects.Actors.Player;
//import GameObjects.Weapon.Knife;
//import GameObjects.Actors.Weapon;
//import GameObjects.Weapon.WeaponBody;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class WeaponItemFactory {
//    Player player;
//    List<WeaponBody> projectiles;
//    //WeaponFactory factory;
//
//    public WeaponItemFactory(Player player) {
//        this.player = player;
//        projectiles = new ArrayList<>();
//        //factory = new WeaponFactory();
//    }
//
//    public List<WeaponBody> getProjectiles() {return projectiles;}
//    //public WeaponFactory getWeapondFactory() {return factory;}
//
//    public Weapon create(WeaponType type) {
//        return switch (type) {
//            case PROJECTILE -> null;
//            case KNIFE -> new Knife(player, projectiles);
//        };
//    }
//}

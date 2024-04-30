package GameObjects.Actors.ObjectActions;

//import GameObjects.Actors.Enemy;
import GameObjects.Actors.Pickups;
//import GameObjects.Actors.Player;

public class PickupActions {

    public static Action<Pickups> giveHP(Player player, float hp) {
        return (p) ->{
            if (player.HP + hp > player.maxHP) {
                player.HP = player.maxHP;
            } else
                player.HP += hp;
        };
    }


    public static Action<Pickups> giveXP(Player player, float xp) {
        return (p) ->{
            player.XP += xp;
        };
    }

}

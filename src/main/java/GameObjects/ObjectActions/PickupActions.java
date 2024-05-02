package GameObjects.ObjectActions;

//import GameObjects.Actors.Enemy;
//import GameObjects.Actors.Player;

import GameObjects.Actor;
import Simulation.Simulation;


public class PickupActions {




    public static Action giveHP(Actor player, float hp) {
        return p ->{
            float newHP = Math.min(player.getHP() + hp, 100);
            player.setHP(newHP);
        };
    }




    /**
     * Change the weapon speed. After the duration is up, the speed is reset to its default
     * @param duration the duration of the action, in milliseconds
     * @return a pickup action
     */
    public static Action setWeaponSpeed(long duration, float speedMultiplier) {
        return (o) -> {
            WeaponActions.setSpeed(duration, speedMultiplier);
        };
    }




    public static Action giveXP(int xp) {
        return p -> Simulation.EXP.addAndGet(xp);
    }

}

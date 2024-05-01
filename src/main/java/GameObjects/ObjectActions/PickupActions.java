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
     * Change the orbit action for a set duration. After the duration is up, the action is reset to its default
     * @param duration the duration of the action, in milliseconds




     * @return a pickup action
     */
    public static Action setOrbitSpeed(long duration, float newSpeed) {
        return (o) -> {
            WeaponActions.setOrbitSpeed(newSpeed, duration);
        };

    }


    public static Action giveXP(int xp) {
        return p -> Simulation.EXP.addAndGet(xp);
    }

}

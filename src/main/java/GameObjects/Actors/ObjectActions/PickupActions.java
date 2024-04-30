package GameObjects.Actors.ObjectActions;

//import GameObjects.Actors.Enemy;
//import GameObjects.Actors.Player;

import GameObjects.Actors.Actor;
import Simulation.Simulation;

public class PickupActions {

    public static Action giveHP(Actor player, float hp) {
        return p ->{
            float newHP = Math.min(player.getHP() + hp, 100);
            player.setHP(newHP);
        };
    }


    public static Action giveXP(int xp) {
        return p -> Simulation.EXP.addAndGet(xp);
    }

}

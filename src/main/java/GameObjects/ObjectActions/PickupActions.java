package GameObjects.ObjectActions;


import GameObjects.Actor;
import Simulation.Simulation;
import Tools.FilterTool;

import java.util.List;

import static Tools.FilterTool.isInCategory;


public class PickupActions {


    public static Action giveHP(Actor player, float hp, float maxHP) {
        return p ->{
            float newHP = Math.min(player.getHP() + hp, maxHP);
            player.setHP(newHP);
        };
    }

    public static Action startTemporaryActionChange(FilterTool.Category category,int frameDuration, List<Actor> actors, Action... actions) {
        return (pickup) -> {
            for(Actor actor: actors) {
                if(isInCategory(actor.getBody(), category)) {
                    actor.setTemporaryActionChange(frameDuration,actions);

                }
            }
        };
    }


    public static Action giveXP(int xp) {
        return p -> Simulation.EXP.addAndGet(xp);
    }

}

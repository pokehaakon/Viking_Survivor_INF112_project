package GameObjects.ObjectActions;

//import GameObjects.Actors.Enemy;
//import GameObjects.Actors.Player;

import GameObjects.Actor;
import Simulation.Simulation;
import Tools.FilterTool;

import java.util.List;
import java.util.logging.Filter;

import static Simulation.ObjectContactListener.isInCategory;


public class PickupActions {




    public static Action giveHP(Actor player, float hp) {
        return p ->{
            float newHP = Math.min(player.getHP() + hp, 100);
            player.setHP(newHP);
        };
    }



    public static Action startTemporaryActionChange(FilterTool.Category category,long duration, List<Actor> actors, Action... actions) {
        return (pickup) -> {
            for(Actor actor: actors) {
                if(isInCategory(actor.getBody(), category)) {
                    actor.setTemporaryActionChange(duration,actions);

                }
            }
        };
    }

    public static Action changeAction(List<Actor> actors, FilterTool.Category category,Action... actions) {
        return (pickup) -> {
            for(Actor actor : actors) {
                if(isInCategory(actor.getBody(),category)) {
                    actor.resetActions();
                    actor.addAction(actions);
                }
            }
        };
    }




    public static Action giveXP(int xp) {
        return p -> Simulation.EXP.addAndGet(xp);
    }

}

package GameObjects.ObjectActions;

import GameObjects.Actor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public abstract class CoolDownActions {

    static private Action doIfInCoolDown(Action action) {
        return (a) -> {
            if (a.isInCoolDown()) {
                action.act(a);
            }
        };
    }

    public static Action changeColorInCoolDown(Color color) {
        return (a) -> {
            if(!a.isInCoolDown()) return;


            a.getAnimationHandler().setDrawColor(color);

        };
    }

}

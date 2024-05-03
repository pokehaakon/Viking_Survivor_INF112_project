package GameObjects.ObjectActions;

import Tools.ExcludeFromGeneratedCoverage;

@ExcludeFromGeneratedCoverage(reason = "not used")
public abstract class CoolDownActions {

    static private Action doIfInCoolDown(Action action) {
        return (a) -> {
            if (a.isInCoolDown()) {
                action.act(a);
            }
        };
    }

}

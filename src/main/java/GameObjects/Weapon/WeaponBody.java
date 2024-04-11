package GameObjects.Weapon;

import GameObjects.Actors.Actor;
import GameObjects.Actors.ObjectTypes.WeaponType;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;

public class WeaponBody extends Actor<WeaponType> {

    public WeaponBody(String spritePath, BodyFeatures bodyFeatures, float scale) {
        super(spritePath, bodyFeatures, scale);
    }

    public WeaponBody() {}
}

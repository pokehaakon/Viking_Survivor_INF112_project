package GameObjects.Weapon;

import GameObjects.Actors.ObjectTypes.WeaponType;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;

public class WeaponBody extends GameObject<WeaponType> {

    public WeaponBody(String spritePath, BodyFeatures bodyFeatures, float scale) {
        super(spritePath,bodyFeatures,scale);
    }

    public WeaponBody() {}
}

package GameObjects.Weapon;

import GameObjects.Actors.Actor;
import GameObjects.Actors.ObjectTypes.WeaponType;
import GameObjects.BodyFeatures;
import Rendering.AnimationRender;

public class Weapon extends Actor<WeaponType>  {

    public Weapon(WeaponType type, AnimationRender render, BodyFeatures bodyFeatures, float scale) {
        super(type,render,bodyFeatures,scale);
    }
}

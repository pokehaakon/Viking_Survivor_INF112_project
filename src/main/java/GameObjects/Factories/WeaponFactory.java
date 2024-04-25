package GameObjects.Factories;


import GameObjects.Animations.AnimationRendering.*;
import GameObjects.Animations.AnimationState;
import GameObjects.ObjectTypes.WeaponType;
import GameObjects.BodyFeatures;
import GameObjects.Actors.Weapon;

import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Shape;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import static GameObjects.Animations.AnimationRendering.GIFS.*;
import static Tools.FilterTool.Category.*;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;

public class WeaponFactory extends Factory<Weapon,WeaponType> {

    private static final Filter DEFAULT_WEAPON_FILTER = createFilter(
            BULLET,
            new FilterTool.Category[]{FilterTool.Category.WALL, ENEMY}
    );

    public WeaponFactory() {

        register( () -> new Weapon(
                WeaponType.KNIFE,
                new AnimationHandler(Map.of(AnimationState.MOVING, KNIFE_FILE_PATH), AnimationType.GIF,AnimationState.MOVING),
                defaultWeaponBodyFeatures(createCircleShape(0.2f * 0.7f * KNIFE_WIDT / 2)),
                0.7f

        ));
        register( () -> new Weapon(
                WeaponType.AXE,
                new AnimationHandler(Map.of(AnimationState.MOVING, AXE_FILE_PATH), AnimationType.GIF,AnimationState.MOVING),
                defaultWeaponBodyFeatures(createCircleShape(0.2f * 0.7f * AXE_WIDTH / 2)),
                0.2f

        ));
    }

    private BodyFeatures defaultWeaponBodyFeatures(Shape shape) {
        return new BodyFeatures(
                shape,
                DEFAULT_WEAPON_FILTER,
                1,
                0,
                0,
                true,
                BodyDef.BodyType.DynamicBody);

    }





}

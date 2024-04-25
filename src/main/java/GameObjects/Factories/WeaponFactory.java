package GameObjects.Factories;


import Rendering.Animations.AnimationRendering.AnimationHandler;
import Rendering.Animations.AnimationRendering.AnimationType;
import Rendering.Animations.AnimationState;
import GameObjects.BodyFeatures;
import GameObjects.Actors.Weapon;

import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Shape;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static Rendering.Animations.AnimationRendering.GIFS.KNIFE_FILE_PATH;
//import static Rendering.Animations.AnimationRendering.GIFS.KNIFE_WIDT;
import static Tools.FilterTool.Category.*;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;

public class WeaponFactory extends AbstractFactory<Weapon>{

    public WeaponFactory() {

    }

    @Override
    public Weapon create(String name) {
        if(name == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Weapon weapon;
        float scale;
        AnimationType animationType;
        AnimationState spawnState;

        BodyFeatures bodyFeatures;
        Map<AnimationState, String> animation =  new EnumMap<>(AnimationState.class);
        switch (name) {
            case "WeaponType:KNIFE": {
                scale = 0.7f;
                animation.put(AnimationState.MOVING, KNIFE_FILE_PATH);
                animation.put(AnimationState.IDLE, KNIFE_FILE_PATH);
                animationType = AnimationType.GIF;
                spawnState  = AnimationState.MOVING;
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid weapon type");
        }

        Filter filter = createFilter(
                WEAPON,
                new FilterTool.Category[]{ FilterTool.Category.WALL, ENEMY}
        );
        //render.setAnimations(animation);
        Shape shape = createCircleShape(scale / 2);
        bodyFeatures = new BodyFeatures(
                shape,
                filter,
                1,
                0,
                0,
                true,
                BodyDef.BodyType.DynamicBody);

        weapon = new Weapon(name, new AnimationHandler(animation, spawnState), bodyFeatures, scale);



        return weapon;
    }

    public List<Weapon> create(int n, String name) {
        return null;
    }

}

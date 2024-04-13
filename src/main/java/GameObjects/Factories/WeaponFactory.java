package GameObjects.Factories;


import GameObjects.Animations.AnimationRendering.*;
import GameObjects.Animations.AnimationState;
import GameObjects.ObjectTypes.WeaponType;
import GameObjects.BodyFeatures;
import GameObjects.Actors.Weapon;

import TextureHandling.GdxTextureHandler;
import TextureHandling.TextureHandler;
import Tools.FilterTool;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Shape;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static GameObjects.Animations.AnimationRendering.GIFS.KNIFE_FILE_PATH;
import static GameObjects.Animations.AnimationRendering.GIFS.KNIFE_WIDT;
import static Tools.FilterTool.Category.*;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;

public class WeaponFactory extends AbstractFactory<Weapon, WeaponType>{

    public WeaponFactory() {

    }
    @Override
    public Weapon create(WeaponType type) {
        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Weapon weapon;
        float scale;
        boolean isGif;

        BodyFeatures bodyFeatures;
        Map<AnimationState, String> animation =  new EnumMap<>(AnimationState.class);
        switch (type) {
            case KNIFE: {
                scale = 0.7f;
                animation.put(AnimationState.MOVING,KNIFE_FILE_PATH);
                isGif = true;
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid weapon type");
        }

        Filter filter = createFilter(
                BULLET,
                new FilterTool.Category[]{ FilterTool.Category.WALL, ENEMY}
        );
        //render.setAnimations(animation);
        Shape shape = createCircleShape(0.2f*scale*KNIFE_WIDT/2);
        bodyFeatures = new BodyFeatures(
                shape,
                filter,
                1,
                0,
                0,
                true,
                BodyDef.BodyType.DynamicBody);

        weapon = new Weapon(type, animation,bodyFeatures,scale);
        weapon.setAnimationState(AnimationState.MOVING);
        weapon.isGif = isGif;

        return weapon;
    }

    @Override
    public List<Weapon> create(int n, WeaponType type) {
        return null;
    }

}

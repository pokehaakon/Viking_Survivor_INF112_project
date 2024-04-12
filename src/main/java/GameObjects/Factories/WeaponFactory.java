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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static GameObjects.Animations.AnimationRendering.GIFS.KNIFE_FILE_PATH;
import static Tools.FilterTool.Category.*;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;

public class WeaponFactory extends AbstractFactory<Weapon, WeaponType>{

    public WeaponFactory(AnimationLoader  animationLoader) {
        spriteRender = new SpriteRender(animationLoader);
        gifRender = new GIFRender<>(animationLoader);
    }
    @Override
    public Weapon create(WeaponType type) {
        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Weapon weapon;
        float scale;


        AnimationRender render;
        BodyFeatures bodyFeatures;
        Map<AnimationState, String> animation =  new HashMap<>();
        switch (type) {
            case KNIFE: {
                render = gifRender;
                scale = 0.7f;
                animation.put(AnimationState.MOVING,KNIFE_FILE_PATH);
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid enemy type");
        }

        Filter filter = createFilter(
                BULLET,
                new FilterTool.Category[]{ FilterTool.Category.WALL, ENEMY}
        );
        render.setAnimations(animation);
        Shape shape = createCircleShape(0.2f*scale*render.getWidth(AnimationState.MOVING)/2);
        bodyFeatures = new BodyFeatures(
                shape,
                filter,
                1,
                0,
                0,
                true,
                BodyDef.BodyType.DynamicBody);

        weapon = new Weapon(type, render,bodyFeatures,scale);
        weapon.setAnimationState(AnimationState.MOVING);

        return weapon;
    }

    @Override
    public List<Weapon> create(int n, WeaponType type) {
        return null;
    }

}

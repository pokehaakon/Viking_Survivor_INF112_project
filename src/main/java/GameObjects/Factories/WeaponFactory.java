package GameObjects.Factories;


import Animations.AnimationState;
import GameObjects.Actors.ObjectTypes.WeaponType;
import GameObjects.BodyFeatures;
import GameObjects.Weapon.Weapon;

import GameObjects.AnimationRendering.GIFRender;
import GameObjects.AnimationRendering.GifPair;
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

import static Animations.AnimationConstants.getGIF;
import static Tools.FilterTool.Category.*;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;

public class WeaponFactory implements IFactory<Weapon, WeaponType>{
    private TextureHandler textureHandler;
    public WeaponFactory() {
        textureHandler = new GdxTextureHandler();
    }
    @Override
    public Weapon create(WeaponType type) {
        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Weapon weapon;
        float scale;
        Shape shape;
        Texture texture;

        BodyFeatures bodyFeatures;
        Map<AnimationState, GifPair> animation =  new HashMap<>();
        switch (type) {
            case KNIFE: {
                scale = 0.7f;
                texture = textureHandler.loadTexture("wolf.png");
                shape = createCircleShape(texture.getWidth()*scale/2);
                animation.put(AnimationState.MOVING,new GifPair(getGIF("knife.gif"),getGIF("knife.gif")));
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid enemy type");
        }

        Filter filter = createFilter(
                BULLET,
                new FilterTool.Category[]{ FilterTool.Category.WALL, ENEMY}
        );
        bodyFeatures = new BodyFeatures(
                shape,
                filter,
                1,
                0,
                0,
                true,
                BodyDef.BodyType.DynamicBody);

        weapon = new Weapon(type, new GIFRender<>(animation),bodyFeatures,scale);
        weapon.setAnimationState(AnimationState.MOVING);

        return weapon;
    }

    @Override
    public List<Weapon> create(int n, WeaponType type) {
        return null;
    }

    @Override
    public void setTextureHandler(TextureHandler newTextureHandler) {
        textureHandler = newTextureHandler;
    }
}

package GameObjects.Factories;

import GameObjects.Animations.AnimationRendering.*;
import GameObjects.Animations.AnimationState;
import GameObjects.ObjectTypes.TerrainType;
import GameObjects.BodyFeatures;
import GameObjects.ObjectTypes.WeaponType;
import GameObjects.StaticObjects.Terrain;
import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.*;

import java.util.*;
import java.util.function.Supplier;


import static GameObjects.Animations.AnimationRendering.Sprites.TREE_WIDTH;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;

public class TerrainFactory extends Factory<Terrain, TerrainType>{

    public TerrainFactory() {
        registerAll(TerrainType.values());
    }


    @Override
    public Supplier<Terrain> build(TerrainType type) {

        return () -> {
            Terrain terrain;
            float scale;
            BodyFeatures bodyFeatures;

            Map<AnimationState, String> animations = new EnumMap<>(AnimationState.class);
            Map<AnimationState, String> gifAnimations = new EnumMap<>(AnimationState.class);

            AnimationRender render;
            Shape shape;

            AnimationState spawnState;
            AnimationType animationType;


            switch (type) {
                case TREE: {
                    scale = 0.1f;
                    animations.put(AnimationState.STATIC, "tree.png");
                    shape = createCircleShape(scale * TREE_WIDTH / 2);
                    animationType = AnimationType.SPRITE;
                    spawnState = AnimationState.STATIC;
                    break;
                }

                default:
                    throw new IllegalArgumentException("Invalid terrain type");
            }
            Filter filter = createFilter(
                    FilterTool.Category.WALL,
                    new FilterTool.Category[]{
                            //FilterTool.Category.WALL,
                            FilterTool.Category.ENEMY,
                            FilterTool.Category.PLAYER,
                            FilterTool.Category.BULLET
                    }
            );


            bodyFeatures = new BodyFeatures(
                    shape,
                    filter,
                    1,
                    1,
                    0,
                    false,
                    BodyDef.BodyType.StaticBody);

            terrain = new Terrain(type, new AnimationHandler(animations, animationType, spawnState), bodyFeatures, scale);

            return terrain;
        };

    }


}

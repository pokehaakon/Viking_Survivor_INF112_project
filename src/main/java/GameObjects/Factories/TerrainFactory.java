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

    private static final Filter DEFAULT_TERRAIN_FILTER = createFilter(
            FilterTool.Category.WALL,
            new FilterTool.Category[]{
                    //FilterTool.Category.WALL,
                    FilterTool.Category.ENEMY,
                    FilterTool.Category.PLAYER,
                    FilterTool.Category.BULLET
            }
    );

    public TerrainFactory() {
        register( () -> {
            return new Terrain(
                    TerrainType.TREE,
                    new AnimationHandler(Map.of(AnimationState.STATIC, "tree.png"), AnimationType.SPRITE,AnimationState.STATIC),
                    defaultTerrainBodyFeatures(createCircleShape(0.1f * TREE_WIDTH / 2)),
                    0.1f
            );
        });
    }

    private BodyFeatures defaultTerrainBodyFeatures(Shape shape) {
        return new BodyFeatures(
                shape,
                DEFAULT_TERRAIN_FILTER,
                1,
                1,
                0,
                false,
                BodyDef.BodyType.StaticBody);

    }




}

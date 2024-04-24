package GameObjects.Factories;

import Rendering.Animations.AnimationRendering.AnimationHandler;
import Rendering.Animations.AnimationRendering.AnimationRender;
import Rendering.Animations.AnimationRendering.AnimationType;
import Rendering.Animations.AnimationState;
import GameObjects.ObjectTypes.TerrainType;
import GameObjects.BodyFeatures;
import GameObjects.StaticObjects.Terrain;
import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.*;

import java.util.*;


import static Rendering.Animations.AnimationRendering.Sprites.TREE_WIDTH;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;

public class TerrainFactory extends AbstractFactory<Terrain>{



    public TerrainFactory() {
        super();
    }

    @Override
    public Terrain create(String name) {
        if(name == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Terrain terrain;
        float scale;
        BodyFeatures bodyFeatures;

        Map<AnimationState, String> animations = new EnumMap<>(AnimationState.class);
        Map<AnimationState, String> gifAnimations = new EnumMap<>(AnimationState.class);

        AnimationRender render;
        Shape shape;

        AnimationState spawnState;
        AnimationType animationType;

        boolean isGif;
        switch (name) {
            case "TerrainType:TREE": {
                scale = 0.1f;
                animations.put(AnimationState.STATIC,"tree.png");
                shape = createCircleShape(scale*TREE_WIDTH/2);
                animationType = AnimationType.SPRITE;
                spawnState = AnimationState.STATIC;
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid terrain type: " + name);
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

        terrain = new Terrain(name, new AnimationHandler(animations,animationType,spawnState),bodyFeatures,scale);

        return terrain;
    }


    public List<Terrain> create(int n, String name) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of enemies must be greater than zero");
        }

        List<Terrain> terrain = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            terrain.add(create(name));
        }
        return terrain;
    }

}

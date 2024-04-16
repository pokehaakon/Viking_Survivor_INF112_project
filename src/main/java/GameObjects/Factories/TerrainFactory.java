package GameObjects.Factories;

import GameObjects.Animations.AnimationRendering.*;
import GameObjects.Animations.AnimationState;
import GameObjects.ObjectTypes.TerrainType;
import GameObjects.BodyFeatures;
import GameObjects.StaticObjects.Terrain;
import TextureHandling.GdxTextureHandler;
import TextureHandling.TextureHandler;
import Tools.FilterTool;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

import java.util.*;


import static GameObjects.Animations.AnimationRendering.GIFS.*;
import static GameObjects.Animations.AnimationRendering.Sprites.TREE_WIDTH;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;
import static Tools.ShapeTools.createSquareShape;

public class TerrainFactory extends AbstractFactory<Terrain, TerrainType>{



    public TerrainFactory() {
        super();


    }
    @Override
    public Terrain create(TerrainType type) {
        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Terrain terrain;
        float scale;
        BodyFeatures bodyFeatures;

        Map<AnimationState, String> animations = new EnumMap<>(AnimationState.class);
        Map<AnimationState, String> gifAnimations = new EnumMap<>(AnimationState.class);

        Shape shape;

        AnimationState spawnState;
        AnimationType animationType;

        boolean isGif;

        switch (type) {
            case TREE: {
                scale = 0.1f;
                animations.put(AnimationState.STATIC,"tree.png");
                shape = createCircleShape(scale*TREE_WIDTH/2);
                animationType = AnimationType.SPRITE;
                spawnState = AnimationState.STATIC;
                break;
            }
            case PICKUPORB:
                scale = 0.5f;
                animations.put(AnimationState.STATIC, PICK_UP_ORB_FILE_PATH);
                shape = createCircleShape(0.2f*scale*PICKUP_ORB_WIDTH/2);
                animationType = AnimationType.GIF;
                spawnState = AnimationState.STATIC;
                break;
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

        terrain = new Terrain(type,new ObjectAnimations(animations,animationType,spawnState),bodyFeatures,scale);

        return terrain;
    }

    @Override
    public List<Terrain> create(int n, TerrainType type) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of enemies must be greater than zero");
        }

        List<Terrain> terrain = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            terrain.add(create(type));
        }
        return terrain;
    }

}

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static GameObjects.Animations.AnimationRendering.GIFS.PICK_UP_ORB_FILE_PATH;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;
import static Tools.ShapeTools.createSquareShape;

public class TerrainFactory extends AbstractFactory<Terrain, TerrainType>{



    public TerrainFactory(AnimationLoader animationLoader) {
        super();
        gifRender = new GIFRender<>(animationLoader);
        spriteRender = new SpriteRender(animationLoader);
        //default

    }
    @Override
    public Terrain create(TerrainType type) {
        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Terrain terrain;
        float scale;
        BodyFeatures bodyFeatures;

        Map<AnimationState, String> spriteAnimations = new HashMap<>();
        Map<AnimationState, String> gifAnimations = new HashMap<>();

        AnimationRender render;

        switch (type) {
            case TREE: {
                //texture = textureHandler.loadTexture("tree.png");
                scale = 0.1f;
                spriteAnimations.put(AnimationState.STATIC,"tree.png");
                render = spriteRender;
                render.setAnimations(spriteAnimations);
                break;
            }
            case PICKUPORB:
                scale = 0.5f;
                gifAnimations.put(AnimationState.STATIC, PICK_UP_ORB_FILE_PATH);
                render = gifRender;
                gifRender.setAnimations(gifAnimations);
                break;
            default:
                throw new IllegalArgumentException("Invalid enemy type");
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
        Shape shape = createCircleShape(scale*render.getWidth(AnimationState.STATIC)/2);

        bodyFeatures = new BodyFeatures(
                shape,
                filter,
                1,
                1,
                0,
                false,
                BodyDef.BodyType.StaticBody);

        terrain = new Terrain(type,render,bodyFeatures,scale);
        terrain.setAnimationState(AnimationState.STATIC);

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

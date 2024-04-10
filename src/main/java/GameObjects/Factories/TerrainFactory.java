package GameObjects.Factories;

import Animations.AnimationState;
import GameObjects.Actors.ObjectTypes.TerrainType;
import GameObjects.BodyFeatures;
import GameObjects.Terrain.Terrain;
import Rendering.AnimationRender;
import Rendering.SpriteRender;
import TextureHandling.GdxTextureHandler;
import TextureHandling.TextureHandler;
import Tools.FilterTool;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Tools.BodyTool.*;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createSquareShape;

public class TerrainFactory implements IFactory<Terrain, TerrainType>{

    private TextureHandler textureHandler;

    public TerrainFactory() {

        //default
        textureHandler = new GdxTextureHandler();
    }
    @Override
    public Terrain create(TerrainType type) {
        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Terrain terrain;
        float scale;
        Texture texture;
        Shape shape;
        BodyFeatures bodyFeatures;

        Map<AnimationState, Sprite> animations = new HashMap<>();

        AnimationRender render;

        switch (type) {
            case TREE: {
                texture = textureHandler.loadTexture("img_2.png");
                scale = 0.6f;
                shape = createSquareShape(
                        (float)(texture.getWidth())*scale,
                        (float) (texture.getHeight()*scale)
                );
                Sprite sprite = new Sprite(texture);
                animations.put(AnimationState.STATIC,sprite);
                render = new SpriteRender(animations);
                break;
            }

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

    @Override
    public void setTextureHandler(TextureHandler newTextureHandler) {
        textureHandler =  newTextureHandler;

    }
}

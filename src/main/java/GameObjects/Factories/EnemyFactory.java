package GameObjects.Factories;


import Animations.AnimationState;
import GameObjects.Actors.ObjectTypes.EnemyType;
import GameObjects.Actors.Stats.EnemyStats;
import GameObjects.Actors.Stats.Stats;
import Animations.AnimationConstants;
import GameObjects.Actors.Enemy.Enemy;
import GameObjects.BodyFeatures;
import GameObjects.AnimationRendering.AnimationRender;
import GameObjects.AnimationRendering.GIFRender;
import GameObjects.AnimationRendering.GifPair;
import TextureHandling.GdxTextureHandler;
import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.*;

import java.util.HashMap;
import java.util.Map;

import static GameObjects.AnimationRendering.GIFS.ORC_GIF;
import static GameObjects.AnimationRendering.GIFS.RAVEN_GIF;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;


public class EnemyFactory extends AbstractFactory<Enemy, EnemyType> {

    private final Filter filter;


    public EnemyFactory() {
        super();
        // default
        textureHandler = new GdxTextureHandler();
        filter  = createFilter(
                FilterTool.Category.ENEMY,
                new FilterTool.Category[]{
                        FilterTool.Category.WALL,
                        FilterTool.Category.ENEMY,
                        FilterTool.Category.PLAYER,
                        FilterTool.Category.BULLET
                }
        );

    }

    /**
     * Creates an instance of an enemy
     * @param type the desired enemy type
     * @return an enemy object
     */
    @Override
    public Enemy create(EnemyType type) {

        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Enemy enemy;
        float scale;
        EnemyStats stats;
        Map<AnimationState,GifPair> gifs = new HashMap<>();

        switch (type) {
            case RAVEN: {
                scale = AnimationConstants.ENEMY1_SCALE;
                stats = Stats.enemy1();
                gifs.put(AnimationState.MOVING, RAVEN_GIF);

                break;
            }
            case ORC: {
                scale = AnimationConstants.ENEMY2_SCALE;
                stats = Stats.enemy2();
                gifs.put(AnimationState.MOVING, ORC_GIF);
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid enemy type");
        }

        AnimationRender render = new GIFRender<>(gifs);
        Shape shape = createCircleShape(scale*render.getWidth(AnimationState.MOVING)/2);
        BodyFeatures bodyFeatures = new BodyFeatures(
                shape,
                filter,
                1,
                0,
                0,
                false,
                BodyDef.BodyType.DynamicBody);



        enemy = new Enemy(type,new GIFRender<>(gifs),bodyFeatures,scale,stats);
        enemy.setAnimationState(AnimationState.MOVING);
        return enemy;
    }

}


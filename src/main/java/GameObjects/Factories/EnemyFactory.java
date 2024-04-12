package GameObjects.Factories;


import GameObjects.Animations.AnimationRendering.*;
import GameObjects.Animations.AnimationState;
import GameObjects.ObjectTypes.EnemyType;
import GameObjects.Actors.Stats.EnemyStats;
import GameObjects.Actors.Stats.Stats;
import GameObjects.Actors.Enemy;
import GameObjects.BodyFeatures;
import TextureHandling.GdxTextureHandler;
import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.*;

import java.util.HashMap;
import java.util.Map;

import static GameObjects.Animations.AnimationRendering.GIFS.*;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;


public class EnemyFactory extends AbstractFactory<Enemy, EnemyType> {

    private final Filter filter;



    public EnemyFactory(AnimationLoader animationLoader) {
        super();
        gifRender = new GIFRender(animationLoader);
        // default

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
        Map<AnimationState,String> gifs = new HashMap<>();

        switch (type) {
            case RAVEN: {
                scale = RAVEN_SCALE;
                stats = Stats.enemy1();
                gifs.put(AnimationState.MOVING, RAVEN_FILE_PATH);

                break;
            }
            case ORC: {
                scale = ORC_SCALE;
                stats = Stats.enemy2();
                gifs.put(AnimationState.MOVING, ORC_FILE_PATH);
                break;
            }
            case WOLF: {
                scale = ORC_SCALE;
                stats = Stats.enemy2();
                gifs.put(AnimationState.MOVING, WOLF_FILE_PATH);
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid enemy type");
        }

        gifRender.setAnimations(gifs);
        Shape shape = createCircleShape(scale*gifRender.getWidth(AnimationState.MOVING)/2);
        BodyFeatures bodyFeatures = new BodyFeatures(
                shape,
                filter,
                1,
                0,
                0,
                false,
                BodyDef.BodyType.DynamicBody);

        enemy = new Enemy(type,gifRender,bodyFeatures,scale,stats);
        enemy.setAnimationState(AnimationState.MOVING);
        System.out.println(enemy.getType());
        return enemy;
    }

}


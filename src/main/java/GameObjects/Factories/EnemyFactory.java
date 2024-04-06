package GameObjects.Factories;


import GameObjects.Actors.ObjectTypes.EnemyType;
import GameObjects.Actors.Stats.EnemyStats;
import GameObjects.Actors.Stats.Stats;
import Animations.ActorAnimation;
import Animations.ActorAnimations;
import Animations.AnimationConstants;
import GameObjects.Actors.Enemy.Enemy;
import GameObjects.BodyFeatures;
import TextureHandling.GdxTextureHandler;
import TextureHandling.TextureHandler;
import Tools.FilterTool;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;

import java.util.*;

import static Animations.AnimationConstants.PLAYER_IDLE_RIGHT;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createSquareShape;


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
                        FilterTool.Category.PLAYER
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
        Shape shape;
        Texture texture;
        ActorAnimation animation;
        EnemyStats stats;


        switch (type) {
            case ENEMY1: {
                scale = AnimationConstants.ENEMY1_SCALE;
                texture = textureHandler.loadTexture(PLAYER_IDLE_RIGHT);
                shape = createSquareShape(
                        (float)(texture.getWidth())*scale,
                        (float) (texture.getHeight()*scale)

                );
                animation = ActorAnimations.enemyMoveAnimation();
                stats = Stats.enemy1();
                break;
            }
            case ENEMY2: {
                scale = AnimationConstants.ENEMY2_SCALE;
                texture = textureHandler.loadTexture(PLAYER_IDLE_RIGHT);
                shape = createSquareShape(
                        texture.getWidth()*scale,
                        texture.getHeight()*scale);

                animation = ActorAnimations.enemyMoveAnimation();
                stats = Stats.enemy2();
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid enemy type");
        }


        BodyFeatures bodyFeatures = new BodyFeatures(
                shape,
                filter,
                1,
                0,
                0,
                false,
                BodyDef.BodyType.DynamicBody);

        enemy = new Enemy();
        enemy.setBodyFeatures(bodyFeatures);
        enemy.setScale(scale);
        enemy.setSprite(texture);
        enemy.setAnimation(animation);
        enemy.setType(type);
        enemy.setStats(stats);

        return enemy;
    }

}


package GameObjects.Factories;


import Animations.MovementState;
import GameObjects.Actors.ObjectTypes.EnemyType;
import GameObjects.Actors.Stats.EnemyStats;
import GameObjects.Actors.Stats.Stats;
import Animations.ActorMovement;
import Animations.ActorAnimations;
import Animations.AnimationConstants;
import GameObjects.Actors.Enemy.Enemy;
import GameObjects.BodyFeatures;
import TextureHandling.GdxTextureHandler;
import Tools.FilterTool;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;

import java.util.HashMap;
import java.util.Map;

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
        EnemyStats stats;

        Map<MovementState,String> gifs = new HashMap<>();

        switch (type) {
            case RAVEN: {
                scale = AnimationConstants.ENEMY1_SCALE;
                texture = textureHandler.loadTexture("raven.gif");
                shape = createSquareShape(
                        (float)(texture.getWidth())*scale,
                        (float) (texture.getHeight()*scale)

                );
                stats = Stats.enemy1();
                gifs.put(MovementState.WALKING,"raven.gif");
                break;
            }
            case ORC: {
                scale = AnimationConstants.ENEMY2_SCALE;
                texture = textureHandler.loadTexture("orc.gif");
                shape = createSquareShape(
                        texture.getWidth()*scale,
                        texture.getHeight()*scale);

                stats = Stats.enemy2();
                gifs.put(MovementState.WALKING, "orc.gif");
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
        enemy.setType(type);
        enemy.setAnimationState(MovementState.WALKING);
        enemy.setStats(stats);
        enemy.setAnimations(gifs);

        return enemy;
    }

}


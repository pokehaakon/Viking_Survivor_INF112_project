package GameObjects.Factories;


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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.w3c.dom.Text;

import java.util.*;

import static Animations.AnimationConstants.PLAYER_IDLE_RIGHT;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createSquareShape;


public class EnemyFactory implements IFactory<Enemy>{

    TextureHandler textureHandler;

    public EnemyFactory() {

        // default
        textureHandler = new GdxTextureHandler();
    }

    /**
     * Creates an instance of an enemy
     * @param type the desired enemy type
     * @return an enemy object
     */
    @Override
    public Enemy create(String type) {

        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Enemy enemy;
        float scale;
        Shape shape;
        Texture texture;
        ActorAnimation animation;
        EnemyStats stats;


        switch (type.toUpperCase()) {
            case "ENEMY1": {
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
            case "ENEMY2": {
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

        Filter filter = createFilter(
                FilterTool.Category.ENEMY,
                new FilterTool.Category[]{
                        FilterTool.Category.WALL,
                        FilterTool.Category.ENEMY,
                        FilterTool.Category.PLAYER
                }
        );
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
        enemy.setType(type.toUpperCase());
        enemy.setStats(stats);

        return enemy;
    }


    /**
     * Create multiple enemies
     * @param n number of enemies to create
     * @param type the desired enemy type
     * @return a list of Enemy objects
     */
    @Override
    public List<Enemy> create(int n, String type) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of enemies must be greater than zero");
        }

        List<Enemy> enemies = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            Enemy newEnemy = create(type);
            enemies.add(newEnemy);
        }
        return enemies;
    }

    @Override
    public void setTextureHandler(TextureHandler newTextureHandler) {

        textureHandler = newTextureHandler;
    }




}


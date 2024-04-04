package GameObjects.Factories;


import GameObjects.Actors.Stats.EnemyStats;
import GameObjects.Actors.Stats.Stats;
import Animations.ActorAnimation;
import Animations.ActorAnimations;
import Animations.AnimationConstants;
import FileHandling.FileHandler;
import FileHandling.GdxFileHandler;
import GameObjects.Actors.Enemy.Enemy;
import GameObjects.GameObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.*;

import static Tools.BodyTool.createBody;
import static Tools.BodyTool.createEnemyBody;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createSquareShape;


public class EnemyFactory implements IFactory<Enemy>{

    FileHandler fileHandler;

    private final World world;
    public EnemyFactory(World world) {
        this.world = world;

        // default
        fileHandler = new GdxFileHandler();
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
        String spawnGIF;
        Texture texture;
        ActorAnimation animation;
        EnemyStats stats;

        switch (type.toUpperCase()) {
            case "ENEMY1": {
                scale = AnimationConstants.ENEMY1_SCALE;
                spawnGIF = AnimationConstants.PLAYER_IDLE_RIGHT;
                texture = fileHandler.loadTexture(spawnGIF);
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
                spawnGIF = AnimationConstants.PLAYER_IDLE_RIGHT;
                texture = fileHandler.loadTexture(spawnGIF);

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

        enemy = new Enemy(createEnemyBody(world, new Vector2(),shape),spawnGIF, scale, stats);
        enemy.setNewAnimationGIF(spawnGIF);
        // setting animations
        enemy.setAnimation(animation);

        enemy.setType(type);


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
    public void setFileHandler(FileHandler newFileHandler) {

        fileHandler = newFileHandler;
    }




}


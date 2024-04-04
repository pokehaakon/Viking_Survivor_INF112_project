package Actors.Enemy;


import Actors.Stats.Stats;
import Animations.ActorAnimation;
import Animations.ActorAnimations;
import Animations.AnimationConstants;
import FileHandling.FileHandler;
import FileHandling.GdxFileHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;

import java.util.*;

import static Tools.BodyTool.createBody;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createSquareShape;


public class EnemyFactory{

    FileHandler fileHandler;


    public EnemyFactory() {
        fileHandler = new GdxFileHandler();
    }
    /**
     * Creates an instance of an enemy
     * @param enemyType the desired enemy type
     * @return an enemy object
     */
    public Enemy create(String enemyType) {

        if(enemyType == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Enemy enemy;
        float scale;
        Shape shape;
        String spawnGIF;
        Texture texture;
        ActorAnimation animation;


        switch (enemyType.toUpperCase()) {
            case "ENEMY1": {
                scale = AnimationConstants.ENEMY1_SCALE;
                spawnGIF = AnimationConstants.PLAYER_IDLE_RIGHT;
                texture = fileHandler.loadTexture(spawnGIF);
                shape = createSquareShape(
                        (float)(texture.getWidth())*scale,
                        (float) (texture.getHeight()*scale)

                );
                animation = ActorAnimations.enemyMoveAnimation();
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
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid enemy type");
        }

        enemy = new Enemy(spawnGIF, scale, Stats.enemy1());
        enemy.currentSprite = texture;
        // setting animations
        enemy.setAnimation(animation);

        enemy.setEnemyType(enemyType);
        enemy.shape = shape;

        return enemy;
    }


    /**
     * Create multiple enemies
     * @param n number of enemies to create
     * @param enemyType the desired enemy type
     * @return a list of Enemy objects
     */
    public List<Enemy> create(int n, String enemyType) {

        if(n <= 0) {
            throw new IllegalArgumentException("Number of enemies must be greater than zero");
        }

        List<Enemy> enemies = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            Enemy newEnemy = create(enemyType);
            enemies.add(newEnemy);
        }
        return enemies;
    }

    public void setFileHandler(FileHandler newFileHandler) {
        fileHandler = newFileHandler;
    }




}


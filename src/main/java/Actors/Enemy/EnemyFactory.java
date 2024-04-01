package Actors.Enemy;


import Actors.Stats.Stats;
import Animations.ActorAnimation;
import Animations.ActorAnimations;
import Animations.AnimationConstants;
import Tools.FilterTool;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.*;

import static Tools.BodyTool.createBodies;
import static Tools.BodyTool.createBody;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createSquareShape;


public class EnemyFactory implements IEnemyFactory {


    private final World world;

    public EnemyFactory(World world) {
        this.world = world;
    }

    @Override
    public Enemy createEnemyType(String type){

        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Enemy enemy;
        float scale;
        Shape shape;
        String spawnGIF;
        Texture texture;
        ActorAnimation animation;

        switch (type.toUpperCase()) {
            case "ENEMY1": {
                scale = AnimationConstants.ENEMY1_SCALE;
                spawnGIF = AnimationConstants.PLAYER_IDLE_RIGHT;
                texture = new Texture(Gdx.files.internal(spawnGIF));
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
                texture = new Texture(Gdx.files.internal(spawnGIF));

                shape = createSquareShape(
                        texture.getWidth()*scale,
                        texture.getHeight()*scale);

                animation = ActorAnimations.enemyMoveAnimation();

                break;
            }
            default:
                throw new IllegalArgumentException("Invalid enemy type");
        }

        enemy = new Enemy(createEnemyBody(new Vector2(), shape), spawnGIF, scale, Stats.enemy1());
        // setting animations
        enemy.setAnimation(animation);
        enemy.getBody().setActive(false);

        return enemy;
    }

    @Override
    public List<Enemy> createEnemies(int count, String type) {

        List<Enemy> enemies = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Enemy newEnemy = createEnemyType(type); //TODO add scale!
            enemies.add(newEnemy);
        }
        return enemies;
    }


    public  Body createEnemyBody(Vector2 pos, Shape shape) {
        Filter enemyFilter = createFilter(
                FilterTool.Category.ENEMY,
                new FilterTool.Category[]{
                        FilterTool.Category.WALL,
                        FilterTool.Category.ENEMY,
                        FilterTool.Category.PLAYER
                }
        );

        return createBody(world, pos, shape, enemyFilter, 1, 0, 0);
    }




    private Array<Body> createEnemyBodies(int n, Iterable<Vector2> poss, Shape shape) {
        Filter enemyFilter = createFilter(
                FilterTool.Category.ENEMY,
                new FilterTool.Category[]{
                        FilterTool.Category.WALL,
                        FilterTool.Category.ENEMY,
                        FilterTool.Category.PLAYER
                }
        );
        return createBodies(n, world, poss, shape, enemyFilter, 1, 0, 0, false);
    }
}


package Actors.Enemy;


import Actors.Stats.Stats;
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


    private final static List<String> enemyTypes = Arrays.asList(
            "ENEMY1",
            "ENEMY2"
    );
    private World world;

    private static final int SWARM_SPEED_MULTIPLIER = 5;

    public EnemyFactory(World world) {
        this.world = world;
    }

    @Override
    public Enemy createEnemyType(String type, Vector2 pos){

        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Enemy enemy;
        float scale;
        Shape shape;
        Texture texture;

        switch (type.toUpperCase()) {
            case "ENEMY1": {
                scale = Sprites.ENEMY1_SCALE;
                texture = new Texture(Gdx.files.internal(Sprites.ENEMY_1_PNG));
                shape = createSquareShape(
                        texture.getWidth()*scale,
                        texture.getHeight()*scale
                );

                enemy = new Enemy(createEnemyBody(pos, shape), texture, scale, Stats.enemy1());
                shape.dispose();
                break;
            }
            case "ENEMY2": {
                scale = Sprites.ENEMY2_SCALE;
                texture = new Texture(Gdx.files.internal(Sprites.ENEMY_2_PNG));
                shape = createSquareShape(
                        texture.getWidth()*scale,
                        texture.getHeight()*scale);

                enemy = new Enemy(createEnemyBody(pos, shape), texture, scale, Stats.enemy2());
                shape.dispose();

                break;
            }
            default:
                throw new IllegalArgumentException("Invalid enemy type");
        }

        return enemy;
    }

    @Override
    public List<Enemy> createEnemies(int count, String type, List<Vector2> startPos) {

        List<Enemy> enemies = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Enemy newEnemy = createEnemyType(type, startPos.get(i)); //TODO add scale!
            enemies.add(newEnemy);
        }
        return enemies;
    }


    private  String randomEnemyType() {
        Random random = new Random();
        int randomIndex = random.nextInt(enemyTypes.size());
        return enemyTypes.get(randomIndex);
    }


    @Override
    public  List<Enemy> createRandomEnemies(int count, List<Vector2> startPoints) {
        List<Enemy> enemyList = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            Enemy enemy = createEnemyType(randomEnemyType(), startPoints.get(i)); //TODO add scale!
            enemyList.add(enemy);
        }
        return enemyList;
    }

    @Override
    public List<Enemy> createSwarm(int numMembers, String enemyType, SwarmType swarmType, List<Vector2> startPoints) {
        List<Enemy> enemies = new ArrayList<>();

        Vector2 centerPos;

        if(swarmType == SwarmType.SQUARE) {
            centerPos = startPoints.get((int)Math.ceil(Math.sqrt(startPoints.size())));

        }
        else if(swarmType == SwarmType.LINE){
            centerPos = startPoints.get(startPoints.size()/2);
        }
        else{
            throw new IllegalArgumentException("Cannot find swarm type");
        }

        for(int i = 0; i < numMembers; i++) {
            Enemy enemy = createEnemyType(enemyType, startPoints.get(i));
            enemy.setEnemyState(EnemyState.SWARM_MEMBER);
            enemy.setSwarmCenter(centerPos);
            enemy.setSpeed(SWARM_SPEED_MULTIPLIER);
            enemies.add(enemy);
        }
        return enemies;
    }

    private Body createEnemyBody(Vector2 pos, Shape shape) {
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


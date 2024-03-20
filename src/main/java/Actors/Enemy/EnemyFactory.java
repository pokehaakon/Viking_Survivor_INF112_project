package Actors.Enemy;


import Actors.Coordinates;
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
import static Tools.ShapeTools.createCircleShape;


public class EnemyFactory implements IEnemyFactory {

    private static float ENEMY1_SCALE = 0.1f;
    private static float ENEMY2_SCALE = 0.1f;


    private final static List<String> enemyTypes = Arrays.asList("ENEMY1", "ENEMY2");
    private final World world;

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
        switch (type.toUpperCase()) {
            case "ENEMY1": {
                scale = ENEMY1_SCALE;
                Shape shape = createCircleShape(scale/2);
                Texture texture = new Texture(Gdx.files.internal(Sprites.ENEMY_1_PNG));
                enemy = new Enemy(createEnemyBody(pos, shape), texture, scale, Stats.enemy1());
                shape.dispose();
                //enemy.setAction((a) -> a.getBody().setLinearVelocity(0, -30)); // <-- use this to set the actor (enemy) 'AI'
                                                            // currently this is set in the MVPContext
                break;
            }
            case "ENEMY2": {
                scale = ENEMY2_SCALE;
                Shape shape = createCircleShape(scale/2);
                Texture texture = new Texture(Gdx.files.internal(Sprites.ENEMY_2_PNG));
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
    public List<Enemy> createEnemies(int count, String type) {

        List<Enemy> enemies = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Vector2 startPoint = Coordinates.randomPoint();
            Enemy newEnemy = createEnemyType(type, startPoint); //TODO add scale!
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
    public  List<Enemy> createRandomEnemies(int count) {
        List<Enemy> enemyList = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            Vector2 startPoint = Coordinates.randomPoint();
            Enemy enemy = createEnemyType(randomEnemyType(), startPoint); //TODO add scale!
            enemyList.add(enemy);
        }
        return enemyList;
    }

//    @Override
//    public Swarm createSwarm(int numMembers, String enemyType, SwarmType swarmType) {
//        Swarm swarm = new Swarm();
//        Vector2 startPoint = Coordinates.randomPoint();
//        List<Vector2> swarmPoints;
//
//        if(swarmType == SwarmType.SQUARE) {
//            swarm.setSwarmType(SwarmType.SQUARE);
//            swarmPoints = Coordinates.squareSwarm(numMembers,startPoint, 60);
//        }
//        else if(swarmType == SwarmType.LINE){
//            swarm.setSwarmType(SwarmType.LINE);
//            swarmPoints = Coordinates.lineSwarm(numMembers,startPoint, 60);
//        }
//        else{
//            throw new IllegalArgumentException("Cannot find swarm type");
//        }
//
//        for(int i = 0; i < numMembers; i++) {
//            Enemy enemy = createEnemyType(enemyType,swarmPoints.get(i).x , swarmPoints.get(i).y);
//            swarm.add(enemy);
//        }
//        return swarm;
//    }

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


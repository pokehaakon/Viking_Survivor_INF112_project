package Actors.Enemy;


import Actors.Coordinates;
import Actors.Enemy.EnemyTypes.*;
import Actors.Player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.*;

import static Tools.ShapeTools.createCircleShape;
import static Tools.ShapeTools.createSquareShape;
import static java.lang.Math.random;


public class EnemyFactory implements IEnemyFactory {

    private final static List<String> enemyTypes = Arrays.asList("ENEMY1", "ENEMY2");
    private final World world;

    public EnemyFactory(World world) {
        this.world = world;
    }

    @Override
    public Enemy createEnemyType(String type, float x, float y, float scale){

        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }
        Enemy enemy;
        switch (type.toUpperCase()) {
            case "ENEMY1": {
                Shape shape = createCircleShape(scale/2);
                Texture texture = new Texture(Gdx.files.internal(Sprites.ENEMY_1_PNG));
                enemy = new Enemy(createEnemyBody(x, y, shape), texture, scale);
                shape.dispose();
                //texture.dispose();
                break;
            }
            case "ENEMY2": {
                Shape shape = createCircleShape(scale/2);
                Texture texture = new Texture(Gdx.files.internal(Sprites.ENEMY_2_PNG));
                enemy = new Enemy(createEnemyBody(x, y, shape), texture, scale);
                shape.dispose();
                //texture.dispose();
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
            Enemy newEnemy = createEnemyType(type, startPoint.x, startPoint.y, 1); //TODO add scale!
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
            Enemy enemy = createEnemyType(randomEnemyType(), startPoint.x, startPoint.y, 1); //TODO add scale!
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

    private Body createEnemyBody(float x, float y, Shape shape) {
        BodyDef enemyBodyDef = new BodyDef();

        enemyBodyDef.type = BodyDef.BodyType.DynamicBody;
        enemyBodyDef.position.set(x, y);
        enemyBodyDef.fixedRotation = true;

        FixtureDef fixtureDefEnemy = new FixtureDef();
        fixtureDefEnemy.shape = shape;
        fixtureDefEnemy.density = 1f;
        fixtureDefEnemy.friction = 0;
        fixtureDefEnemy.restitution = 0;
        fixtureDefEnemy.isSensor = false;


        Body enemyBody = world.createBody(enemyBodyDef);
        enemyBody.createFixture(fixtureDefEnemy);

        return enemyBody;
    }
}


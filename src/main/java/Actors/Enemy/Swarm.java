//package Actors.Enemy;
//
//import Actors.Enemy.Enemy;
//import Actors.Enemy.SwarmType;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Swarm extends Enemy {
//    private static final float SWARM_SPEED_MULTIPLIER = 1.5f;
//
//    private float startDeltaX;
//    private float startDeltaY;
//    private boolean movementStarted;
//    private SwarmType swarmType;
//    private List<Enemy> enemies;
//
//    public Swarm() {
//        this.enemies = new ArrayList<>();
//        movementStarted = false;
//    }
//    public void add(Enemy enemy) {
//        enemy.speedX *= SWARM_SPEED_MULTIPLIER;
//        enemy.speedY *= SWARM_SPEED_MULTIPLIER;
//        enemies.add(enemy);
//    }
//
//    public void setSwarmType(SwarmType swarmType) {
//        this.swarmType = swarmType;
//    }
//
//    @Override
//    public void attack(Actor actor) {
//        getSpawnDelta(actor);
//        for(Enemy enemy : enemies) {
//            enemy.moveTowardsPosition(startDeltaX, startDeltaY);
//        }
//
//    }
//    private void getSpawnDelta(Actor actor) {
//        int centerIndex;
//
//        if(swarmType == SwarmType.SQUARE) {
//            centerIndex = (int)Math.ceil(Math.sqrt(enemies.size()));
//        }
//        //for line swarm
//        else{
//            centerIndex = enemies.size()/2;
//        }
//        Enemy centerEnemy = enemies.get(centerIndex);
//        // Swarm moves in a straight line, based on player's position when spawned
//        // want to store the initial delta position between the center enemy and the player
//        if (!movementStarted) {
//            startDeltaX = actor.x - centerEnemy.x;
//            startDeltaY = actor.y - centerEnemy.y;
//            float length = (float)Math.sqrt(startDeltaX * startDeltaX + startDeltaY * startDeltaY);
//            if (length != 0) {
//                startDeltaX /= length;
//                startDeltaY /= length;
//            }
//            movementStarted = true;
//        }
//
//    }
//
//
//    @Override
//    public void draw(SpriteBatch batch) {
//        for(Enemy enemy: enemies) {
//            enemy.draw(batch);
//        }
//    }
//
//    @Override
//    public boolean collision(Actor actor) {
//        boolean collision = false;
//        for(Enemy enemy:enemies) {
//            if(enemy.hitBox.intersects(actor.hitBox)) {
//                collision = true;
//            }
//        }
//        return collision;
//    }
//
//
//    @Override
//    public void moveInRelationTo(Actor actor) {
//        for(Enemy enemy: enemies) {
//            enemy.x -= actor.speedX;
//            enemy.y -= actor.speedY;
//        }
//
//    }
//
//    @Override
//    public void destroy() {
//        for(Enemy enemy:enemies){
//            enemy.destroy();
//        }
//    }
//
//
//}
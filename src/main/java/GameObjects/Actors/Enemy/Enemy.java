package GameObjects.Actors.Enemy;

import GameObjects.Actors.Actor;
import GameObjects.Actors.Stats.EnemyStats;
import com.badlogic.gdx.physics.box2d.Body;

import static Tools.BodyTool.createBody;
import static Tools.FilterTool.createFilter;

import static InputProcessing.Coordinates.RandomCoordinates.*;

public class Enemy extends Actor{

    public float knockBackResistance;

    private EnemyStats stats;

    private String enemyType;


    public Enemy(Body body, String spawnGIF, float scale, EnemyStats stats) {
        super(body,scale);
        this.stats = stats;

        // stats
        HP = stats.HP();
        speed = stats.speed();
        damage = stats.damage();
        armour = stats.armour();
        knockBackResistance = stats.knockBackResistance();


    }


    /**
     * Check if enemy is out of bounds - if the distance between player and enemy is over a certain threshold.
     * If it is, then its destroy tag is set to true.
     * @param player the player
     */
    public boolean outOfBounds(Actor player) {
        float dx = body.getPosition().x - player.getBody().getPosition().x;
        float dy =  body.getPosition().y - player.getBody().getPosition().y;
        float dist = (float) Math.sqrt(dx*dx + dy*dy);
        return(dist > DESPAWN_RADIUS);
    }







}

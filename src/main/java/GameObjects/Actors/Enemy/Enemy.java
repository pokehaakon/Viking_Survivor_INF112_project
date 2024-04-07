package GameObjects.Actors.Enemy;

import GameObjects.Actors.Actor;
import GameObjects.Actors.Stats.EnemyStats;
import GameObjects.BodyFeatures;
import com.badlogic.gdx.graphics.Texture;

import static Tools.FilterTool.createFilter;

import static InputProcessing.Coordinates.SpawnCoordinates.*;

public class Enemy extends Actor{

    public float knockBackResistance;

    private EnemyStats stats;



    public Enemy(String type,Texture texture, BodyFeatures bodyFeatures, float scale, EnemyStats stats) {
        super(type,texture,bodyFeatures,scale);
        this.stats = stats;

        // stats
        HP = stats.HP();
        speed = stats.speed();
        damage = stats.damage();
        armour = stats.armour();
        knockBackResistance = stats.knockBackResistance();
    }
    public Enemy() {

    }


    /**
     * Check if enemy is out of bounds - if the distance between player and enemy is over a certain threshold.
     * If it is, then its destroy tag is set to true.
     * @param player the player
     */
    public boolean outOfBounds(Actor player, double deSpawnRadius) {
        float dx = body.getPosition().x - player.getBody().getPosition().x;
        float dy =  body.getPosition().y - player.getBody().getPosition().y;
        float dist = (float) Math.sqrt(dx*dx + dy*dy);
        return(dist > deSpawnRadius);
    }

    public void setStats(EnemyStats newStats) {
        HP = newStats.HP();
        speed = newStats.speed();
        damage = newStats.damage();
        armour = newStats.armour();
        knockBackResistance = newStats.knockBackResistance();
    }







}

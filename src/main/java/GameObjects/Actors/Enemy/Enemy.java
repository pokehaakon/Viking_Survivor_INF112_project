package GameObjects.Actors.Enemy;

import Animations.AnimationState;
import GameObjects.Actors.ObjectTypes.EnemyType;
import GameObjects.Actors.Actor;
import GameObjects.Actors.Stats.EnemyStats;
import GameObjects.BodyFeatures;
import Rendering.AnimationRender;

import static Tools.FilterTool.createFilter;

public class Enemy extends Actor<EnemyType> {

    public float knockBackResistance;

    private EnemyStats stats;



    public Enemy(EnemyType type,AnimationRender render, BodyFeatures bodyFeatures, float scale, EnemyStats stats) {
        super(type,render, bodyFeatures, scale);
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

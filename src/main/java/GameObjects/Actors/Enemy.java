package GameObjects.Actors;

import GameObjects.Animations.AnimationRendering.AnimationHandler;

import GameObjects.ObjectTypes.EnemyType;
import GameObjects.Actors.Stats.EnemyStats;
import GameObjects.BodyFeatures;

import static Tools.FilterTool.createFilter;

public class Enemy extends Actor<EnemyType> {

    public float knockBackResistance;

    private EnemyStats stats;



    public Enemy(EnemyType type, AnimationHandler animationHandler, BodyFeatures bodyFeatures, float scale, EnemyStats stats) {
        super(type,animationHandler, bodyFeatures, scale);
        this.stats = stats;

        // stats
        HP = stats.HP();
        speed = stats.speed();
        damage = stats.damage();
        armour = stats.armour();
        knockBackResistance = stats.knockBackResistance();

    }




    public void setStats(EnemyStats newStats) {
        HP = newStats.HP();
        speed = newStats.speed();
        damage = newStats.damage();
        armour = newStats.armour();
        knockBackResistance = newStats.knockBackResistance();
    }







}

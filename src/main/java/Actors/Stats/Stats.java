package Actors.Stats;


public class Stats {

    //player
    public static final float PLAYER_HP = 100;
    public static final float PLAYER_SPEED = 10000 ;
    public static final float PLAYER_DAMAGE = 100;
    public static final float PLAYER_ARMOUR = 100;

    //enemy 1
    public static final float ENEMY1_HP = 100;
    public static final float ENEMY1_SPEED = 100;
    public static final float ENEMY1_DAMAGE = 100;
    public static final float ENEMY1_ARMOUR = 100;
    public static final float ENEMY1_KNOCKBACK_RESISTANCE = 100;

    //enemy 2
    public static final float ENEMY2_HP = 100;
    public static final float ENEMY2_SPEED = 100;
    public static final float ENEMY2_DAMAGE = 100;
    public static final float ENEMY2_ARMOUR = 100;
    public static final float ENEMY2_KNOCKBACK_RESISTANCE = 100;



    public static PlayerStats player() {
        return new PlayerStats(PLAYER_HP,PLAYER_SPEED,PLAYER_DAMAGE,PLAYER_ARMOUR);
    }
    public static EnemyStats enemy1() {
       return new EnemyStats(ENEMY1_HP,ENEMY1_SPEED,ENEMY1_DAMAGE,ENEMY1_ARMOUR, ENEMY1_KNOCKBACK_RESISTANCE);
    }


    public static EnemyStats enemy2() {
        return new EnemyStats(ENEMY2_HP,ENEMY2_SPEED,ENEMY2_DAMAGE,ENEMY2_ARMOUR, ENEMY2_KNOCKBACK_RESISTANCE);
    }









}

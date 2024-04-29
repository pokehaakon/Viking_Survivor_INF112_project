package GameObjects.Actors.Stats;


public abstract class StatsConstants {

    //player: random values
    public static final float PLAYER_START_HP = 100;
    public static final float PLAYER_SPEED = 200 ;
    public static final float PLAYER_START_DAMAGE = 40;
    public static final float PLAYER_START_ARMOUR = 100;
    public static final float PLAYER_START_LEVEL = 1;
    public static final float PLAYER_START_XP = 0;

    //enemy 1: random values
    public static final float ENEMY1_HP = 100;
    public static final float ENEMY1_SPEED = 60;
    public static final float ENEMY1_DAMAGE = 5;
    public static final float ENEMY1_ARMOUR = 100;
    public static final float ENEMY1_KNOCKBACK_RESISTANCE = 100;

    //enemy 2 : random values
    public static final float ENEMY2_HP = 100;
    public static final float ENEMY2_SPEED = 60;
    public static final float ENEMY2_DAMAGE = 5;
    public static final float ENEMY2_ARMOUR = 100;
    public static final float ENEMY2_KNOCKBACK_RESISTANCE = 100;

    public static final int SWARM_SPEED_MULTIPLIER = 5;
    public static final float XPAMOUNT = 10;
    public static final boolean IS_PICKED_UP = false;


    public static class Stats {
        public final float SPEED, HP, DAMAGE, RESISTANCE;
        public Stats(float speed, float hp, float damage, float resistance) {
            SPEED = speed;
            HP = hp;
            DAMAGE = damage;
            RESISTANCE = resistance;
        }
    }

    public static Stats player() {
        return new Stats(PLAYER_SPEED, PLAYER_START_HP, PLAYER_START_DAMAGE, PLAYER_START_ARMOUR);
        //return new PlayerStats(PLAYER_START_HP, PLAYER_SPEED, PLAYER_START_DAMAGE, PLAYER_START_ARMOUR, PLAYER_START_XP);
    }
    public static EnemyStats enemy1() {
       return new EnemyStats(ENEMY1_HP, ENEMY1_SPEED, ENEMY1_DAMAGE, ENEMY1_ARMOUR, ENEMY1_KNOCKBACK_RESISTANCE);
    }


    public static EnemyStats enemy2() {
        return new EnemyStats(ENEMY2_HP, ENEMY2_SPEED, ENEMY2_DAMAGE, ENEMY2_ARMOUR, ENEMY2_KNOCKBACK_RESISTANCE);
    }

    public static PickupStats pickupStats() {
        return new PickupStats(XPAMOUNT);
    }








}
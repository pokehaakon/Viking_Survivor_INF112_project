package Actors;

public class Stats {
    public int HP;
    public float speed;
    public int damage;

    public final static int SWARM_MULTIPLIER = 2;

    public Stats(int HP, float speed, int damage) {
        this.HP = HP;
        this.speed = speed;
        this.damage = damage;
    }

    public static Stats enemy1() {
        return new Stats(100,3,4);
    }

    public static Stats enemy2() {
        return new Stats(100,3,4);
    }

    public static Stats player() {
        return new Stats(100,5, 10);
    }



}

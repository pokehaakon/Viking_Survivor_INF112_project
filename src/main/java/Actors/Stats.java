package Actors;

public class Stats {
    public int HP;
    public int speed;
    public int damage;

    public final static int SWARM_MULTIPLIER = 2;

    public Stats(int HP, int speed, int damage) {
        this.HP = HP;
        this.speed = speed;
        this.damage = damage;
    }

    public static Stats enemy1() {
        return new Stats(100,5,4);
    }

    public static Stats enemy2() {
        return new Stats(100,10,4);
    }

    public static Stats player() {
        return new Stats(100,15, 10);
    }

    public static Stats squareHorde() {
        return new Stats(100,15, 10);
    }

}

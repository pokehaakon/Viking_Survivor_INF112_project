package Actors;

public class Stats {
    public int HP;
    public int speedX;
    public int speedY;
    public int damage;

    public final static int SWARM_MULTIPLIER = 2;

    public Stats(int HP, int speedY,  int speedX, int damage) {
        this.HP = HP;
        this.speedX = speedX;
        this.speedY =  speedY;
        this.damage = damage;
    }

    public static Stats enemy1() {
        return new Stats(100,5,5,4);
    }

    public static Stats enemy2() {
        return new Stats(100,10,10,4);
    }

    public static Stats player() {
        return new Stats(100,15,15, 10);
    }



}

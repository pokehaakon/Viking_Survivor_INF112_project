package Actors.Player;

import Actors.Actor;
import Actors.Stats;
import com.badlogic.gdx.physics.box2d.Body;

public class PlayerExample extends Actor {
    private String name;
    public Stats stats;

    public static float x, y;

    public boolean idle, up, down, left, rigth;

    public PlayerExample(String name, Stats stats) {
        super(stats);
        this.name = name;
        initialize("img_4.png", 800, 500, 100,100);
        speedX = 0;
        speedY = 0;
    }



    public void setSpeedX(int newSpeed) {
        speedX = newSpeed;
    }

    public void setSpeedY(int newSpeed){
        speedY = newSpeed;
    }


    @Override
    public Body getBody() {
        return null;
    }
}

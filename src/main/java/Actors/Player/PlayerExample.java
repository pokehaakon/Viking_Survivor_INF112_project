package Actors.Player;

import Actors.Actor;
import Actors.Stats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class PlayerExample extends Actor {
    private String name;
    public Stats stats;

    //public static float x, y;

    public boolean idle, up, down, left, rigth;

    public PlayerExample(String name, Stats stats, Body body) {
        super(stats, body);
        this.name = name;
        initialize("img_4.png", 800, 500, 100,100);
        speedX = 0;
        speedY = 0;
    }



    public void setSpeedX(float newSpeed) {
        speedX = newSpeed;
    }

    public void setSpeedY(float newSpeed){
        speedY = newSpeed;
    }

    @Override
    public void draw(SpriteBatch batch) {
        Vector2 playerPos = body.getPosition().cpy();

        Vector2 correctionVector = body.getLinearVelocity().cpy();
        correctionVector.scl(1f/60);
        batch.draw(
                spriteImage,
                playerPos.x - correctionVector.x,
                playerPos.y - correctionVector.y,
                this.hitBox.width,
                this.hitBox.height
        );
    }

}

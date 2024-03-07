package Actors.Enemy.EnemyTypes;

import Actors.Player.Actor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Enemy extends Actor {

    public Enemy(Body body, Texture sprite, float scale) {
        super(body, sprite, scale);

    }


    @Override
    public void draw(SpriteBatch batch) {
        Vector2 p = body.getPosition();
        batch.draw(sprite,p.x,p.y, sprite.getWidth()*scale,  sprite.getHeight()*scale);
    }

    @Override
    public void update() {


    }
}

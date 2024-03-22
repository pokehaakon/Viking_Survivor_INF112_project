package Actors.Player;

import Actors.Actor;
import Actors.Stats.PlayerStats;
import Tools.GifDecoder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Player extends Actor {
    public Vector2 velocityVector;
    public int level;
    public float XP;

    private final PlayerStats stats;

    private Animation<TextureRegion> animation;

    public Player(Body body, Texture sprite, float scale, PlayerStats stats) {
        super(body, sprite, scale);
        this.stats = stats;

        HP = stats.HP();
        speed = stats.speed();
        damage = stats.damage();
        armour = stats.armour();
        XP = stats.XP();
        level = 1;
        animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("vikingright.gif").read());
    }

    //@Override
    public void draw(SpriteBatch batch, float elapsedTime) {
        Vector2 p = body.getPosition();
        animation.setFrameDuration(0.2f);

        batch.draw(animation.getKeyFrame(elapsedTime), p.x, p.y, 500.0f, 500.0f);
        //batch.d
//        Vector2 p = body.getPosition();
//        batch.draw(sprite,p.x,p.y, sprite.getWidth()*scale,  sprite.getHeight()*scale);
    }

}

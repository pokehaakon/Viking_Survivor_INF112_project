package Actors.Player;

import Actors.Actor;
import Actors.Stats.PlayerStats;
import Animations.Animations;
import Animations.PlayerAnimationStates;
import Tools.GifDecoder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Player extends Actor {
    public Vector2 velocityVector;
    public int level;
    public float XP;

    private final PlayerStats stats;

    // for animation, from Hallvards code
    public boolean lastMoveRight = true, idle = true;

    public PlayerAnimationStates currentAnimationState;


    public Player(Body body, Texture sprite, float scale, PlayerStats stats) {
        super(body, sprite, scale);
        this.stats = stats;

        HP = stats.HP();
        speed = stats.speed();
        damage = stats.damage();
        armour = stats.armour();
        XP = stats.XP();
        level = 1;

        currentAnimationState = PlayerAnimationStates.IDLE_RIGHT;

    }

    /**
     * Checks if player is idle - if no key is pressed
     * @return a boolean value
     */
    public boolean isIdle() {
        return idle;
    }


}

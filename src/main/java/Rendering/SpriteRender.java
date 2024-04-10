package Rendering;

import Animations.AnimationState;
import GameObjects.GameObject;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

public class SpriteRender implements AnimationRender {

    private Sprite sprite;

    private boolean flip = false;

    private int flipMultiplier = 1;

    Map<AnimationState, Sprite> stateAnimations;

    public SpriteRender(Map<AnimationState, Sprite> stateAnimations) {
        this.stateAnimations = stateAnimations;
    }

    @Override
    public void draw(SpriteBatch batch, float elapsedTime, GameObject object) {
        Vector2 pos = object.getBody().getPosition();
        batch.draw(sprite,
                pos.x,
                pos.y,
                flipMultiplier * sprite.getWidth() * object.getScale(),
                sprite.getHeight() * object.getScale());

    }


    @Override
    public void setAnimation(AnimationState state) {
        sprite = stateAnimations.get(state);
    }


}

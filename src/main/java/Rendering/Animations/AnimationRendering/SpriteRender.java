package Rendering.Animations.AnimationRendering;

import Rendering.Animations.AnimationState;
import GameObjects.GameObject;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.EnumMap;
import java.util.Map;

public class SpriteRender implements AnimationRender {

    private Sprite sprite;

    private boolean flip = false;

    private int flipMultiplier = 1;


    Map<AnimationState, Sprite> stateAnimations = new EnumMap<>(AnimationState.class);

    protected SpriteRender(Map<AnimationState, String> stateAnimations) {
        getSprites(stateAnimations);
    }

    @Override
    public void draw(SpriteBatch batch, long frame, GameObject<?> object) {
        Vector2 pos = object.getBody().getPosition();
        batch.draw(sprite,
                pos.x - sprite.getWidth()/2*object.getScale(), // subtracting offset
                pos.y - sprite.getWidth()/2*object.getScale(),
                sprite.getWidth() * object.getScale(),
                sprite.getHeight() * object.getScale());

    }


    @Override
    public void setAnimation(AnimationState state) {
        sprite = stateAnimations.get(state);
    }

    @Override
    public float getWidth(AnimationState state) {
        return stateAnimations.get(state).getWidth();
    }

    @Override
    public float getHeight(AnimationState state) {
        return stateAnimations.get(state).getHeight();
    }

    @Override
    public void initAnimations(Map<AnimationState, String> animationMap) {
        getSprites(animationMap);
    }
    private void getSprites(Map<AnimationState,String> map) {
        for(Map.Entry<AnimationState, String> entry : map.entrySet()) {
            AnimationState state = entry.getKey();
            String filePath = entry.getValue();
            stateAnimations.put(state, Sprites.getSprite(filePath));
        }
    }


}

package GameObjects.Animations.AnimationRendering;

import GameObjects.Animations.AnimationState;
import GameObjects.GameObject;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class SpriteRender implements AnimationRender {

    private Sprite sprite;


    private float rotation = 0;

    private float rotationSpeed = 0;

    private final AnimationLibrary  animationLibrary;
    private final Map<AnimationState, Sprite> stateAnimations = new EnumMap<>(AnimationState.class);

    public SpriteRender(AnimationLibrary animationLibrary, Map<AnimationState, String> stateAnimations) {
        this.animationLibrary = animationLibrary;
        getSprites(stateAnimations);
    }

    public SpriteRender(AnimationLibrary animationLibrary) {
        this.animationLibrary = animationLibrary;

    }

    @Override
    public void draw(SpriteBatch batch, float elapsedTime, GameObject object) {
        float originX = object.getBody().getPosition().x;
        float originY = object.getBody().getPosition().y;

        // Adjust origin to center of the sprite
        originX -= sprite.getWidth()* 0.5f;
        originY -= sprite.getWidth() * 0.5f;

        batch.draw(
                sprite,
                originX,
                originY,
                sprite.getWidth() * 0.5f,
                sprite.getHeight() * 0.5f,
                sprite.getWidth(),
                sprite.getHeight(),
                object.getScale(),
                object.getScale(),
                rotation
        );

        rotation += rotationSpeed;

        if(rotation >= 360) {
            rotation = 0;
        }
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
    public void setAnimations(Map<AnimationState, String> animationMap) {
        getSprites(animationMap);

    }

    @Override
    public void rotate(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;

    }

    @Override
    public void stopRotation() {
        rotation = 0;
        rotationSpeed = 0;

    }

    private void getSprites(Map<AnimationState,String> map) {
        for(Map.Entry<AnimationState, String> entry : map.entrySet()) {
            AnimationState state = entry.getKey();
            String filePath = entry.getValue();
            stateAnimations.put(state, animationLibrary.getSprite(filePath));
        }
    }


}

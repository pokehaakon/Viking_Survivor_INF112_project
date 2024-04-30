package Rendering.Animations.AnimationRendering;

import GameObjects.GameObject;
import Rendering.Animations.AnimationState;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.EnumMap;
import java.util.Map;

public class SpriteRender implements AnimationRender {
    private final float scale;
    private final Map<AnimationState, Sprite> stateAnimations = new EnumMap<>(AnimationState.class);

    private AnimationState state;

    protected SpriteRender(Map<AnimationState, String> stateAnimations, float scale) {
        //stateAnimationsTemp = stateAnimations;
        getSprites(stateAnimations);
        this.scale = scale;
    }

    @Override
    public void draw(SpriteBatch batch, long frame, GameObject object) {
        Vector2 pos = object.getBody().getPosition();
        Sprite sprite = stateAnimations.get(state);
        if (sprite == null) return;
        Vector2 regionRect = new Vector2(sprite.getRegionWidth(), sprite.getRegionHeight());

        regionRect.scl(1/regionRect.y * scale); // * Main.PPM);
        batch.draw(sprite,
                pos.x - regionRect.x / 2, // subtracting offset
                pos.y - regionRect.y / 2,
                regionRect.x,
                regionRect.y
        );

    }


    @Override
    public void setAnimation(AnimationState state) {
        this.state = state;
        //sprite = stateAnimations.get(state);
    }

    @Override
    public float getWidth(AnimationState state) {
        return stateAnimations.get(state).getWidth();
    }

    @Override
    public float getHeight(AnimationState state) {
        return stateAnimations.get(state).getHeight();
    }


    /**
     * IMPORTANT, THIS ONLY LOADS IN RENDER THREAD!
     * @param map from AnimationState to file path
     */
    private void getSprites(Map<AnimationState, String> map) {
        for(Map.Entry<AnimationState, String> entry : map.entrySet()) {
            AnimationState state = entry.getKey();
            String filePath = entry.getValue();
            stateAnimations.put(
                    state,
                    Sprites.getSprite(
                            filePath,
                            s -> stateAnimations.put(state, s)
                    )
            );
        }
    }


}

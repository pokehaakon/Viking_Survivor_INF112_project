package Rendering.Animations.AnimationRendering;

import Rendering.Animations.AnimationState;
import GameObjects.GameObject;
import VikingSurvivor.app.Main;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.EnumMap;
import java.util.Map;

public class SpriteRender implements AnimationRender {

    private Sprite sprite = null;

    private AnimationState state;
    private final float scale;

    private final Map<AnimationState, Sprite> stateAnimations = new EnumMap<>(AnimationState.class);

    protected SpriteRender(Map<AnimationState, String> stateAnimations, float scale) {
        //stateAnimationsTemp = stateAnimations;
        getSprites(stateAnimations);
        this.scale = scale;
    }

    @Override
    public void draw(SpriteBatch batch, long frame, GameObject object) {
        Vector2 pos = object.getBody().getPosition();
        sprite = stateAnimations.get(state);
        if (sprite == null) return;
        Vector2 regionRect = new Vector2(sprite.getRegionWidth(), sprite.getRegionHeight());
        float max = Math.max(regionRect.x, regionRect.y);
        max = regionRect.y;
        regionRect.scl(1/max * scale * Main.PPM);
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
     * IMPORTANT, ONLY LOAD IN RENDER THREAD!
     * @param map
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

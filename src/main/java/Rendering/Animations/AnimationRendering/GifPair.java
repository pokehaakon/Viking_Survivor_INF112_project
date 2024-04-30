package Rendering.Animations.AnimationRendering;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface GifPair {
    Animation<TextureRegion> left();
    Animation<TextureRegion> right();
}
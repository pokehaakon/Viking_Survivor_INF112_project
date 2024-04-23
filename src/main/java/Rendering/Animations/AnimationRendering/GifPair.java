package Rendering.Animations.AnimationRendering;

import Tools.GifDecoder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface GifPair {
    Animation<TextureRegion> left();
    Animation<TextureRegion> right();
}
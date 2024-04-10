package Rendering;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public record GifPair(Animation<TextureRegion> left, Animation<TextureRegion> right){}

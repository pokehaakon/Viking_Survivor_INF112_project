package Rendering.Animations.AnimationRendering;

import Tools.ExcludeFromGeneratedCoverage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.google.common.collect.ListMultimap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Store the game sounds
 */
@ExcludeFromGeneratedCoverage
public abstract class SoundManager {


    // source: https://www.youtube.com/watch?v=AjJEhMtqDzE (free of use)
    public static final String ATTACK_SOUND ="Knife Stab Sound Effect [FREE USE].mp3";

    private static final Map<String, Sound> soundEffects = new HashMap<>();
    private static final Map<String, Sound> music = new HashMap<>();

    private static boolean isMuted = false;

    static{
        soundEffects.put(ATTACK_SOUND, Gdx.audio.newSound(Gdx.files.internal(ATTACK_SOUND)));

    }


    /**
     * Gets the sound from its storage and calls sound.play()
     * @param soundName name of the sound
     */
    public static void playSoundEffect(String soundName) {

        if(!soundEffects.containsKey(soundName)) {
            throw new IllegalArgumentException("Can't find the sound name");
        }
        Sound sound = soundEffects.get(soundName);

        if(Objects.isNull(sound)) {
            throw new NullPointerException("Can not play when sound is null!");
        }
        if (!isMuted) {
            sound.play();
        }
    }

    public static void dispose() {
        for (Sound sound : soundEffects.values()) {
            sound.dispose();
        }
        soundEffects.clear();
    }

    public static void mute () {
        isMuted = true;
    }

    public static void unmute () {
        isMuted = false;
    }

}

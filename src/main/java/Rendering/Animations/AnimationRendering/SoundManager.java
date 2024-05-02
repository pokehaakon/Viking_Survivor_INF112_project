package Rendering.Animations.AnimationRendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public abstract class SoundManager {

    public static final String ATTACK_SOUND ="Knife Stab Sound Effect [FREE USE].mp3";


    private static final Map<String, Sound> soundEffects = new HashMap<>();
    private static final Map<String, Sound> music = new HashMap<>();

    static{
        soundEffects.put(ATTACK_SOUND, Gdx.audio.newSound(Gdx.files.internal(ATTACK_SOUND)));


    }



    public static void playSoundEffect(String soundName) {

        if(!soundEffects.containsKey(soundName)) {
            throw new IllegalArgumentException("Can't find the sound name");
        }
        Sound sound = soundEffects.get(soundName);
        if(Objects.isNull(sound)) {
            throw new NullPointerException("Can not play when sound is null!");
        }
        sound.play();
    }

    public static void dispose() {
        for (Sound sound : soundEffects.values()) {
            sound.dispose();
        }
        soundEffects.clear();
    }




}

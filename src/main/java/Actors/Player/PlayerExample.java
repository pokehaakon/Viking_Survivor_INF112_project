package Actors.Player;

import Actors.Actor;
import Actors.Coordinates;
import Actors.Stats;
import com.badlogic.gdx.physics.box2d.Body;

public class PlayerExample extends Actor {
    private String name;
    public Stats stats;

    public PlayerExample(String name, Stats stats) {
        super(stats);
        this.name = name;
        initialize("img_4.png", 800, 500);
    }


    @Override
    public Body getBody() {
        return null;
    }
}

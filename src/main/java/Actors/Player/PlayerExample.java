package Actors.Player;

import Actors.Actor;
import com.badlogic.gdx.physics.box2d.Body;

public class PlayerExample extends Actor {
    private String name;

    public PlayerExample(String name, int x, int y) {
        super(x,y);
        this.name = name;

        init("obligator.png", 100,100);

    }


    @Override
    public Body getBody() {
        return null;
    }
}

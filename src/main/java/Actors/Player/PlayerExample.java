package Actors.Player;

import Actors.Actor;
import com.badlogic.gdx.physics.box2d.Body;

public class PlayerExample extends Actor {
    //private Texture spriteImage;
    //public Rectangle spriteRect;

    public PlayerExample(String name) {
        super(name);
        init("obligator.png", 100,100);

    }

   // @Override
   // public void draw(SpriteBatch batch) {
    //    batch.draw(spriteImage, spriteRect.x, spriteRect.y, spriteRect.width, spriteRect.height);
    //}

    @Override
    public Body getBody() {
        return null;
    }
}

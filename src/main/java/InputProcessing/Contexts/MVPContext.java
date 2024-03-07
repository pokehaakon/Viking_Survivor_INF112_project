package InputProcessing.Contexts;

import Actors.Actor;
import Actors.Enemy.Enemy;
import Actors.Player.PlayerExample;
import InputProcessing.ContextualInputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

public class MVPContext extends Context {

    Actor player;

    List<Enemy> enemies;

    SpriteBatch batch;

    public MVPContext(String name, SpriteBatch batch, ContextualInputProcessor iProc) {
        super(name, iProc);



    }
    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

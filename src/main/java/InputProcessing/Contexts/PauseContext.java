package InputProcessing.Contexts;

import InputProcessing.Contexts.Context;
import InputProcessing.ContextualInputProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;

public class PauseContext extends Context {
    private final SpriteBatch batch;
    private Stage stage;
    private Table table;
    private Texture bgTexture;
    private Image backgroundImage;

    public PauseContext(String name, SpriteBatch batch, ContextualInputProcessor iProc) {
        super(name, iProc);
        this.batch = batch;

//        this.addAction(Input.Keys.P, ContextualInputProcessor.KeyEvent.KEYDOWN, (x) -> this.getInputProcessor().setContext("GAME"));
//        this.addAction(Input.Keys.ESCAPE, ContextualInputProcessor.KeyEvent.KEYDOWN, (x) -> this.getInputProcessor().setContext("MAINMENU"));

        this.stage = new Stage();

        bgTexture = new Texture(Gdx.files.internal("assets/PauseMenu2.jpg"));

        backgroundImage = new Image(bgTexture);

        table = new Table();
        table.setFillParent(true);
        table.setBackground(backgroundImage.getDrawable());

        stage.addActor(table);

        //Gdx.input.setInputProcessor(stage);
        this.setInputProcessor(stage);

        table.setDebug(true);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();
        ScreenUtils.clear(Color.WHITE);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}

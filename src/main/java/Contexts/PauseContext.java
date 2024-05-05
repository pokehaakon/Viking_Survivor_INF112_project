package Contexts;

import InputProcessing.ContextualInputProcessor;
import InputProcessing.DefaultInputProcessor;
import InputProcessing.KeyStates;
import Tools.ExcludeFromGeneratedCoverage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;

class PauseContext extends Context {
    private final SpriteBatch batch;
    private Stage stage;
    private Table table;
    private Texture bgTexture;
    private Image backgroundImage;
    private KeyStates keyStates;

    // Might get deleted and replaced with a screen that covers MVP and pauses game, similar to Lvl up
    public PauseContext(String name, SpriteBatch batch, ContextualInputProcessor iProc) {
        super(iProc);
        this.batch = batch;

        this.stage = new Stage();

        this.setInputProcessor(createInputProcessor());

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
    @ExcludeFromGeneratedCoverage
    @Override
    public void render(float delta) {
        batch.begin();
        ScreenUtils.clear(Color.WHITE);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        batch.end();
    }

    private InputProcessor createInputProcessor() {
        Context me = this;
        keyStates = new KeyStates();
        return new DefaultInputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return switch (keycode) {
                    case Input.Keys.ESCAPE -> {keyStates.setInputKey(keycode); System.exit(0); yield true;}
                    case Input.Keys.P -> {me.getContextualInputProcessor().setContext("MVP"); yield true;}
                    default -> false;
                };
            }
        };
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

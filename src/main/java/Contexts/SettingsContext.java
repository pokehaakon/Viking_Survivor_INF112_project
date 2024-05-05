package Contexts;

import InputProcessing.ContextualInputProcessor;
import InputProcessing.DefaultInputProcessor;
import Rendering.Animations.AnimationRendering.SoundManager;
import Tools.ExcludeFromGeneratedCoverage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

class SettingsContext extends Context{
    private final SpriteBatch batch;
    private Stage stage;
    private Table table;
    private Texture mmTexture;
    private Image backgroundImage;

    @ExcludeFromGeneratedCoverage
    public SettingsContext(String name, SpriteBatch batch, ContextualInputProcessor iProc) {
        super(iProc);
        this.batch = batch;


        // Input listener for possible use of ContextualInputListener for buttons
        //setupInputListener();

        // Stage is created in constructor since render is called before stage initialization
        this.stage = new Stage();
        // Load the texture
        mmTexture = new Texture(Gdx.files.internal("Settings.jpg"));
        backgroundImage = new Image(mmTexture);

        // Creating a transparent drawable. Might be needed for buttons to be clickable.
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.CLEAR);
        pixmap.fill();
        TextureRegionDrawable transparentDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();

        // Style buttonStyle
        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = transparentDrawable;
        buttonStyle.down = transparentDrawable;
        buttonStyle.checked = transparentDrawable;


        // Create a table to layout the buttons
        table = new Table().padTop(490);
        table.setFillParent(true);
        table.setBackground(backgroundImage.getDrawable());

        //      Mute CheckBox
        // Checkbox unchecked/checked style
        Skin muteSkin = new Skin();

        muteSkin.add("mute_checked", new TextureRegion(new Texture(Gdx.files.internal("MuteChecked.png"))));
        muteSkin.add("mute_unchecked", new TextureRegion(new Texture(Gdx.files.internal("MuteUnchecked.png"))));

        ImageButton.ImageButtonStyle muteStyle = new ImageButton.ImageButtonStyle();

        muteStyle.imageUp = muteSkin.getDrawable("mute_unchecked");
        muteStyle.imageChecked = muteSkin.getDrawable("mute_checked");

        // Create checkbox
        ImageButton muteButton = new ImageButton(muteStyle);

        // Add button to table
        table.add(muteButton).padBottom(20).row();

        // Create back button
        Button backButton = new Button(buttonStyle);
        table.add(backButton).width(620).height(90).padRight(20).spaceBottom(20);

        // Mute/unmute game on click
        muteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(muteButton.isChecked()){

                    SoundManager.mute();
                } else {
                    SoundManager.unmute();
                }
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                iProc.setContext("MAINMENU");
            }
        });


        // Add table to stage
        stage.addActor(table);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        this.setInputProcessor(multiplexer);
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

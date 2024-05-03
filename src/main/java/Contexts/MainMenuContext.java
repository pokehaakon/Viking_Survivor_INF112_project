package Contexts;

import InputProcessing.ContextualInputProcessor;
import InputProcessing.DefaultInputProcessor;
import Tools.ExcludeFromGeneratedCoverage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

class MainMenuContext extends Context{
    private final SpriteBatch batch;
    private Stage stage;
    private Table table;
    private Texture mmTexture;
    private Image backgroundImage;

    @ExcludeFromGeneratedCoverage
    public MainMenuContext(String name, SpriteBatch batch, ContextualInputProcessor iProc) {
        super(iProc);
        this.batch = batch;


        // Input listener for possible use of ContextualInputListener for buttons
        //setupInputListener();

        // Stage is created im constructor since render is called before stage initialization
        this.stage = new Stage();
        // Load the texture
        mmTexture = new Texture(Gdx.files.internal("assets/MainMenu1.jpg"));
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
        table = new Table().padTop(700);
        table.setFillParent(true);
        table.setBackground(backgroundImage.getDrawable());


        // Create buttons
        Button startButton = new Button(buttonStyle);
        Button optionsButton = new Button(buttonStyle);
        Button exitButton = new Button(buttonStyle);

        // Add buttons to table
        table.add(startButton).width(250).height(55).padLeft(10).spaceBottom(30).row();
        table.add(optionsButton).width(250).height(60).padLeft(10).spaceBottom(30).row();
        table.add(exitButton).width(250).height(60).padLeft(10).spaceBottom(20);


        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent click, Actor exitButton) {
                Gdx.app.exit();
            }
        });
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Start game button clicked!");
                iProc.removeContext("ReleaseCandidate");
                iProc.setContext("ReleaseCandidate");
            }
        });
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Options button clicked!");
            }
        });

        table.setDebug(true);



        // Add table to stage
        stage.addActor(table);

        InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
        //multiplexer.addProcessor(createInputProcessor());
        //Gdx.input.setInputProcessor(stage);
        this.setInputProcessor(multiplexer);
    }


    private InputProcessor createInputProcessor() {
        Context me = this;
        return new DefaultInputProcessor() {
            @Override
            public boolean keyDown(int keycode) {

                return switch (keycode) {
                    case Input.Keys.S -> {System.out.println("S button clicked!"); yield true;}
                    case Input.Keys.ESCAPE -> {Gdx.app.exit(); yield true;}
                    default -> false;
                };
            }
        };
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

package VikingSurvivor.app;

import Contexts.Context;
import Contexts.ContextFactory;
import InputProcessing.ContextualInputProcessor;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class HelloWorld implements ApplicationListener {
	public static final int SET_FPS = 60;
	public static int millisToFrames(float millis) {return (int) (millis / 1000 * SET_FPS);}
	private SpriteBatch batch;
    private OrthographicCamera camera;
	private Context currentContext;
	private final Rectangle screenRect = new Rectangle(); //"used" when resizing
	private ContextualInputProcessor inProc;

	@Override
	public void create() {

		batch = new SpriteBatch();
		//batch.maxSpritesInBatch = 256;

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.setProjectionMatrix(camera.combined);

		inProc = new ContextualInputProcessor(inProc -> new ContextFactory(batch, camera, inProc));
		inProc.setContext("MAINMENU"); //set starting context
		//inProc.setContext("Training");


//		InputMultiplexer multiplexer = new InputMultiplexer();
//		multiplexer.addProcessor(inProc);


		Gdx.input.setInputProcessor(inProc);

		currentContext = inProc.getCurrentContext();


		Gdx.graphics.setForegroundFPS(SET_FPS);
	}


	@Override
	public void dispose() {
		inProc.dispose();
		batch.dispose();
	}

	@Override
	public void render() {
		//camera.position.x += 0.1f;
		camera.update();
		currentContext = inProc.getCurrentContext();
		batch.setProjectionMatrix(camera.combined); //probably not needed
		currentContext.render(0f); //delta not used

	}

	@Override
	public void resize(int width, int height) {
		// Called whenever the window is resized (including with its original site at
		// startup)

		screenRect.width = width;
		screenRect.height = height;
		currentContext.resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

}

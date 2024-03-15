package VikingSurvivor.app;

import InputProcessing.*;
import InputProcessing.Contexts.Context;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class HelloWorld implements ApplicationListener {
	private SpriteBatch batch;
    private OrthographicCamera camera;
	private Context currentContext;
	private Rectangle screenRect = new Rectangle(); //"used" when resizing
	private ContextualInputProcessor inProc;

	@Override
	public void create() {
		batch = new SpriteBatch();

		//camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.x = Gdx.graphics.getWidth() / 2f;
		camera.position.y = Gdx.graphics.getHeight() / 2f;
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		inProc = new ContextualInputProcessor(batch, camera);
		inProc.setContext("MAINMENU"); //set starting context

//		InputMultiplexer multiplexer = new InputMultiplexer();
//		multiplexer.addProcessor(inProc);


		Gdx.input.setInputProcessor(inProc);

		currentContext = inProc.getCurrentContext();


		Gdx.graphics.setForegroundFPS(60);
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
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

}

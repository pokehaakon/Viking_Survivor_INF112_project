package inf112.skeleton.app;

import InputProcessing.*;
import Simulation.SimulationThread;
import Tools.RollingSum;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import org.apache.maven.surefire.shared.lang3.tuple.ImmutablePair;
import org.apache.maven.surefire.shared.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static Rendering.Shapes.makeRectangle;
import static java.lang.Math.round;

public class HelloWorld implements ApplicationListener {
	private SpriteBatch batch;
	private BitmapFont font;
	private Texture spriteImage;
	private Texture rectSprite, rectSpriteEnemy;
	private Sound bellSound;
	private Rectangle spriteRect, spriteRectEnemy;
	private Rectangle screenRect = new Rectangle();
	private KeyStates keyStates;
	private World world;

	private Body player, enemy;
	private Lock renderLock;
	private RollingSum UpdateTime, FrameTime, FPS, UPS;
	private ContextFactory contextFactory;
	private OrthographicCamera camera;

	private long previousFrameStart = System.nanoTime();




	@Override
	public void create() {
		// Called at startup
		List<Pair<Integer, GameKey>> keyBinds= new ArrayList<>();
		keyBinds.add(new ImmutablePair<>(Input.Keys.W, GameKey.UP));
		keyBinds.add(new ImmutablePair<>(Input.Keys.A, GameKey.LEFT));
		keyBinds.add(new ImmutablePair<>(Input.Keys.S, GameKey.DOWN));
		keyBinds.add(new ImmutablePair<>(Input.Keys.D, GameKey.RIGHT));
		keyBinds.add(new ImmutablePair<>(Input.Keys.SPACE, GameKey.ZOOOM));
		keyBinds.add(new ImmutablePair<>(Input.Keys.ESCAPE, GameKey.QUIT));

		UpdateTime = new RollingSum(60*3);
		FrameTime = new RollingSum(60*3);
		FPS = new RollingSum(60 * 3);
		UPS = new RollingSum(60 * 3);

		keyStates = new KeyStates(keyBinds);
		contextFactory = new ContextFactory();
		contextFactory.setupGameContext(keyStates);

		InputMultiplexer multiplexer = new InputMultiplexer();

		ContextualInputProccessor inProc = new ContextualInputProccessor(contextFactory);
		inProc.setContext(ContextName.GAME);
		multiplexer.addProcessor(inProc);
		Gdx.input.setInputProcessor(multiplexer);


		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.RED);
		spriteImage = new Texture(Gdx.files.internal("obligator.png"));

		spriteRect = new Rectangle(1, 1, spriteImage.getWidth() / 2, spriteImage.getHeight() / 2);
		spriteRectEnemy = new Rectangle(1, 1, spriteImage.getWidth() / 2, spriteImage.getHeight() / 2);

		rectSprite = makeRectangle(spriteImage.getWidth() / 2, spriteImage.getHeight() / 2, 2,2,2,2, Color.GREEN, Color.CLEAR);
		rectSpriteEnemy = makeRectangle(spriteImage.getWidth() / 2, spriteImage.getHeight() / 2, 2,2,2,2, Color.RED, Color.CLEAR);

		bellSound = Gdx.audio.newSound(Gdx.files.internal("blipp.ogg"));


		world = createWorld();
		renderLock = new ReentrantLock(true);

		Thread simThread = new SimulationThread(renderLock, keyStates, world, UpdateTime, UPS, player);
		simThread.start();
		Gdx.graphics.setForegroundFPS(60);

		camera = new OrthographicCamera(480, 320);
		camera.position.x = 240;
		camera.position.y = 160;
		camera.update();
		//Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
		//debugRenderer.render(world, camera.combined);
	}


	private World createWorld() {
		Box2D.init();
		World world = new World(new Vector2(0, 0), true);

		BodyDef playerBodyDef = new BodyDef();
		BodyDef enemyBodyDef = new BodyDef();
		//playerBodyDef.type = BodyDef.BodyType.KinematicBody; //we are not able to get the mass center of a Kinematic Body :(
		playerBodyDef.type = BodyDef.BodyType.DynamicBody;
		enemyBodyDef.type = BodyDef.BodyType.DynamicBody;
		playerBodyDef.position.set(20, 20);
		enemyBodyDef.position.set(100, 100);

		Body body = world.createBody(playerBodyDef);
		Body body2 = world.createBody(enemyBodyDef);
		player = body;
		enemy = body2;

		PolygonShape square = new PolygonShape();
		float width = spriteRect.getWidth();
		float height = spriteRect.getHeight();
		square.set(new float[] {0,0, width,0, width,height, 0,height});
		//square.set(new float[] {0,height, width,height, width,0, 0,0});

		CircleShape circle = new CircleShape();

		circle.setRadius(6f);

		FixtureDef fixtureDef = new FixtureDef();
		//fixtureDef.shape = circle;
		fixtureDef.shape = square;
		fixtureDef.density = 1f;
		fixtureDef.friction = 0;
		fixtureDef.restitution = 0;

		Fixture fixture = player.createFixture(fixtureDef);
		Fixture fixture1 = enemy.createFixture(fixtureDef);

		circle.dispose();
		square.dispose();

		return world;
	}

	@Override
	public void dispose() {
		// Called at shutdown

		// Graphics and sound resources aren't managed by Java's garbage collector, so
		// they must generally be disposed of manually when no longer needed. But,
		// any remaining resources are typically cleaned up automatically when the
		// application exits, so these aren't strictly necessary here.
		// (We might need to do something like this when loading a new game level in
		// a large game, for instance, or if the user switches to another application
		// temporarily (e.g., incoming phone call on a phone, or something).
		batch.dispose();
		font.dispose();
		spriteImage.dispose();
		bellSound.dispose();
	}

	@Override
	public void render() {
		FPS.add(System.nanoTime() - previousFrameStart);
		previousFrameStart = System.nanoTime();

		ScreenUtils.clear(Color.WHITE);
		camera.update();

		batch.setProjectionMatrix(camera.combined);

		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderLock.lock();

		long renderStartTime = System.nanoTime();
		spriteRect.setPosition(player.getPosition());
		spriteRectEnemy.setPosition(enemy.getPosition());
		Vector2 playerCenter = new Vector2().add(player.getWorldCenter());
		Vector2 enemyCenter = new Vector2().add(enemy.getWorldCenter());

		renderLock.unlock();

		batch.begin();

		font.draw(batch, "fps: " + (int)(1_000_000_000F/FPS.avg()), 10, 80);
		font.draw(batch, "ups: " + (int)(1_000_000_000F/UPS.avg()), 10, 60);
		font.draw(batch, "us/f: " + (int)(FrameTime.avg()/1_000), 10, 40);
		font.draw(batch, "us/u: " + (int)(UpdateTime.avg()/1_000), 10, 20);

		batch.draw(rectSprite, spriteRect.x, spriteRect.y, spriteRect.width, spriteRect.height);
		batch.draw(spriteImage, spriteRect.x, spriteRect.y, spriteRect.width, spriteRect.height);

		batch.draw(rectSpriteEnemy, spriteRectEnemy.x, spriteRectEnemy.y, spriteRectEnemy.width, spriteRectEnemy.height);
		batch.draw(spriteImage, spriteRectEnemy.x, spriteRectEnemy.y, spriteRectEnemy.width, spriteRectEnemy.height);


		batch.draw(new Texture(2,2, Pixmap.Format.RGB888), playerCenter.x, playerCenter.y);
		batch.draw(new Texture(2,2, Pixmap.Format.RGB888), enemyCenter.x, enemyCenter.y);
		batch.end();

		FrameTime.add(System.nanoTime() - renderStartTime);

	}

	//@Override
	public void render2() {

		ScreenUtils.clear(Color.WHITE);

		// Draw calls should be wrapped in batch.begin() ... batch.end()

		long renderStartTime = System.nanoTime();
		renderLock.lock();

		spriteRect.setPosition(player.getPosition());
		spriteRectEnemy.setPosition(enemy.getPosition());

		renderLock.unlock();

        batch.begin();
		font.draw(batch, "us/f: " + (int)(FrameTime.avg()/1_000), 100, 200);
		font.draw(batch, "us/u: " + (int)(UpdateTime.avg()/1_000), 100, 180);

		batch.draw(rectSprite, spriteRect.x, spriteRect.y, spriteRect.width, spriteRect.height);
		batch.draw(spriteImage, spriteRect.x, spriteRect.y, spriteRect.width, spriteRect.height);

		batch.draw(rectSpriteEnemy, spriteRectEnemy.x, spriteRectEnemy.y, spriteRectEnemy.width, spriteRectEnemy.height);
		batch.draw(spriteImage, spriteRectEnemy.x, spriteRectEnemy.y, spriteRectEnemy.width, spriteRectEnemy.height);


		batch.end();

		FrameTime.add(System.nanoTime() - renderStartTime);

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

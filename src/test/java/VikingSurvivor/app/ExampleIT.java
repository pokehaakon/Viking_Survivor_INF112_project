package VikingSurvivor.app;

import static org.junit.jupiter.api.Assertions.*;

import Actors.Enemy.Enemy;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;

import static org.mockito.Mockito.*;

/**
 * Integration tests (example).
 * 
 * (Run using `mvn verify`)
 */
public class ExampleIT {
	/**
	 * Static method run before everything else
	 */

	static ShapeRenderer shapeRenderer;
	static Shape shape;
	static World world;
	@BeforeAll
	static void setUpBeforeAll() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        ApplicationListener listener = new ApplicationListener() {

			@Override
			public void create() {
				world = new World(new Vector2(0,0), true);
				
			}

			@Override
			public void resize(int width, int height) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void render() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void pause() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void resume() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}};
        new HeadlessApplication(listener, config);

        }



	/**
	 * Setup method called before each of the test methods
	 */
	@BeforeEach
	void setUpBeforeEach() {
	}

	/**
	 * Simple test case
	 */
	@Test
	void dummy1() {
		Enemy enemy = new Enemy();
		Shape shapes = new PolygonShape();
		enemy.shape = shapes;
		Texture texture = mock(Texture.class);
		when(texture.getHeight()).thenReturn(10);
		// check that we can find a file using the LibGDX file API
		assertNotNull(Gdx.files.internal("obligator.png"));
		assertEquals(texture.getHeight(),10);

	}
}
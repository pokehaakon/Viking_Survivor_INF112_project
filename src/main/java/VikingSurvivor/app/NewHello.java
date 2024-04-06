package VikingSurvivor.app;

import Tools.GifDecoder;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;


    public class NewHello implements ApplicationListener {
        private SpriteBatch batch;
        private BitmapFont font;
        private Texture grassSprite;
        private Rectangle vikingRect;
        private Rectangle screenRect = new Rectangle();
        private ShapeRenderer shapeRenderer;
        private int screenwidth = 1440;
        private int screenheight = 1080;
        private int startx = screenwidth / 2 - 100;
        private int starty = screenheight / 2 - 100;
        private Animation<TextureRegion> vikingLeft; // New sprite for switching
        private Animation<TextureRegion>  vikingRight; // New sprite for switching
        private Animation<TextureRegion>  vikingIdleRight;
        private Animation<TextureRegion>  vikingIdleLeft;
        //private Animation<TextureRegion>  pickupOrb;
        private boolean isPlayerMoving = false; // Flag to check movement
        private boolean lastMoveRight = true; // To track the direction of the last movement
        private Animation<TextureRegion> currentSprite;
        private float elapsedTime;
        private Texture wolfRight;
        private Rectangle wolfRect;

        Body player;

        World world;

        @Override
        public void create() {
            // Called at startup
            world = new World(new Vector2(0,0),true);
            batch = new SpriteBatch();
            font = new BitmapFont();
            font.setColor(Color.RED);

            //pickupOrb = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("pickupOrb.gif").read());

            vikingRight = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("vikingright.gif").read());
            vikingLeft = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("vikingleft.gif").read());

            vikingIdleRight = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("viking_idle_right.gif").read());
            vikingIdleLeft = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("viking_idle_left.gif").read());

            wolfRight = new Texture(Gdx.files.internal("wolfleft.png"));
            grassSprite = new Texture(Gdx.files.internal("grass.png"));
            currentSprite = vikingRight;

            vikingRect = new Rectangle(startx, starty, 100, 100);
            wolfRect = new Rectangle(700, 700, wolfRight.getWidth()*4, wolfRight.getHeight()*4);


            shapeRenderer = new ShapeRenderer();
            Gdx.graphics.setForegroundFPS(60);

            BodyDef playerDef = new BodyDef();


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
            wolfRight.dispose();
            grassSprite.dispose();
            shapeRenderer.dispose();
        }

        @Override
        public void render() {
            // Called when the application should draw a new frame (many times per second).
            // This is a minimal example – don't write your application this way!
            // Start with a blank screen
            ScreenUtils.clear(Color.WHITE);

            // Draw calls should be wrapped in batch.begin() ... batch.end()
            batch.begin();
            batch.draw(grassSprite, 0, 0, screenwidth, screenheight);
            batch.draw(wolfRight, wolfRect.x, wolfRect.y, wolfRect.width, wolfRect.height);

            // Draw the animation
            elapsedTime += Gdx.graphics.getDeltaTime();
            //set the speed of the animation
            currentSprite.setFrameDuration(0.2f);
            batch.draw(currentSprite.getKeyFrame(elapsedTime), vikingRect.x, vikingRect.y, 500.0f, 500.0f);
            //batch.draw(pickupOrb.getKeyFrame(elapsedTime), 100, 100, 350.0f, 350.0f);

            batch.end();
            movePlayer();

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

        public void movePlayer(){
            if (!isPlayerMoving){
                // If the player is not moving, switch to the idle sprite
                currentSprite = lastMoveRight ? vikingIdleRight : vikingIdleLeft;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                vikingRect.x -= 200 * Gdx.graphics.getDeltaTime();
                isPlayerMoving = true;
                lastMoveRight = false;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                vikingRect.x += 200 * Gdx.graphics.getDeltaTime();
                isPlayerMoving = true;
                lastMoveRight = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                vikingRect.y += 200 * Gdx.graphics.getDeltaTime();
                isPlayerMoving = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                vikingRect.y -= 200 * Gdx.graphics.getDeltaTime();
                isPlayerMoving = true;
            }

            // defining the edge of the map
            if (vikingRect.x < -50) {
                vikingRect.x = -50;
            }
            if (vikingRect.x > screenwidth - vikingRect.width + 63) {
                vikingRect.x = screenwidth - vikingRect.width + 63;
            }
            if (vikingRect.y < 0) {
                vikingRect.y = 0;
            }
            if (vikingRect.y > screenheight - vikingRect.height) {
                vikingRect.y = screenheight - vikingRect.height;
            }

            // Choose the sprite based on movement
            if(isPlayerMoving) {
                // Switch sprites based on the last movement direction
                currentSprite = lastMoveRight ? vikingRight : vikingLeft;
            }


            // Reset the flag after deciding which sprite to use
            isPlayerMoving = false;
        }
    }



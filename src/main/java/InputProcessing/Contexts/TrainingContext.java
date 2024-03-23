//package InputProcessing.Contexts;
//
//import Actors.Enemy.Enemy;
//import Actors.Enemy.EnemyFactory;
//import Actors.Player.Player;
//import Actors.Stats.Stats;
//import InputProcessing.ContextualInputProcessor;
//import InputProcessing.KeyStates;
//import Simulation.EnemyContactListener;
////import Simulation.SimulationThread;
//import Tools.RollingSum;
//import com.badlogic.gdx.*;
//import com.badlogic.gdx.graphics.Camera;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.*;
//import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.ScreenUtils;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
//import static Rendering.Shapes.makeRectangle;
//import static Tools.ShapeTools.createSquareShape;
//import static Tools.ShapeTools.getBottomLeftCorrection;
//
//public class TrainingContext extends Context{
//
//    //    private PlayerExample player;
//
//
//
//    private RollingSum UpdateTime, FrameTime, FPS, UPS;
//    private long previousFrameStart = System.nanoTime();
//
//    private KeyStates keyStates;
//
//    private  EnemyFactory enemyFactory;
//    private float zoomLevel = 1f;
//    Player player;
//    Player ground;
//
//    Body playerBody;
//
//
//
//    private World world;
//    private SpriteBatch batch;
//    private Camera camera;
//
//    private Box2DDebugRenderer debugRenderer;
//
//    private static final int meterToPixels = 32;
//    public TrainingContext(String name, SpriteBatch batch, Camera camera, ContextualInputProcessor iProc) {
//        super(name, iProc);
//        this.batch = batch;
//        this.camera = camera;
//
//        world = new World(new Vector2(0, 0), true);
//        debugRenderer = new Box2DDebugRenderer();
//
//        BodyDef ballDef = new BodyDef();
//        ballDef.type = BodyDef.BodyType.DynamicBody;
//        ballDef.position.set(200,200);
//
//        CircleShape ballShape = new CircleShape();
//        ballShape.setRadius(1);
//
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = ballShape;
//        fixtureDef.density = 1f;
//        fixtureDef.friction = 0;
//        fixtureDef.restitution = 0.75f;
//
//        playerBody = world.createBody(ballDef);
//        Fixture playerFixture = playerBody.createFixture(fixtureDef);
//        player = new Player(playerBody,new Texture(Gdx.files.internal("obligator.png")),0.5f, Stats.player());
//        ballShape.dispose();
//
//        // ground
//
//        BodyDef groundDef = new BodyDef();
//        groundDef.type = BodyDef.BodyType.StaticBody;
//        groundDef.position.set(500,0);
//
//        ChainShape groundShape = new ChainShape();
//        groundShape.createChain(new Vector2[] {new Vector2(-500,200), new Vector2(3000,200)});
//
//        FixtureDef groundFixture = new FixtureDef();
//        groundFixture.density = 1f;
//        groundFixture.friction = 0.5f;
//        groundFixture.restitution = 0;
//        groundFixture.shape = groundShape;
//        Body groundBody = world.createBody(groundDef);
//
//        Fixture groundFix = groundBody.createFixture(groundFixture);
//        ground = new Player(groundBody,new Texture(Gdx.files.internal("obligator.png")),0.5f, Stats.player());
//        groundShape.dispose();
//
//        // box
//
////
////        FixtureDef groundFixture = new FixtureDef();
////        groundFixture.density = 1f;
////        groundFixture.friction = 0.5f;
////        groundFixture.restitution = 0;
////        groundFixture.shape = groundShape;
////        Body groundBody = world.createBody(groundDef);
////        Fixture groundFix = groundBody.createFixture(groundFixture);
////        ground = new Player(groundBody,new Texture(Gdx.files.internal("obligator.png")),0.5f);
////        groundShape.dispose();
//
//        this.setInputProcessor(new InputProcessor() {
//            @Override
//            public boolean keyDown(int i) {
//                Vector2 vel = new Vector2();
//                switch (i) {
//                    case Input.Keys.W:
//                        vel.y =10000;
//                        break;
//
//                    case Input.Keys.S:
//                        vel.y =- 1;
//                        break;
//                    case Input.Keys.A:
//                        vel.x =- 1;
//                        break;
//                    case Input.Keys.D:
//                        vel.x = 1;
//                        break;
//                }
//
//                //vel.setLength(100000000);
//                playerBody.setLinearVelocity(new Vector2(0,-10000));
//
//                return true;
//
//            }
//
//            @Override
//            public boolean keyUp(int i) {
//
//                return false;
//            }
//
//            @Override
//            public boolean keyTyped(char c) {
//                return false;
//            }
//
//            @Override
//            public boolean touchDown(int i, int i1, int i2, int i3) {
//                return false;
//            }
//
//            @Override
//            public boolean touchUp(int i, int i1, int i2, int i3) {
//                return false;
//            }
//
//            @Override
//            public boolean touchCancelled(int i, int i1, int i2, int i3) {
//                return false;
//            }
//
//            @Override
//            public boolean touchDragged(int i, int i1, int i2) {
//                return false;
//            }
//
//            @Override
//            public boolean mouseMoved(int i, int i1) {
//                return false;
//            }
//
//            @Override
//            public boolean scrolled(float v, float v1) {
//                return false;
//            }
//        }
//        );
//    }
//
//
//
//
//
//    @Override
//    public void show() {
//        camera = new OrthographicCamera();
//        world = new World(new Vector2(0, 0), true);
//        debugRenderer = new Box2DDebugRenderer();
//
//        BodyDef ballDef = new BodyDef();
//        ballDef.type = BodyDef.BodyType.DynamicBody;
//        ballDef.position.set(500,500);
//
//        CircleShape ballShape = new CircleShape();
//        ballShape.setRadius(0.9f);
//
//
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = ballShape;
//        fixtureDef.density = 1f;
//        fixtureDef.friction = 0;
//        fixtureDef.restitution = 0.75f;
//
//        //playerBody = world.createBody(ballDef);
//        Fixture playerFixture = playerBody.createFixture(fixtureDef);
//        //player = new Player(playerBody,new Texture(Gdx.files.internal("obligator.png")),0.5f, Stats.player());
//        ballShape.dispose();
//
//
//
//    }
//
//    @Override
//    public void render(float v) {
//        ScreenUtils.clear(Color.WHITE);
//        debugRenderer.render(world, camera.combined);
////
//        playerBody.setLinearVelocity(0,-120);
//
//        world.step(1/60f, 6, 8);
//    }
//
//    @Override
//    public void resize(int i, int i1) {
//
//
//    }
//
//    @Override
//    public void pause() {
//
//    }
//
//    @Override
//    public void resume() {
//
//    }
//
//    @Override
//    public void hide() {
//        dispose();
//
//    }
//
//    @Override
//    public void dispose() {
//        world.dispose();
//
//
//    }
//}

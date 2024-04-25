//package Actors.Enemy;
//
//
//import GameObjects.ObjectTypes.EnemyType;
//import GameObjects.Factories.EnemyFactory;
//import com.badlogic.gdx.ApplicationListener;
//import com.badlogic.gdx.backends.headless.HeadlessApplication;
//import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.World;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//class EnemyFactoryTest {
//    EnemyFactory enemyFactory;
//
//    static World world;
//
//    Texture textureMock;
//
//    //TextureHandler mockTextureHandler;
//    @BeforeEach
//     void setUpBeforeAll() {
//        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
//
//        ApplicationListener listener = new ApplicationListener() {
//
//            @Override
//            public void create() {
//                world = new World(new Vector2(0,0), true);
//
//            }
//
//            @Override
//            public void resize(int width, int height) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void render() {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void pause() {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void resume() {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void dispose() {
//                // TODO Auto-generated method stub
//
//            }};
//        new HeadlessApplication(listener, config);
//
//       enemyFactory = new EnemyFactory();
//
//    }
//
//    @Test
//    void correctSize() {
//        assertEquals(5, enemyFactory.create(5, EnemyType.RAVEN).size());
//    }
//
//
//    @Test
//    void errorHandling() {
//
//        assertThrows(NullPointerException.class, () -> {
//            enemyFactory.create(null);
//        });
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            enemyFactory.create(0, EnemyType.RAVEN);
//        });
//    }
//
//    //to test: correct enemytype, correct position, correct number of enemies etc
//}
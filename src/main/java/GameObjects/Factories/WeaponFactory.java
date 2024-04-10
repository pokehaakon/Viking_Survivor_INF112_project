//package GameObjects.Factories;
//
//import Animations.ActorAnimation;
//import Animations.ActorAnimations;
//import Animations.AnimationConstants;
//import GameObjects.Actors.ObjectTypes.WeaponType;
//import GameObjects.Actors.Player.Player;
//import GameObjects.Actors.Stats.PlayerStats;
//import GameObjects.Actors.Stats.Stats;
//import GameObjects.BodyFeatures;
//import GameObjects.Weapon.Weapon;
//import GameObjects.Weapon.WeaponBody;
//import TextureHandling.GdxTextureHandler;
//import TextureHandling.TextureHandler;
//import Tools.FilterTool;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.physics.box2d.BodyDef;
//import com.badlogic.gdx.physics.box2d.Filter;
//import com.badlogic.gdx.physics.box2d.Shape;
//
//import java.util.List;
//
//import static Tools.FilterTool.createFilter;
//import static Tools.ShapeTools.createSquareShape;
//
//public class WeaponFactory implements IFactory<WeaponBody, WeaponType>{
//    private TextureHandler textureHandler;
//    public WeaponFactory() {
//        textureHandler = new GdxTextureHandler();
//    }
//    @Override
//    public WeaponBody create(WeaponType type) {
//        if(type == null) {
//            throw new NullPointerException("Type cannot be null!");
//        }
//
//        Player player;
//        float scale;
//        Shape shape;
//        String spawnGIF;
//        Texture texture;
//        ActorAnimation animation;
//        PlayerStats stats;
//        BodyFeatures bodyFeatures;
//
//        switch (type) {
//            case PROJECTILE: {
//                scale = AnimationConstants.PLAYER_SCALE;
//                spawnGIF = AnimationConstants.PLAYER_IDLE_RIGHT;
//                texture = textureHandler.loadTexture(spawnGIF);
//                shape = createSquareShape(
//                        (float)(texture.getWidth())*scale,
//                        (float) (texture.getHeight()*scale)
//
//                );
//                animation = ActorAnimations.playerMoveAnimation();
//
//                stats = Stats.player();
//                break;
//            }
//
//            default:
//                throw new IllegalArgumentException("Invalid enemy type");
//        }
//
//        Filter filter = createFilter(
//                FilterTool.Category.BULLET,
//                new FilterTool.Category[]{FilterTool.Category.ENEMY, FilterTool.Category.WALL}
//        );
//
//        bodyFeatures = new BodyFeatures(
//                shape,
//                filter,
//                0,
//                0,
//                0,
//                true,
//                BodyDef.BodyType.DynamicBody);
//
//
//        player = new Player();
//        player.setStats(stats);
//        player.setScale(scale);
//        player.setBodyFeatures(bodyFeatures);
//        player.setSprite(texture);
//        player.setAnimation(animation);
//        player.setType(type);
//
//        return player;
//    }
//
//    @Override
//    public List<WeaponBody> create(int n, WeaponType type) {
//        return null;
//    }
//
//    @Override
//    public void setTextureHandler(TextureHandler newTextureHandler) {
//        textureHandler = newTextureHandler;
//    }
//}

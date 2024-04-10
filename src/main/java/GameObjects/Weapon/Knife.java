//package GameObjects.Weapon;
//
//import GameObjects.Actors.Actor;
//import GameObjects.Actors.ObjectTypes.WeaponType;
//import GameObjects.Actors.Player.Player;
//import GameObjects.BodyFeatures;
//import Tools.FilterTool;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.physics.box2d.BodyDef;
//import com.badlogic.gdx.physics.box2d.Filter;
//import com.badlogic.gdx.physics.box2d.Shape;
//import com.badlogic.gdx.physics.box2d.World;
//
//import java.util.List;
//
//import static Tools.FilterTool.createFilter;
//import static Tools.ShapeTools.createSquareShape;
//
//public class Knife extends Actor<> {
//    int cooldown = 0;
//    Texture sprite = new Texture(Gdx.files.internal("obligator.png"));
//
//
//    public Knife(Player player, List<WeaponBody> projectiles) {
//        super(player, projectiles);
//    }
//
//    @Override
//    public void attack() {
//        if (cooldown++ != 120) return;
//        cooldown = 0;
//
//        addToProjectileList(createKnife());
//    }
//
//    private WeaponBody createKnife() {
//
//        Shape shape = createSquareShape(1,1);
//
//        Filter filter = createFilter(
//                FilterTool.Category.BULLET,
//                new FilterTool.Category[]{FilterTool.Category.ENEMY, FilterTool.Category.WALL}
//        );
//
//        BodyFeatures bodyFeatures = new BodyFeatures(
//                shape,
//                filter,
//                0,
//                0,
//                0,
//                true,
//                BodyDef.BodyType.DynamicBody);
//
//        WeaponBody body = new WeaponBody();
//        body.setBodyFeatures(bodyFeatures);
//        body.setScale(1);
//        body.setSprite(sprite);
//        body.setType(WeaponType.KNIFE);
//        body.addToWorld(player.getBody().getWorld());
//        return body;
//    }
//}

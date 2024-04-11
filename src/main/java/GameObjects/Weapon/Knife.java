package GameObjects.Weapon;

import GameObjects.Actors.ActorAction.ActorAction;
import GameObjects.Actors.Enemy.Enemy;
import GameObjects.Actors.ObjectTypes.WeaponType;
import GameObjects.Actors.Player.Player;
import GameObjects.BodyFeatures;
import GameObjects.SmallPool;
import TextureHandling.TextureHandler;
import Tools.FilterTool;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.List;

import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;
import static Tools.ShapeTools.createSquareShape;

public class Knife extends Weapon {
    private static final ActorAction<WeaponBody> action = (b) -> {
        World world = b.getBody().getWorld();
        Body weaponBody = b.getBody();
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        float minDist = Float.POSITIVE_INFINITY;
        Body lastBody = null;
        for (Body body : bodies) {
            if (!body.isActive()) continue;
            if (body.getType() != BodyDef.BodyType.DynamicBody) continue;
            if (body.getUserData() == null) continue;
            if (!(body.getUserData() instanceof Enemy)) continue;
            Vector2 pos = body.getPosition().cpy();
            pos.sub(weaponBody.getPosition());
            if(minDist < pos.len()) continue;
            minDist = pos.len();
            lastBody = body;
        }
        if (lastBody == null) return;
        Vector2 vel = lastBody.getPosition().sub(weaponBody.getPosition()).setLength(20);
        weaponBody.setLinearVelocity(vel);
    };
    private int cooldown = 0;

    public Knife(Player player, List<WeaponBody> projectiles, TextureHandler textureHandler) {
        super(
                player,
                projectiles,
                new SmallPool<>(player.getBody().getWorld(), () -> {
                    WeaponBody body = createKnife(textureHandler.loadTexture("img_2.png"), player);
                    body.setAction(action);
                    return body;
                }, 1),
                WeaponType.KNIFE,
                (body) -> {
                    body.setPosition(player.getBody().getPosition());
                    //body.getBody().setLinearVelocity(1,1);
                }
        );
    }

    @Override
    public void attack() {
        if (cooldown++ != 120) return;
        cooldown = 0;

        System.out.println("Shot knife");
        addToProjectileList(pool.get());
    }

    static private WeaponBody createKnife(Texture sprite, Player player) {

        //Shape shape = createSquareShape(1,1);
        Shape shape = createCircleShape(1);

        Filter filter = createFilter(
                FilterTool.Category.BULLET,
                new FilterTool.Category[]{FilterTool.Category.ENEMY, FilterTool.Category.WALL}
        );

        BodyFeatures bodyFeatures = new BodyFeatures(
                shape,
                filter,
                0,
                0,
                0,
                true,
                BodyDef.BodyType.DynamicBody);

        WeaponBody body = new WeaponBody();
        body.setBodyFeatures(bodyFeatures);
        body.setScale(1);
        body.setSprite(sprite);
        body.setType(WeaponType.KNIFE);
        body.addToWorld(player.getBody().getWorld());
        return body;
    }
}

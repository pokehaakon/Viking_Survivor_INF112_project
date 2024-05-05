package Tools;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;

public abstract class FilterTool {

    /**
     * The collision categories that a Box2d body can have
     * use '.getMask()' to get the mask that Box2d uses.
     */
    public enum Category {
        //do not make more than 16
        PLAYER,
        ENEMY,
        WALL,
        WEAPON,
        PICKUP,
        BOSS_WEAPON;



        Category() {if (this.ordinal() > 15) {throw new RuntimeException("TOO MANY CATEGORIES, MAX 16!");}}

        /**
         *
         * @return the mask of the category
         */
        public short getMask() {
            return (short) (1 << this.ordinal());
        }
    }

    /**
     * Combines the masks of the categories
     * @param filters array of categories
     * @return the combined masks of the categories
     */
    public static short combineMaskEnums(Category... filters) {
        short mask = 0;
        for(Category m : filters) {
            mask = (short) (mask | m.getMask());
        }

        return mask;
    }

    /**
     * Creates a Box2d collision filter for a body with in the category 'thisIs', colliding with the categories in 'collidesWith'
     * @param thisIs the category of the filter
     * @param collidesWith the categories to collide with
     * @return a filter with the wanted properties
     */
    public static Filter createFilter(Category thisIs, Category... collidesWith) {
        return createFilter(
                thisIs.getMask(),
                combineMaskEnums(collidesWith),
                (short) 0
        );
    }


    /**
     * Creates a Box2d collision filter for a body with in the categories in 'thisIs', colliding with the categories in 'collidesWith'
     * @param thisIs the categories of the filter
     * @param collidesWith the categories to collide with
     * @return a filter with the wanted properties
     */
    public static Filter createFilter(Category[] thisIs, Category[] collidesWith) {
        return createFilter(
                combineMaskEnums(thisIs),
                combineMaskEnums(collidesWith),
                (short) 0
        );
    }

    /**
     * Creates a Box2d collision filter for a body
     * @param category the category bits
     * @param maskBits the mask bits
     * @param groupIndex the group index
     * @return a filter with the wanted properties
     */
    public static Filter createFilter(short category, short maskBits, short groupIndex) {
        Filter filter = new Filter();
        filter.categoryBits = category;
        filter.maskBits = maskBits;
        filter.groupIndex = groupIndex;

        return filter;
    }

    static public boolean isInCategory(Body body, FilterTool.Category category) {
        return (categoryBits(body) & category.getMask()) != 0;
    }

    /**
     * gets the category bits of the body
     * @param body the body to get the category bits from
     * @return the category bits of the body
     */
    public static short categoryBits(Body body) {
        return body.getFixtureList().get(0).getFilterData().categoryBits;
    }
}

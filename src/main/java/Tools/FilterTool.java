package Tools;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Filter;

public abstract class FilterTool {
    public enum Category {
        //do not make more than 16
        PLAYER,
        ENEMY,
        WALL,
        ;

        Category() {if (this.ordinal() > 15) {throw new RuntimeException("TOO MANY CATEGORIES, MAX 16!");}}

        public short getMask() {
            return (short) (1 << this.ordinal());
        }
    }

    public static short combineMaskEnums(Category[] filters) {
        short mask = 0;
        for(Category m : filters) {
            mask = (short) (mask | m.getMask());
        }

        return mask;
    }

    public static Filter createFilter(Category thisIs, Category[] collidesWith) {
        return createFilter(
                thisIs.getMask(),
                combineMaskEnums(collidesWith),
                (short) 0
        );
    }

    public static Filter createFilter(Category[] thisIs, Category[] collidesWith) {
        return createFilter(
                combineMaskEnums(thisIs),
                combineMaskEnums(collidesWith),
                (short) 0
        );
    }

    public static Filter createFilter(short category, short maskBits, short groupIndex) {
        Filter filter = new Filter();
        filter.maskBits = maskBits;
        filter.categoryBits = category;
        filter.groupIndex = groupIndex;

        return filter;
    }
}

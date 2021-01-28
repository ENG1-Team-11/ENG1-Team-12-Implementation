package com.teamonehundred.pixelboat;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents the simple branch obstacle.
 *
 * @author James Frost
 * @author William Walton
 * JavaDoc by Umer Fakher
 */
public class ObstacleBranch extends Obstacle {



    /**
     * A constructor for an Obstacle taking its position (x and y).
     * <p>
     * <p>
     * Branch image is taken by default from C:\...\ENG1-Team-12\Implementation\core\assets.
     * Random rotation is set.
     *
     * @author James Frost
     * @author William Walton
     */
    public ObstacleBranch(int x, int y) {
        super(x, y, 60, 60, "obstacle_branch.png");
        getSprite().setRotation(-90 + (float) Math.random() * 180);
    }

    /**
     * Returns a new collision bounds object for the branch obstacle.
     * <p>
     * <p>
     * Creates a new collision bounds object representing the current position of this branch.
     * See the collision bounds visualisation folder in assets for a visual representation.
     *
     * @return CollisionBounds of branch obstacle
     * @author James Frost
     * @author William Walton
     */
    @Override
    public CollisionBounds getBounds() {
        CollisionBounds my_bounds = new CollisionBounds();
        Rectangle main_rect = new Rectangle(
                getSprite().getX() + (0.31f * getSprite().getWidth()),
                getSprite().getY() + (0.06f * getSprite().getHeight()),
                0.31f * getSprite().getWidth(),
                0.88f * getSprite().getHeight());
        my_bounds.addBound(main_rect);

        my_bounds.setOrigin(new Vector2(
                getSprite().getX() + (getSprite().getWidth() / 2),
                getSprite().getY() + (getSprite().getHeight() / 2)));
        my_bounds.setRotation(getSprite().getRotation());

        return my_bounds;
    }
}

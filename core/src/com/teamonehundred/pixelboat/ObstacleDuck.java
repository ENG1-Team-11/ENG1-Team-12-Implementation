package com.teamonehundred.pixelboat;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents the simple duck obstacle.
 *
 * @author James Frost
 * @author William Walton
 * JavaDoc by Umer Fakher
 */
public class ObstacleDuck extends Obstacle {

    /**
     * A constructor for an Obstacle taking its position (x and y).
     * <p>
     * <p>
     * Duck image is taken by default from C:\...\ENG1-Team-12\Implementation\core\assets.
     * Random rotation is set.
     *
     * @author James Frost
     * @author William Walton
     */
    ObstacleDuck(float x, float y) {
        super(x, y, 30, 30, "obstacle_duck.png");
        // give each duck a random rotation
        getSprite().setOriginCenter();
        getSprite().setRotation((float) Math.random() * 360);
        // have the duck move at a constant speed
        drag = 0;
        speed = .2f;
        rotationSpeed = .2f;
    }

    /**
     * Updates position of duck obstacle and turns by 1 point.
     */
    @Override
    public boolean update(float deltaTime) {
        turn(deltaTime, 5.0f);
        super.update(deltaTime);
        return true;
    }

    /**
     * Returns a new collision bounds object for the duck obstacle.
     * <p>
     * <p>
     * Creates a new collision bounds object representing the current position of this duck.
     * See the collision bounds visualisation folder in assets for a visual representation.
     *
     * @return CollisionBounds of duck obstacle
     * @author James Frost
     * @author William Walton
     */
    @Override
    public CollisionBounds getBounds() {
        CollisionBounds myBounds = new CollisionBounds();
        Rectangle r1 = new Rectangle(
                getSprite().getX() + (0.09f * getSprite().getWidth()),
                getSprite().getY() + (0.13f * getSprite().getHeight()),
                0.41f * getSprite().getWidth(),
                0.4f * getSprite().getHeight());
        Rectangle r2 = new Rectangle(
                getSprite().getX() + (0.5f * getSprite().getWidth()),
                getSprite().getY() + (0.13f * getSprite().getHeight()),
                0.31f * getSprite().getWidth(),
                0.75f * getSprite().getHeight());

        myBounds.addBound(r1);
        myBounds.addBound(r2);

        myBounds.setOrigin(new Vector2(
                getSprite().getX() + (getSprite().getWidth() / 2),
                getSprite().getY() + (getSprite().getHeight() / 2)));
        myBounds.setRotation(getSprite().getRotation());

        return myBounds;
    }
}

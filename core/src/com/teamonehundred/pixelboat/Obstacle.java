package com.teamonehundred.pixelboat;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents the obstacle as an abstract class that extends from movable object
 * and implements the CollisionObject interface.
 *
 * @author James Frost
 * @author William Walton
 * JavaDoc by Umer Fakher
 */
abstract class Obstacle extends MovableObject implements CollisionObject {
    /* ################################### //
                  CONSTRUCTORS
    // ################################### */

    /**
     * A constructor for an Obstacle taking its position (x and y) and width and height.
     * <p>
     * <p>
     * Accepts Texture indirectly through file path. Integer for frame count not needed.
     *
     * @author James Frost
     * @author William Walton
     */
    Obstacle(int x, int y, int w, int h, String texturePath) {
        super(x, y, w, h, texturePath);
    }

    /**
     * A constructor for an Obstacle taking its position (x and y) and width and height.
     * <p>
     * <p>
     * Accepts Texture directly. Integer for frame count needed.
     *
     * @author James Frost
     * @author William Walton
     */
    Obstacle(int x, int y, int w, int h, Texture t, int frameCount) {
        super(x, y, w, h, t, frameCount);
    }

    /* ################################### //
                    METHODS
    // ################################### */

    /**
     * Removes object from game screen once a boat has collided with it.
     *
     * @param other The collision object that this has collided with
     *
     * @author James Frost
     * @author William Walton
     */
    public void hasCollided(CollisionObject other) {
        setIsShown(false);
    }

    /**
     * Get the value of colliding with this object
     * 1.0 is normal (avoid), -1.0 and below is bad (very avoid), and anything above 1.0 is good (aim to get)
     *
     * @return A float representing the value of a collision
     */
    @Override
    public float getCollisionValue() {
        return 0.5f;
    }
}

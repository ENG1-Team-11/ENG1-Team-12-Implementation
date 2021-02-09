package com.teamonehundred.pixelboat;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents a lane wall obstacle.
 *
 * @author James Frost
 * @author William Walton
 * JavaDoc by Umer Fakher
 */
public class ObstacleLaneWall extends Obstacle {
    // Class attributes shared by all instances
    public static final int TEXTURE_HEIGHT = 64;

    /**
     * A constructor for an lane wall obstacle taking its position (x and y).
     * <p>
     * <p>
     * Image is taken by default from C:\...\ENG1-Team-12\Implementation\core\assets.
     *
     * @author James Frost
     * @author William Walton
     */
    ObstacleLaneWall(float x, float y, Texture t) {
        super(x, y, 32, TEXTURE_HEIGHT, t, 2);
        setAnimationFrame(0);
    }

    public void setAnimationFrame(int i) {
        super.setAnimationFrame(i);
    }

    @Override
    public void hasCollided(CollisionObject other) {
        setAnimationFrame(1);
    }

    /**
     * Get the value of colliding with this object
     * 1.0 is normal (avoid), -1.0 and below is bad (very avoid), and anything above 1.0 is good (aim to get)
     *
     * @return A float representing the value of a collision
     */
    @Override
    public float getCollisionValue() {
        return 0.0001f;
    }
}

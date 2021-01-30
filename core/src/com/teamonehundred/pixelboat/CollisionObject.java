package com.teamonehundred.pixelboat;

/**
 * Represents the CollisionObject as an interface.
 *
 * @author James Frost
 * @author William Walton
 * @author Umer Fakher
 */
public interface CollisionObject {
    /**
     * Called when this object collides with something
     * @param other The collision object that this object has collided with
     */
    void hasCollided(CollisionObject other);

    /**
     * Returns a collision bounds object for intersection checking
     */
    CollisionBounds getBounds();

    /**
     * Returns true if the object should be considered for collision checking
     */
    boolean isShown();

    /**
     * Get the value of colliding with this object
     * 1.0 is normal (avoid), -1.0 and below is bad (very avoid), and anything above 1.0 is good (aim to get)
     * @return A float representing the value of a collision
     */
    float getCollisionValue();

}

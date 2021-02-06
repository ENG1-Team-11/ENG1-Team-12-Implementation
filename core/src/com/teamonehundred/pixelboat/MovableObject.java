package com.teamonehundred.pixelboat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents the movable object as an abstract class that extends from game object.
 *
 * @author James Frost
 * @author William Walton
 * JavaDoc by Umer Fakher
 */
public abstract class MovableObject extends GameObject {
    /* ################################### //
                   ATTRIBUTES
    // ################################### */

    protected float maxSpeed = 20.0f;
    protected float speed = 0;
    protected float drag = 2.4f;  // amount speed is reduced by every frame naturally
    protected float acceleration = 7.2f;
    protected float rotationSpeed = 10.0f;

    /* ################################### //
                  CONSTRUCTORS
    // ################################### */

    /**
     * A constructor for MovableObject.
     *
     * @param x           int for horizontal position of object
     * @param y           int for vertical position of object
     * @param w           int for width of object
     * @param h           int for height of object
     * @param texturePath String of object's file path
     */
    public MovableObject(int x, int y, int w, int h, String texturePath) {
        super(x, y, w, h, texturePath);
    }


    /**
     * A constructor for MovableObject.
     *
     * @param x           int for horizontal position of object
     * @param y           int for vertical position of object
     * @param w           int for width of object
     * @param h           int for height of object
     * @param texturePath String of object's file path
     * @param frameCount  int frame count
     */
    public MovableObject(int x, int y, int w, int h, String texturePath, int frameCount) {
        super(x, y, w, h, texturePath, frameCount);
    }

    /**
     * A constructor for MovableObject.
     *
     * @param x          int for horizontal position of object
     * @param y          int for vertical position of object
     * @param w          int for width of object
     * @param h          int for height of object
     * @param t          Direct Texture
     * @param frameCount int frame count
     */
    public MovableObject(int x, int y, int w, int h, Texture t, int frameCount) {
        super(x, y, w, h, t, frameCount);
    }

    /* ################################### //
                    METHODS
    // ################################### */

    /**
     * Rotates the Movable object by some given value.
     * <p>
     * Note: turn left (1) or right (-1)
     *
     * @param amount Integer value that dictates how much the movable object will be rotated
     * @author James Frost
     * @author William Walton
     */
    public void turn(float deltaTime, float amount) {
        getSprite().rotate(amount * rotationSpeed * deltaTime);
    }


    /**
     * Move forwards x, y in whatever direction currently facing.
     *
     * @param distance to be moved
     * @author James Frost
     * @author William Walton
     */
    private void move(float distance) {
        double xRad = Math.toRadians(getSprite().getRotation());
        double yRad = Math.toRadians(getSprite().getRotation());

        float dy = (float) Math.cos(xRad) * distance;
        float dx = (float) Math.sin(yRad) * distance;

        getSprite().translate(-dx, dy);
    }

    /**
     * Updates position of movable object based on speed and decreases speed according to drag calculation.
     *
     * @author James Frost
     * @author William Walton
     */
    public void update(float deltaTime) {
        move(speed);
        speed -= drag * deltaTime;
        speed = Math.max(0.0f, speed);
    }

    /**
     * Increase speed based on current acceleration attribute.
     * <p>
     * If max_speed (terminal velocity) is reached for the movable object then don't increase speed.
     *
     * @author James Frost
     * @author William Walton
     */
    public void accelerate(float deltaTime) {
        speed += acceleration * deltaTime;
        speed = Math.min(maxSpeed, speed);
    }

    /**
     * Resets speed to 0 and rotation to 0.
     */
    public void resetMotion() {
        speed = 0.0f;
        getSprite().setRotation(0);
    }

    // Getter / Setter / Modifier methods for various properties

    /** Get the max speed of the object **/
    public float getMaxSpeed() {
        return maxSpeed;
    }

    /** Set the max speed of the object **/
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /** Change the max speed of the object by delta **/
    public void changeMaxSpeed(float delta) {
        this.maxSpeed += delta;
    }

    /** Get the speed of the object**/
    public float getSpeed() {
        return speed;
    }

    /** Change the speed of the object by delta **/
    public void changeSpeed(float delta) {
        speed += delta;
        speed = Math.min(maxSpeed, Math.max(-maxSpeed, speed));
    }

    /** Set the acceleration of the object **/
    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }


}

package com.teamonehundred.pixelboat;

import com.badlogic.gdx.graphics.Texture;

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

    protected float maxSpeed = 15.0f;
    protected float speed = 0;
    protected float drag = 2.4f;  // amount speed is reduced by every frame naturally
    protected float acceleration = 5.0f;
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
    public void updatePosition(float deltaTime) {
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
        speed = 0;
        getSprite().setRotation(0);
    }

    // Getter / Setter / Modifier methods for various properties

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void changeMaxSpeed(float delta) {
        this.maxSpeed += delta;
    }

    public float getSpeed() {
        return speed;
    }

    public void changeSpeed(float deltaSpeed) {
        speed += deltaSpeed;
        speed = Math.min(maxSpeed, Math.max(-maxSpeed, speed));
    }

    public float getDrag() {
        return drag;
    }

    public void setDrag(float drag) {
        this.drag = drag;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

}

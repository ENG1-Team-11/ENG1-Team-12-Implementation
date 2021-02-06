package com.teamonehundred.pixelboat;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Represents the AI's boat.
 *
 * @author James Frost
 * JavaDoc by Umer Fakher
 */
public class AIBoat extends Boat {
    /* ################################### //
                   ATTRIBUTES
    // ################################### */

    // "Radius" given to objects before AI detects them
    // 40 seems like a good value; don't go below ~20
    final private static float DETECTION_THRESHOLD = 25.0f;
    // Gives the AI a reasonable amount of time to react without being too CPU hungry
    final private static float RAY_RANGE = 480.0f;
    final private static float RAY_RANGE_2 = RAY_RANGE * RAY_RANGE;
    // The separation, in degrees, between the forward and left / right rays
    final private static float RAY_SEPARATION = 25.0f;
    // Using the pigeonhole principle, the smallest obstacle is 30x30 + a 100 pixel
    // "radius", so ~90% of that guarantees we can't miss one
    final private static float RAY_STEP_FACTOR = (30.0f + DETECTION_THRESHOLD * 2.0f) * 0.92f;
    // The multiplier by which the AI turns
    // It's sometimes slow to react, so it has to turn a lot quicker than the player to compensate
    final private static float AI_TURN_FACTOR = 6.0f;
    // Disabling this theoretically improves performance, but because it's Java it doesn't
    // Enable to make the AI search out powerups and always attempt to optimise its route
    final private static boolean AGGRESSIVE_AI = false;

    private final float targetSpeed;

    private boolean regen;


    /* ################################### //
              CONSTRUCTORS
    // ################################### */

    /**
     * Construct a AIBoat object at point (x,y) with default size, texture and animation.
     *
     * @param x int coordinate for the bottom left point of the boat
     * @param y int coordinate for the bottom left point of the boat
     * @param targetSpeed The speed that the boat should aim to go at, as a decimal percentage
     * @author James Frost
     */
    public AIBoat(int x, int y, float targetSpeed) {
        super(x, y);
        this.targetSpeed = targetSpeed * getMaxSpeed();
        regen = false;
    }

    /**
     * Updates position of objects AIBoat based on acceleration and stamina.
     * <p>
     * Checks if AIBoat can turn and updates position accordingly based on any collision objects that may overlap.
     *
     * @param collisionObjects List of Collision Objects
     * @author James Frost
     */
    public void updatePosition(float deltaTime, List<CollisionObject> collisionObjects) {
        // If the boat is not regenerating and below the target speed, accelerate
        if (!regen && getSpeed() < targetSpeed) {
            this.accelerate(deltaTime);
            if (getStamina() <= 0.1) {
                regen = true;
            }
        } else {
            // Otherwise just regenerate up until 50%
            if (getStamina() >= 0.5) {
                regen = false;
            }
        }

        // Check whether to turn or not
        this.checkTurn(deltaTime, collisionObjects);
        super.update(deltaTime);
    }

    /**
     * Returns true if AIBoat should exist on the screen.
     *
     * @return boolean parent isShown
     * @author James Frost
     */
    @Override
    public boolean isShown() {
        return super.isShown();
    }

    /**
     * Return centre coordinates of point where ray is fired.
     *
     * @return Vector2 of coordinates
     * @author James Frost
     */
    private Vector2 getRayFirePoint() {
        Vector2 p = new Vector2(
                getSprite().getX() + getSprite().getWidth() / 2,
                getSprite().getY() + getSprite().getHeight() + 5.0f);

        Vector2 centre = new Vector2(
                getSprite().getX() + (getSprite().getWidth() / 2),
                getSprite().getY() + (getSprite().getHeight() / 2));
        return p.rotateAround(centre, getSprite().getRotation());
    }

    /**
     * Helper function to cast a ray and get the distance to the nearest object
     *
     * @param startX           The x coordinate to start the cast from
     * @param startY           The y coordinate to start the cast from
     * @param angle            The angle to cast the ray at, in degrees clockwise
     * @param collisionObjects The collision objects to check against
     * @return The distance to the nearest object, or ray_range if nothing is nearby
     */
    public float castRay(float startX, float startY, float angle, List<CollisionObject> collisionObjects) {
        // Convert the angle to a normalised gradient to save on trigonometry overhead
        // y/x = tan(angle), therefore y/x = sin(angle)/cos(angle)
        // y = sin(angle), x = cos(angle).  Offset by 90.0f degrees (see graphs)
        float radiansAngle = (float) Math.toRadians(90.0f + angle);
        Vector2 gradient = new Vector2((float) -Math.cos(radiansAngle), (float) Math.sin(radiansAngle));

        for (float distance = 0.0f; distance < RAY_RANGE; distance += RAY_STEP_FACTOR) {
            float xPos = startX + distance * gradient.x;
            float yPos = startY + distance * gradient.y;

            for (CollisionObject collisionObject : collisionObjects) {
                // If the object is hidden, continue
                if (!collisionObject.isShown()) continue;
                // If the object is not an obstacle, continue
                // Assume that all collision objects are also game objects (they are)
                GameObject go = (GameObject) collisionObject;
                float goX = go.getSprite().getX() + go.getSprite().getWidth() * 0.5f;
                float goY = go.getSprite().getY() + go.getSprite().getHeight() * 0.5f;
                // If we're nowhere near the object, move onto the next object
                if (xPos < goX - DETECTION_THRESHOLD) continue;
                if (xPos > goX + DETECTION_THRESHOLD) continue;
                if (yPos < goY - DETECTION_THRESHOLD) continue;
                if (yPos > goY + DETECTION_THRESHOLD) continue;

                // Don't bother with precise collisions, just have the AI be careful
                return Vector2.dst2(xPos, yPos, goX, goY) * collisionObject.getCollisionValue();
            }
        }
        // Return the range, with a bias towards whichever direction is straight forwards
        // We use the squared forms as the above function returns the square distance
        return RAY_RANGE_2 * gradient.y;
    }

    /**
     * Decides the best direction to turn in based on which is closest if any of the ray are negative,
     * otherwise it decides based on which is furthest
     * @param leftRay The distance measured by the left ray cast
     * @param forwardRay The distance measured by the forward ray cast
     * @param rightRay The distance measured by the right ray cast
     * @return The optimal turn direction, as a float between 1.0f and -1.0f
     */
    private float evaluateTurnDirection(float leftRay, float forwardRay, float rightRay) {
        float furthestRay = Math.max(forwardRay, Math.max(leftRay, rightRay));
        float closestRay = Math.min(forwardRay, Math.min(leftRay, rightRay));
        // If the closest ray is greater than zero, there's nothing we urgently need to avoid
        // As such, we instead choose the best location to go to
        if (closestRay > 0.0f) {
            if (leftRay == furthestRay) {
                return AI_TURN_FACTOR;
            }
            if (rightRay == furthestRay) {
                return -AI_TURN_FACTOR;
            }
        }
        // The closest ray is less than zero, so we have to avoid something
        // As such, we choose the least worst location to go to
        else {
            if (leftRay == closestRay) {
                return (furthestRay==forwardRay) ? 0.0f : -AI_TURN_FACTOR;
            }
            if (rightRay == closestRay) {
                return (furthestRay==forwardRay) ? 0.0f : AI_TURN_FACTOR;
            }
            if (forwardRay == closestRay) {
                return (furthestRay==leftRay) ? AI_TURN_FACTOR : -AI_TURN_FACTOR;
            }
        }
        return 0.0f;
    }

    /**
     * Fire three rays from the front of the boat, representing forward, left, and right
     * These will either collide with objects and return the square distance to them, or
     * return a max range.
     * This is then used to calculate which direction to go using evaluateTurnDirection
     *
     * @param collisionObjects List of Collision Objects
     * @author James Frost
     */
    private void checkTurn(float deltaTime, List<CollisionObject> collisionObjects) {
        Vector2 startPoint = getRayFirePoint();

        // Calculate collision of forward ray
        float forwardRay = castRay(startPoint.x, startPoint.y, -getSprite().getRotation(), collisionObjects);
        if (forwardRay > RAY_RANGE_2 && !AGGRESSIVE_AI) return;
        // Sprite rotation is inverted as clockwise is negative..?
        float leftRay = castRay(startPoint.x, startPoint.y, -getSprite().getRotation() - RAY_SEPARATION, collisionObjects);
        float rightRay = castRay(startPoint.x, startPoint.y, -getSprite().getRotation() + RAY_SEPARATION, collisionObjects);

        // Evaluate the best (or least worst) turn direction, then go there
        float turnDirection = evaluateTurnDirection(leftRay, forwardRay, rightRay);
        turn(deltaTime, turnDirection);
    }
}

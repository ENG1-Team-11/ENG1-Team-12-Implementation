package com.teamonehundred.pixelboat;

import com.badlogic.gdx.math.Shape2D;
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

    // Gives the AI a reasonable amount of time to react without being too CPU hungry
    final private static float RAY_RANGE = 140.0f;
    // Using the pigeonhole principle, the smallest obstacle is 30x30, so 29 guarantees we can't skip one
    final private static float RAY_STEP_FACTOR = 29.0f;
    final private static int AI_TURN_FACTOR = 3;

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
    public void updatePosition(List<CollisionObject> collisionObjects) {
        if (!regen && getSpeed() < targetSpeed) {
            this.accelerate();
            if (getStamina() <= 0.1) {
                regen = true;
            }
        } else {
            if (getStamina() >= 0.5) {
                regen = false;
            }
        }

        this.checkTurn(collisionObjects);
        super.updatePosition();
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
    private Vector2 getRayFirePoint(float xOffset) {
        Vector2 p = new Vector2(
                getSprite().getX() + getSprite().getWidth() / 2 + xOffset,
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
                float goX = go.getSprite().getX();
                float goY = go.getSprite().getY();
                // If we're nowhere near the object, move onto the next object
                if (xPos < goX - 100) continue;
                if (xPos > goX + 100) continue;
                if (yPos < goY - 100) continue;
                if (yPos > goY + 100) continue;

                for (Shape2D bound : collisionObject.getBounds().getShapes()) {
                    if (bound.contains(xPos, yPos)) {
                        // Add a factor of the y-gradient so that the boat tends to go straight
                        if (collisionObject instanceof Obstacle)
                            return distance;
                        // The AI should try to go for powerups if it can see one
                        if (collisionObject instanceof Powerup)
                            return -distance * 2.0f;
                    }
                }
            }
        }
        // Return the range, plus a slight bias towards whichever direction is straight forwards
        return RAY_RANGE + gradient.y * 20.0f;
    }

    private int evaluateTurnDirection(float leftRay, float forwardRay, float rightRay) {
        float closestRay = Math.min(forwardRay, Math.min(leftRay, rightRay));
        if (leftRay == closestRay) {
            return (forwardRay > rightRay) ? 0 : -AI_TURN_FACTOR;
        }
        if (rightRay == closestRay) {
            return (forwardRay > leftRay) ? 0 : AI_TURN_FACTOR;
        }
        if (forwardRay == closestRay) {
            return (leftRay > rightRay) ? AI_TURN_FACTOR : -AI_TURN_FACTOR;
        }
        return 0;
    }

    /**
     * Fire a number of rays with limited distance out the front of the boat, select a ray that
     * isn't obstructed by an object, preference the middle (maybe put a preference to side as well)
     * if every ray is obstructed either (keep turning [left or right] on the spot until one is,
     * or choose the one that is obstructed furthest away the second option
     * (choose the one that is obstructed furthest away) is better
     *
     * @param collisionObjects List of Collision Objects
     * @author James Frost
     */
    private void checkTurn(List<CollisionObject> collisionObjects) {
        // Calculate collision of left ray
        Vector2 startPoint = getRayFirePoint(-0.16f * getSprite().getWidth());
        float forwardRayLeft = castRay(startPoint.x, startPoint.y, -getSprite().getRotation(), collisionObjects);

        // Calculate collision of right ray
        startPoint = getRayFirePoint(0.16f * getSprite().getWidth());
        float forwardRayRight = castRay(startPoint.x, startPoint.y, -getSprite().getRotation(), collisionObjects);

        // If closest object is far enough away, keep going straight
        float forwardRay = Math.min(forwardRayLeft, forwardRayRight);
        if (forwardRay == RAY_RANGE) return;

        // Calculate the centre start point
        startPoint = getRayFirePoint(0.0f);
        // Sprite rotation is inverted as clockwise is negative..?
        float leftRay = castRay(startPoint.x, startPoint.y, -getSprite().getRotation() - 35.0f, collisionObjects);
        float rightRay = castRay(startPoint.x, startPoint.y, -getSprite().getRotation() + 35.0f, collisionObjects);

        int turnDirection = evaluateTurnDirection(leftRay, forwardRay, rightRay);
        turn(turnDirection);
    }
}

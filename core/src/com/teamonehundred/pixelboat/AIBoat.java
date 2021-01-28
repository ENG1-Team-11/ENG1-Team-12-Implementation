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
    final private static int AI_TURN_FACTOR = 3 ;

    private boolean regen;


    /* ################################### //
              CONSTRUCTORS
    // ################################### */

    /**
     * Construct a AIBoat object at point (x,y) with default size, texture and animation.
     *
     * @param x int coordinate for the bottom left point of the boat
     * @param y int coordinate for the bottom left point of the boat
     * @author James Frost
     */
    public AIBoat(int x, int y) {
        super(x, y);

        regen = false;
    }

    /**
     * Updates position of objects AIBoat based on acceleration and stamina.
     * <p>
     * Checks if AIBoat can turn and updates position accordingly based on any collision objects that may overlap.
     *
     * @param collision_objects List of Collision Objects
     * @author James Frost
     */
    public void updatePosition(List<CollisionObject> collision_objects) {
        if (!regen && speed < max_speed * 0.99f) {
            this.accelerate();
            if (stamina <= 0.1) {
                regen = true;
            }
        } else {
            if (stamina >= 0.5) {
                regen = false;
            }
        }

        this.check_turn(collision_objects);
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
    private Vector2 get_ray_fire_point(float x_offset) {
        Vector2 p = new Vector2(
                getSprite().getX() + getSprite().getWidth() / 2 + x_offset,
                getSprite().getY() + getSprite().getHeight() + 5.0f);

        Vector2 centre = new Vector2(
                getSprite().getX() + (getSprite().getWidth() / 2),
                getSprite().getY() + (getSprite().getHeight() / 2));
        return p.rotateAround(centre, getSprite().getRotation());
    }

    /**
     * Helper function to cast a ray and get the distance to the nearest object
     *
     * @param start_x           The x coordinate to start the cast from
     * @param start_y           The y coordinate to start the cast from
     * @param angle             The angle to cast the ray at, in degrees clockwise
     * @param collision_objects The collision objects to check against
     * @return The distance to the nearest object, or ray_range if nothing is nearby
     */
    public float cast_ray(float start_x, float start_y, float angle, List<CollisionObject> collision_objects) {
        // Convert the angle to a normalised gradient to save on trigonometry overhead
        // y/x = tan(angle), therefore y/x = sin(angle)/cos(angle)
        // y = sin(angle), x = cos(angle).  Offset by 90.0f degrees (see graphs)
        float radians_angle = (float) Math.toRadians(90.0f + angle);
        Vector2 gradient = new Vector2((float) -Math.cos(radians_angle), (float) Math.sin(radians_angle));

        for (float distance = 0.0f; distance < RAY_RANGE; distance += RAY_STEP_FACTOR) {
            float x_pos = start_x + distance * gradient.x;
            float y_pos = start_y + distance * gradient.y;

            for (CollisionObject collision_object : collision_objects) {
                // If the object is hidden, continue
                if (!collision_object.isShown()) continue;
                // Assume that all collision objects are also game objects (they are)
                GameObject go = (GameObject) collision_object;
                float go_x = go.getSprite().getX();
                float go_y = go.getSprite().getY();
                // If we're nowhere near the object, move onto the next object
                if (x_pos < go_x - 100) continue;
                if (x_pos > go_x + 100) continue;
                if (y_pos < go_y - 100) continue;
                if (y_pos > go_y + 100) continue;

                for (Shape2D bound : collision_object.getBounds().getShapes()) {
                    if (bound.contains(x_pos, y_pos)) {
                        // Add a factor of the y-gradient so that the boat tends to go straight
                        return distance;
                    }
                }
            }
        }
        // Return the range, plus a slight bias towards whichever direction is straight forwards
        return RAY_RANGE + gradient.y * 20.0f;
    }

    private int evaluateTurnDirection(float left_ray, float forward_ray, float right_ray) {
        float closestRay = Math.min(forward_ray, Math.min(left_ray, right_ray));
        if (left_ray == closestRay) {
            return (forward_ray > right_ray) ? 0 : -AI_TURN_FACTOR;
        }
        if (right_ray == closestRay) {
            return (forward_ray > left_ray) ? 0 : AI_TURN_FACTOR;
        }
        if (forward_ray == closestRay) {
            return (left_ray > right_ray) ? AI_TURN_FACTOR : -AI_TURN_FACTOR;
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
     * @param collision_objects List of Collision Objects
     * @author James Frost
     */
    private void check_turn(List<CollisionObject> collision_objects) {
        // Calculate collision of left ray
        Vector2 start_point = get_ray_fire_point(-0.16f * getSprite().getWidth());
        float forward_ray_left = cast_ray(start_point.x, start_point.y, -getSprite().getRotation(), collision_objects);

        // Calculate collision of right ray
        start_point = get_ray_fire_point(0.16f * getSprite().getWidth());
        float forward_ray_right = cast_ray(start_point.x, start_point.y, -getSprite().getRotation(), collision_objects);

        // If closest object is far enough away, keep going straight
        float forward_ray = Math.min(forward_ray_left, forward_ray_right);
        if (forward_ray == RAY_RANGE) return;

        // Calculate the centre start point
        start_point = get_ray_fire_point(0.0f);
        // Sprite rotation is inverted as clockwise is negative..?
        float left_ray = cast_ray(start_point.x, start_point.y, -getSprite().getRotation()  - 35.0f, collision_objects);
        float right_ray = cast_ray(start_point.x, start_point.y, -getSprite().getRotation()  + 35.0f, collision_objects);

        int turnDirection = evaluateTurnDirection(left_ray, forward_ray, right_ray);
        turn(turnDirection);
    }
}

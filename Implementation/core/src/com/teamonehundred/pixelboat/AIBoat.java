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
class AIBoat extends Boat {
    /* ################################### //
                   ATTRIBUTES
    // ################################### */

    protected float number_of_rays;
    protected float ray_angle_range;
    protected float ray_range;
    protected float ray_step_size;
    protected boolean regen;

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
    AIBoat(int x, int y) {
        super(x, y);

        initialise();
    }

    /**
     * Shared initialisation functionality among all constructors.
     * <p>
     * Initialises the ray properties. Rays are used to help the AI control the boat based on visual feedback
     * of its environment i.e. obstacles such as movable obstacles and static lane wall obstacles.
     *
     * @author James Frost
     */
    public void initialise() {
        number_of_rays = 4; // how many rays are fired from the boat
        ray_angle_range = 145; // the range of the angles that the boat will fire rays out at
        ray_range = 30; // the range of each ray
        ray_step_size = (float) 10;
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
        // TODO: Make this a method, and neaten it up
        // TODO: Link Acc w/ turning for better AI (that one may take a bit of time though)
        // TODO: Visible stamina for AI (maybe as debug option)
        if (!regen) {
            this.accelerate();
            if (stamina <= 0.1) {
                regen = true;
            }
        } else {
            if (stamina >= 0.5) {
                regen = false;
            }
        }
        // todo fix this, it takes too long
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
    protected Vector2 get_ray_fire_point() {
        Vector2 p = new Vector2(
                sprite.getX() + (sprite.getWidth() / 2),
                sprite.getY() + (sprite.getHeight()));

        Vector2 centre = new Vector2(
                        sprite.getX() + (sprite.getWidth() / 2),
                        sprite.getY() + (sprite.getHeight() / 2));
        return p.rotateAround(centre, sprite.getRotation());
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
    protected void check_turn(List<CollisionObject> collision_objects) {

        Vector2 start_point = get_ray_fire_point();
        for (int ray = 0; ray <= number_of_rays; ray++) {

            float ray_angle = ((ray_angle_range / number_of_rays) * ray) + sprite.getRotation();

            for (float dist = 0; dist <= ray_range; dist += ray_step_size) {

                double temp_x = (Math.cos(Math.toRadians(ray_angle)) * dist) + (start_point.x);
                double temp_y = (Math.sin(Math.toRadians(ray_angle)) * dist) + (start_point.y);
                //check if there is a collision hull (other than self) at (temp_x, temp_y)
                for (CollisionObject collision_object : collision_objects) {
                    // very lazy way of optimising this code. will break if the collision object isn't an obstacle
                    if (collision_object.isShown() &&
                            ((Obstacle) collision_object).getSprite().getY() > sprite.getY() - 200 &&
                            ((Obstacle) collision_object).getSprite().getY() < sprite.getY() + 200 &&
                            ((Obstacle) collision_object).getSprite().getX() > sprite.getX() - 200 &&
                            ((Obstacle) collision_object).getSprite().getX() < sprite.getX() + 200)
                        for (Shape2D bound : collision_object.getBounds().getShapes()) {
                            if (bound.contains((float) temp_x, (float) temp_y)) {
                                turn(1);
                                return;
                            }
                        }
                }
            }
        }
    }
}

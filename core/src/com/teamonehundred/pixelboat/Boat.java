package com.teamonehundred.pixelboat;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

// generic boat class, never instantiated

/**
 * Base class for all boat types. Contains all functionality for moving, taking damage and collision
 *
 * @author William Walton
 * @author Umer Fakher
 */
public abstract class Boat extends MovableObject implements CollisionObject {
    /* ################################### //
                   ATTRIBUTES
    // ################################### */

    private final List<Integer> legTimes = new ArrayList<>();  // times for every previous leg
    // Accessible by derived classes as they may need to change these
    protected float durability = 1.f;  // from 0 to 1
    protected float durabilityPerHit = .1f;
    protected float maxSpeedPerHit = 1.0f;
    protected float stamina = 1.f;  // from 0 to 1, percentage of stamina max
    protected float staminaUsage = 0.005f;
    protected float staminaRegen = .002f;

    private String name = "default boat name";
    private int currentRaceTime = 0;
    private int timeToAdd = 0;  // ms to add to the end time for this leg. Accumulated by crossing the lines

    private int framesToAnimate = 0;
    private int currentAnimationFrame = 0;
    private int framesElapsed = 0;

    private boolean hasFinishedLeg = false;
    private boolean hasStartedLeg = false;

    /* ################################### //
                  CONSTRUCTORS
    // ################################### */

    //default specs

    /**
     * Construct a Boat object at point (x,y) with default size, texture and animation.
     *
     * @param x int coordinate for the bottom left point of the boat
     * @param y int coordinate for the bottom left point of the boat
     * @author William Walton
     */
    public Boat(float x, float y) {
        super(x, y, 80, 100, "boat.png", 4);
    }

    //specify specs

    /* ################################### //
                    METHODS
    // ################################### */

    /**
     * Function called when this boat collides with another object
     *
     * @param other The collision object that this has collided with
     * @author William Walton
     */
    public void hasCollided(CollisionObject other) {

        // Lane wall isn't most likely, but needs to be handled first otherwise it'll be handled as a regular obstacle
        //noinspection StatementWithEmptyBody (Make IntelliJ go away)
        if (other instanceof ObstacleLaneWall) {
            // Do nothing
        }
        // Obstacle is most likely, so it goes at the top
        else if (other instanceof Obstacle) {
            changeDurability(-durabilityPerHit);
            changeMaxSpeed(-maxSpeedPerHit);
            changeSpeed(-2.0f * maxSpeedPerHit);
        }
        // Powerups are less common
        else if (other instanceof Powerup) {
            Powerup p = (Powerup) other;
            switch (p.getType()) {
                case Repair:
                    changeDurability(durabilityPerHit * 3.0f);
                    changeMaxSpeed(maxSpeedPerHit * 2.0f);
                    break;
                case Boost:
                    changeSpeed(acceleration * 90.0f);
                    break;
                case Stamina:
                    changeStamina(0.5f);
                    break;
                case Time:
                    // Add 5000ms to start time (5s advantage)
                    currentRaceTime -= 5000;
                    break;
                case Teleport:
                    getSprite().translate(0.0f, 250.0f);
                    if (this instanceof PlayerBoat)
                        ((PlayerBoat) this).getCamera().translate(0.0f, 250.0f);
            }
        } else if (other instanceof Boat) {
            changeDurability(-durabilityPerHit);
        }


    }

    /**
     * Function called when the boat accelerates
     *
     * @author William Walton
     */
    @Override
    public void accelerate(float deltaTime) {
        changeStamina(-staminaUsage);
        if (stamina > 0) {
            super.accelerate(deltaTime);
            framesToAnimate += 1;
        }

        if (framesToAnimate > 0) {
            setAnimationFrame(currentAnimationFrame);
            framesElapsed++;
            if (framesElapsed % 15 == 0)
                currentAnimationFrame++;
            framesToAnimate--;
        } else {
            // reset everything
            setAnimationFrame(0);
            currentAnimationFrame = 0;
            framesElapsed = 0;
            framesToAnimate = 0;
        }
    }

    /**
     * Function called every frame when the game updates all objects positions
     *
     * @author William Walton
     */
    @Override
    public boolean update(float deltaTime) {
        super.update(deltaTime);
        changeStamina(staminaRegen);

        // Add to the race time if the boat has not finished
        if (!hasFinishedLeg)
            currentRaceTime += (int) (deltaTime * 1000.0f);
        return true;
    }

    // Getter and Setter methods for attributes

    public void changeDurability(float delta) {
        durability = Math.max(0.0f, Math.min(1.0f, durability + delta));
    }

    public void changeStamina(float delta) {
        stamina = Math.max(0.0f, Math.min(1.0f, stamina + delta));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds the difference between end time and start time into the leg times list as a long value.
     *
     * @author Umer Fakher
     */
    public void setLegTime() {
        this.legTimes.add(currentRaceTime);
    }

    /**
     * Set the time for the leg to a specific value
     *
     * @param time The time to set
     */
    public void setLegTime(int time) {
        this.legTimes.add(time);
    }

    /**
     * Returns recorded leg times of this boat.
     *
     * @return List of Long Returns a list of long types in milliseconds.
     * @author Umer Fakher
     */
    public List<Integer> getLegTimes() {
        return legTimes;
    }

    public void setLegTimes(List<Integer> legTimes) {
        this.legTimes.addAll(legTimes);
    }

    /**
     * Returns the time penalties to be added this boat accumulated by crossing the lines.
     *
     * @return Returns a long time in milliseconds.
     */
    public int getTimeToAdd() {
        return timeToAdd;
    }

    /**
     * Sets the time penalties to be added by this boat accumulated by crossing the lines.
     *
     * @param timeToAdd Recorded long time in milliseconds.
     */
    public void setTimeToAdd(int timeToAdd) {
        this.timeToAdd = timeToAdd;
    }

    public int getCurrentRaceTime() {
        return currentRaceTime;
    }

    public void setCurrentRaceTime(int raceTime) {
        this.currentRaceTime = raceTime;
    }

    /**
     * Checks to see if the this boat has collided with the other CollisionObject object passed.
     *
     * @param object The CollisionObject that will be checked to see if it has hit this boat.
     * @author Umer Fakher
     */
    public void checkCollisions(CollisionObject object) {
        // All CollisionObject's extend GameObject, so we can make this assumption
        GameObject gameObject = (GameObject) object;
        float goX = gameObject.getSprite().getX();
        float goY = gameObject.getSprite().getY();

        // If the boat is nowhere near the object, return early
        if (goY < getSprite().getY() - 200) return;
        if (goY > getSprite().getY() + 200) return;
        if (goX < getSprite().getX() - 200) return;
        if (goX > getSprite().getX() + 200) return;

        if (this.getBounds().isColliding(object.getBounds())) {
            if (object.isShown()) {
                hasCollided(object);
                object.hasCollided(this);
            }
        }
    }

    /**
     * Used to return the CollisionBounds object representing this boat. Used for collision detection
     *
     * @author William Walton
     */
    @Override
    public CollisionBounds getBounds() {
        // create a new collision bounds object representing my current position
        // see the collision bounds visualisation folder in assets for a visual representation
        CollisionBounds myBounds = new CollisionBounds();
        Rectangle mainRect = new Rectangle(
                getSprite().getX() + (0.32f * getSprite().getWidth()),
                getSprite().getY() + (0.117f * getSprite().getHeight()),
                0.32f * getSprite().getWidth(),
                0.77f * getSprite().getHeight());
        myBounds.addBound(mainRect);

        myBounds.setOrigin(new Vector2(
                getSprite().getX() + (getSprite().getWidth() / 2),
                getSprite().getY() + (getSprite().getHeight() / 2)));
        myBounds.setRotation(getSprite().getRotation());

        return myBounds;
    }

    // Getters and Setters for has_started_leg and has_finished_leg

    /**
     * Get whether the boat has finished the leg or not
     **/
    public boolean hasFinishedLeg() {
        return hasFinishedLeg;
    }

    /**
     * Set whether the boat has finished the leg or not
     **/
    public void setHasFinishedLeg(boolean hasFinishedLeg) {
        this.hasFinishedLeg = hasFinishedLeg;
    }

    /**
     * Get whether the boat has started the leg or not
     **/
    public boolean hasStartedLeg() {
        return hasStartedLeg;
    }

    /**
     * Set whether the boat has started the leg or not
     **/
    public void setHasStartedLeg(boolean hasStartedLeg) {
        this.hasStartedLeg = hasStartedLeg;
    }

    /**
     * Reset max_speed, durability and stamina to defaults
     */
    public void reset() {
        setMaxSpeed(20.0f);
        this.durability = 1.0f;
        this.stamina = 1.0f;
    }

    /**
     * Gets current best time for boat from its list of leg_times.
     *
     * @return long time in milliseconds.
     */
    public int getBestTime() {
        int currentBest = -1;

        for (int time : legTimes) {
            if (time > currentBest)
                currentBest = time;
        }

        return currentBest;
    }


    /**
     * Get the boat's current durability
     *
     * @return The boat's durability value, as a decimal percentage between 0.0f and 1.0f
     */
    public float getDurability() {
        return durability;
    }

    /**
     * Get the boat's current stamina
     *
     * @return The boat's stamina value, as a decimal percentage between 0.0f and 1.0f
     */
    public float getStamina() {
        return stamina;
    }

    /**
     * Get the value of colliding with this object
     * 1.0 is normal (avoid), -1.0 and below is bad (very avoid), and anything above 1.0 is good (aim to get)
     *
     * @return A float representing the value of a collision
     */
    @Override
    public float getCollisionValue() {
        return 1.0f;
    }

    /**
     * Adds an amount of time to the penalty time
     * @param time Time to add in ms (probably dt * 1000)
     */
    public void addPenaltyTime(int time) {
        timeToAdd += time;
    }
}

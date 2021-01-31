package com.teamonehundred.pixelboat;

public class Powerup extends MovableObject implements CollisionObject {

    enum Type { Repair, Boost, Stamina, Time, Teleport }
    private final Type powerupType;

    /**
     * A constructor for MovableObject.
     *
     * @param x           int for horizontal position of object
     * @param y           int for vertical position of object
     */
    public Powerup(int x, int y, Type powerupType) {
        super(x, y, 32, 32, getTypeTexture(powerupType));

        this.powerupType = powerupType;
    }

    /**
     * Called when something collides with this
     * @param other The collision object that this object has collided with
     */
    @Override
    public void hasCollided(CollisionObject other) { setIsShown(false); }

    /**
     * Get the powerup type
     * @return The type of powerup, as a Type enum
     */
    public Type getType() { return powerupType; }

    /**
     * Gets the texture filepath associated with a powerup type
     * @param powerupType The type of powerup
     * @return A string representing the filepath where the texture is found
     */
    private static String getTypeTexture(Type powerupType) {
        switch (powerupType) {
            case Repair:
                return "repairPowerup.png";
            case Boost:
                return "boostPowerup.png";
            case Stamina:
                return "staminaPowerup.png";
            case Time:
                return "clockPowerup.png";
            case Teleport:
                return "teleportPowerup.png";
        }
        // Default to error texture
        return "powerup.png";
    }

    /**
     * Get the value of colliding with this object
     * 1.0 is normal (avoid), -1.0 and below is bad (very avoid), and anything above 1.0 is good (aim to get)
     *
     * @return A float representing the value of a collision
     */
    @Override
    public float getCollisionValue() {
        return 500000.0f;
    }
}

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
     * A constructor for MovableObject.
     *
     * @param x           int for horizontal position of object
     * @param y           int for vertical position of object
     * @param w           int for width of object
     * @param h           int for height of object
     * @param texturePath String of object's file path
     * @param frameCount  int frame count
     */
    public Powerup(int x, int y, int w, int h, String texturePath, int frameCount, Type powerupType) {
        super(x, y, w, h, texturePath, frameCount);
        this.powerupType = powerupType;
    }

    @Override
    public void hasCollided(CollisionObject other) { setIsShown(false); }

    public Type getType() { return powerupType; }

    // TODO - Add unique textures to these
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
}

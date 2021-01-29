package com.teamonehundred.pixelboat;

public class Difficulty {

    enum Level { Easy, Medium, Hard }

    // Singleton pattern
    private static Difficulty instance;
    public static Difficulty getInstance() {
        if (instance == null) {
            instance = new Difficulty(Level.Medium);
        }
        return instance;
    }

    Level difficultyLevel;

    private static final float[] BOAT_TARGET_SPEED = {0.9f, 0.97f, 0.99f};
    private static final float[] OBSTACLE_COUNT = { 50, 100, 200 };
    private static final float[] POWER_UP_COUNT = { 50, 25, 25 };

    // Set to private so difficulty can only be obtained as a singleton instance
    private Difficulty(Level difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public void setDifficultyLevel(Level difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public float getBoatTargetSpeed() {
        return BOAT_TARGET_SPEED[difficultyLevel.ordinal()];
    }

    public float getObstacleCount() {
        return OBSTACLE_COUNT[difficultyLevel.ordinal()];
    }

    public float getPowerUpCount() {
        return POWER_UP_COUNT[difficultyLevel.ordinal()];
    }

}

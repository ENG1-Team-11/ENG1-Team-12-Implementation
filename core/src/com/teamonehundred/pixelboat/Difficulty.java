package com.teamonehundred.pixelboat;

public class Difficulty {

    /* Game difficulty settings */
    private static final float[] BOAT_TARGET_SPEED = {0.9f, 0.97f, 0.99f};
    private static final int[] OBSTACLE_COUNT = {50, 100, 200};
    private static final int[] POWER_UP_COUNT = {50, 25, 25};
    private static Difficulty instance;
    private DifficultyLevel difficultyLevel;

    /* Singleton pattern */
    // Set to private so difficulty can only be obtained as a singleton instance
    private Difficulty(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public static Difficulty getInstance() {
        if (instance == null) {
            instance = new Difficulty(DifficultyLevel.Medium);
        }
        return instance;
    }

    /**
     * Get the speed that boat's should be targeting for the current difficulty level
     **/
    public float getBoatTargetSpeed() {
        return BOAT_TARGET_SPEED[difficultyLevel.ordinal()];
    }

    /**
     * Get the obstacle count for the current difficulty level
     **/
    public int getObstacleCount() {
        return OBSTACLE_COUNT[difficultyLevel.ordinal()];
    }

    /**
     * Get the power up count for the current difficulty level
     **/
    public int getPowerUpCount() {
        return POWER_UP_COUNT[difficultyLevel.ordinal()];
    }

    /**
     * Increases the difficulty level, up to a maximum of Hard
     **/
    public void increaseDifficulty() {
        if (difficultyLevel == DifficultyLevel.Easy) {
            setDifficultyLevel(DifficultyLevel.Medium);
        } else if (difficultyLevel == DifficultyLevel.Medium) {
            setDifficultyLevel(DifficultyLevel.Hard);
        }
    }

    /**
     * Decreases the difficulty level, down to a minimum of Easy
     **/
    public void decreaseDifficulty() {
        if (difficultyLevel == DifficultyLevel.Hard) {
            setDifficultyLevel(DifficultyLevel.Medium);
        } else if (difficultyLevel == DifficultyLevel.Medium) {
            setDifficultyLevel(DifficultyLevel.Easy);
        }
    }

    /**
     * Gets the current difficulty level
     **/
    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    /**
     * Set the difficulty level of the game
     **/
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    /**
     * Get the difficulty level as a string
     **/
    @Override
    public String toString() {
        return difficultyLevel.toString();
    }

    /* Difficulty level enum.  Could also be implemented as structs but we only really need 3 */
    public enum DifficultyLevel {Easy, Medium, Hard}
}

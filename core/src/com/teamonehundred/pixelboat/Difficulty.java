package com.teamonehundred.pixelboat;

public class Difficulty {

    enum DifficultyLevel { Easy, Medium, Hard }

    // Singleton pattern
    private static Difficulty instance;
    public static Difficulty getInstance() {
        if (instance == null) {
            instance = new Difficulty(DifficultyLevel.Medium);
        }
        return instance;
    }

    DifficultyLevel difficultyLevel;

    private static final float[] BOAT_TARGET_SPEED = {0.9f, 0.97f, 0.99f};
    private static final float[] OBSTACLE_COUNT = { 50, 100, 200 };
    private static final float[] POWER_UP_COUNT = { 50, 25, 25 };

    // Set to private so difficulty can only be obtained as a singleton instance
    private Difficulty(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
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

    /**
     * Increases the difficulty leve, up to a maximum of Hard
     */
    public void increaseDifficulty() {
        if (difficultyLevel == DifficultyLevel.Easy) {
            setDifficultyLevel(DifficultyLevel.Medium);
        }
        else if (difficultyLevel == DifficultyLevel.Medium) {
            setDifficultyLevel(DifficultyLevel.Hard);
        }
    }

    /**
     * Decreases the difficulty level, down to a minimum of Easy
     */
    public void decreaseDifficulty() {
        if (difficultyLevel == DifficultyLevel.Hard) {
            setDifficultyLevel(DifficultyLevel.Medium);
        }
        else if (difficultyLevel == DifficultyLevel.Medium) {
            setDifficultyLevel(DifficultyLevel.Easy);
        }
    }

    /**
     * Gets the current difficulty level
     * @return The current difficulty level, as a DifficultyLevel enum type
     */
    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    @Override
    public String toString() {
        return difficultyLevel.toString();
    }
}

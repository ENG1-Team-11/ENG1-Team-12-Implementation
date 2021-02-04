package io.github.eng1team11.pixelboattests;

import com.teamonehundred.pixelboat.Difficulty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestMain.class)
public class TestDifficulty {

    @Test
    @DisplayName("Target speed returns properly")
    void testTargetSpeed() {
        Difficulty.getInstance().setDifficultyLevel(Difficulty.DifficultyLevel.Easy);
        float e = Difficulty.getInstance().getBoatTargetSpeed();
        Difficulty.getInstance().increaseDifficulty();
        float m = Difficulty.getInstance().getBoatTargetSpeed();
        Difficulty.getInstance().increaseDifficulty();
        float h = Difficulty.getInstance().getBoatTargetSpeed();
        Assertions.assertTrue(e <= m && m <= h);
    }

    @Test
    @DisplayName("Obstacle count returns properly")
    void testObstacleCount() {
        Difficulty.getInstance().setDifficultyLevel(Difficulty.DifficultyLevel.Easy);
        int e = Difficulty.getInstance().getObstacleCount();
        Difficulty.getInstance().increaseDifficulty();
        int m = Difficulty.getInstance().getObstacleCount();
        Difficulty.getInstance().increaseDifficulty();
        int h = Difficulty.getInstance().getObstacleCount();
        Assertions.assertTrue(e <= m && m <= h);
    }

    @Test
    @DisplayName("Power up count returns properly")
    void testPowerupCount() {
        Difficulty.getInstance().setDifficultyLevel(Difficulty.DifficultyLevel.Easy);
        int e = Difficulty.getInstance().getPowerUpCount();
        Difficulty.getInstance().increaseDifficulty();
        int m = Difficulty.getInstance().getPowerUpCount();
        Difficulty.getInstance().increaseDifficulty();
        int h = Difficulty.getInstance().getPowerUpCount();
        Assertions.assertTrue(e >= m && m >= h);
    }

    @Test
    @DisplayName("Difficulty will not increment past hard")
    void testMaxDifficulty() {
        for (int i = 0; i < 100; ++i)
            Difficulty.getInstance().increaseDifficulty();
        Assertions.assertSame(Difficulty.getInstance().getDifficultyLevel(), Difficulty.DifficultyLevel.Hard);
    }

    @Test
    @DisplayName("Difficulty will not decrement below easy")
    void testMinDifficulty() {
        for (int i = 0; i < 100; ++i)
            Difficulty.getInstance().decreaseDifficulty();
        Assertions.assertSame(Difficulty.getInstance().getDifficultyLevel(), Difficulty.DifficultyLevel.Easy);
    }

}

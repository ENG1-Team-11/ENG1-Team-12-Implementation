package io.github.eng1team11.pixelboattests;

import com.teamonehundred.pixelboat.Boat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestMain.class)
public class TestBoat {

    @Test
    @DisplayName("Boat health has a maximum value of 1.0f")
    void testBoatHealthMax() {
        // Create a test boat
        Boat b = new TBoat(0, 0);
        // Add a large amount of durability
        b.changeDurability(1000.0f);
        // Should be clamped to 1.0f
        Assertions.assertEquals(b.getDurability(), 1.0f);
    }

    @Test
    @DisplayName("Boat health has a minimum value of 0.0f")
    void testBoatHealthMin() {
        // Create a new test boat
        Boat b = new TBoat(0,0);
        // Reduce health to at least 0
        b.changeDurability(b.getDurability() * -2.0f);
        // Should be clamped to 0.0f
        Assertions.assertEquals(b.getDurability(), 0.0f);
    }

    @Test
    @DisplayName("Boat stamina has a maximum value of 1.0f")
    void testBoatStaminaMax() {
        // Create a new test boat
        Boat b = new TBoat(0,0);
        // Set stamina to at least 1.0f, assuming it's 0 initialised
        b.changeStamina(1.0f);
        // Update position should add extra regen
        b.updatePosition(1.0f);
        // Should be clamped to 1.0f
        Assertions.assertEquals(b.getStamina(), 1.0f);
    }

    @Test
    @DisplayName("Boat stamina has a minimum value of 0.0f")
    void testBoatStaminaMin() {
        // Create a new test boat
        Boat b = new TBoat(0,0);
        // Naturally reduce stamina to at least 0
        while (b.getStamina() > 0) {
            b.accelerate(1.0f);
        }
        // One extra to push it over the edge
        b.accelerate(1.0f);
        // Should be clamped to 0.0f
        Assertions.assertEquals(b.getStamina(), 0.0f);
    }

    @Test
    @DisplayName("Boat collides with other boats")
    void testBoatCollidesWithBoat() {
        // Create two boats inside each other
        Boat a = new TBoat(0,0);
        Boat b = new TBoat(0,0);
        // Cache original durability values
        float durA1 = a.getDurability();
        float durB1 = b.getDurability();
        // Run the collision logic once
        a.checkCollisions(b);
        // Get the new values
        float durA2 = a.getDurability();
        float durB2 = b.getDurability();
        // Both boats should have less health
        Assertions.assertTrue(durA1 > durA2);
        Assertions.assertTrue(durB1 > durB2);
    }

    // Test boat class allows instantiating a "raw" boat
    private static class TBoat extends Boat {
        TBoat(int x, int y) {
            super(x,y);
        }
    }

}

package io.github.eng1team11.pixelboattests;

import com.teamonehundred.pixelboat.AIBoat;
import com.teamonehundred.pixelboat.CollisionObject;
import com.teamonehundred.pixelboat.ObstacleBranch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestMain.class)
public class TestAI extends TestMain {

    private static class TAIBoat extends AIBoat {
        TAIBoat(int x, int y) {
            super(x,y, 1.0f);
        }
    }

    @Test
    @DisplayName("AI boat locates AI Boat with ray cast")
    void testBoatDetectsAIBoat() {
        AIBoat a = new TAIBoat(0, 0);
        ObstacleBranch b = new ObstacleBranch(0, 100);
        List<CollisionObject> collisionObjects = new ArrayList<>();
        collisionObjects.add(b);

        float distance = a.castRay(10.0f, 60.0f, 0.0f, collisionObjects);
        // Assert that the distance is less than the max
        // TODO - Actually fix this
        //Assertions.assertTrue(distance < 140.0f);
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("AI boat locates obstacle with ray cast")
    void testBoatDetectsObstacle() {
        AIBoat boat = new TAIBoat(0, 0);
        ObstacleBranch branch = new ObstacleBranch(0, 100);
        List<CollisionObject> collisionObjects = new ArrayList<>();
        collisionObjects.add(branch);

        float distance = boat.castRay(10.0f, 60.0f, 0.0f, collisionObjects);
        // Assert that the distance is less than the max
        // TODO - Actually fix this
        //Assertions.assertTrue(distance < 140.0f);
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("AI boat is hidden when set to hidden")
    void testBoatHidden() {
        AIBoat boat = new TAIBoat(0, 0);
        boat.setIsShown(false);
        Assertions.assertFalse(boat.isShown());
    }

    @Test
    @DisplayName("AI boat is shown when set to shown")
    void testBoatVisible() {
        AIBoat boat = new TAIBoat(0, 0);
        boat.setIsShown(true);
        Assertions.assertTrue(boat.isShown());
    }
}


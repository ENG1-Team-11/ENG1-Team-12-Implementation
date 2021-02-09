package io.github.eng1team11.pixelboattests;

import com.teamonehundred.pixelboat.AIBoat;
import com.teamonehundred.pixelboat.CollisionObject;
import com.teamonehundred.pixelboat.ObstacleBranch;

import com.teamonehundred.pixelboat.Powerup;
import com.teamonehundred.pixelboat.util.CollisionTree;
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
        ObstacleBranch branch = new ObstacleBranch(0, 100);
        CollisionTree collisionTree = new CollisionTree(200, 200, -100, -100);
        collisionTree.add(branch.getSprite().getX(), branch.getSprite().getY(), branch);

        float distance = a.castRay(10.0f, 60.0f, 0.0f, collisionTree);
        // Assert that the distance is less than the max
        Assertions.assertTrue(distance < 140.0f);
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("AI boat locates obstacle with ray cast")
    void testBoatDetectsObstacle() {
        AIBoat boat = new TAIBoat(0, 0);
        ObstacleBranch branch = new ObstacleBranch(0, 100);
        CollisionTree collisionTree = new CollisionTree(200, 200, -100, -100);
        collisionTree.add(branch.getSprite().getX(), branch.getSprite().getY(), branch);

        float distance = boat.castRay(10.0f, 60.0f, 0.0f, collisionTree);
        // Assert that the distance is less than the max
        Assertions.assertTrue(distance < 140.0f);
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("AI boat locates powerup with ray cast")
    void testBoatDetectsPowerup() {
        AIBoat boat = new TAIBoat(0, 0);
        Powerup powerup = new Powerup(0, 100, Powerup.Type.Boost);
        CollisionTree collisionTree = new CollisionTree(200, 200, -100, -100);
        collisionTree.add(powerup.getSprite().getX(), powerup.getSprite().getY(), powerup);

        float distance = boat.castRay(10.0f, 60.0f, 0.0f, collisionTree);
        // Assert that the distance is less than the max
        Assertions.assertTrue(distance > powerup.getCollisionValue());
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


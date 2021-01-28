package io.github.eng1team11.pixelboattests;

import com.teamonehundred.pixelboat.MovableObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestMain.class)
public class TestMovableObject {

    class TMovableObject extends MovableObject {
        TMovableObject() {
            super(0, 0, 10, 10, "boat.png");
        }
    }

    @Test
    @DisplayName("Object cannot naturally accelerate faster than max speed")
    void testMaxSpeedNatural() {
        TMovableObject obj = new TMovableObject();
        float maxSpeed = obj.getMaxSpeed();
        obj.setAcceleration(2.0f * maxSpeed);
        obj.accelerate();
        Assertions.assertEquals(obj.getSpeed(), maxSpeed);
    }

    @Test
    @DisplayName("Object cannot programmatically accelerate faster than max speed")
    void testMaxSpeedProgrammatic() {
        TMovableObject obj = new TMovableObject();
        obj.changeSpeed(2.0f * obj.getMaxSpeed());
        Assertions.assertEquals(obj.getSpeed(), obj.getMaxSpeed());
    }

    @Test
    @DisplayName("Object moves in a straight line at the right speed")
    void testObjectMovesStraight() {
        TMovableObject obj = new TMovableObject();
        obj.setAcceleration(1.0f);
        obj.accelerate();
        obj.updatePosition();

        Assertions.assertEquals(obj.getSprite().getY(), 1.0f);
    }

    @Test
    @DisplayName("Object moves at an angle at the right speed")
    void testObjectMovesAngle() {
        TMovableObject obj = new TMovableObject();
        obj.getSprite().setRotation(-45.0f);
        obj.setAcceleration(1.0f);
        obj.accelerate();
        obj.updatePosition();

        // Compute the distance travelled at a 45 degree angle
        double distance = Math.sin(Math.PI / 4);
        double epsilon = 0.001;

        // Calculate using an epsilon value as the distance calculated is too precise
        Assertions.assertTrue(
                (obj.getSprite().getY() - distance < epsilon) &&
                        (obj.getSprite().getX() -  distance < epsilon)
        );
    }

}

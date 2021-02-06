package io.github.eng1team11.pixelboattests;

import com.badlogic.gdx.math.Rectangle;
import com.teamonehundred.pixelboat.CollisionBounds;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;



@ExtendWith(TestMain.class)
public class TestCollisionBounds {

    @Test
    @DisplayName("AABB collision of rects")
    void testAABB() {
        CollisionBounds cb1 = new CollisionBounds();
        Rectangle r1 = new Rectangle(0,0,10,10);
        cb1.addBound(r1);

        CollisionBounds cb2 = new CollisionBounds();
        Rectangle r2 = new Rectangle(5,5,10,10);
        cb2.addBound(r2);

        Assertions.assertTrue(cb1.isColliding(cb2));
    }

    @Test
    @DisplayName("AABB collision of multiple rects per collider")
    void testAABBMultiple() {
        CollisionBounds cb1 = new CollisionBounds();
        Rectangle r1 = new Rectangle(0,0,10,10);
        Rectangle r12 = new Rectangle(5,5,10,10);
        cb1.addBound(r1);
        cb1.addBound(r12);

        CollisionBounds cb2 = new CollisionBounds();
        Rectangle r2 = new Rectangle(11,11,10,10);
        cb2.addBound(r2);

        Assertions.assertTrue(cb1.isColliding(cb2));
    }

    @Test
    @DisplayName("AABB collision of rotated collider")
    void testCollisionRotation() {
        CollisionBounds cb1 = new CollisionBounds();
        Rectangle r1 = new Rectangle(0,0,10,10);
        cb1.addBound(r1);
        // Because +90 is anticlockwise for some godforsaken reason
        cb1.setRotation(-90.0f);

        CollisionBounds cb2 = new CollisionBounds();
        Rectangle r2 = new Rectangle(5,-5,10,10);
        cb2.addBound(r2);

        Assertions.assertTrue(cb1.isColliding(cb2));
    }

}

package io.github.eng1team11.pixelboattests.util;

import com.teamonehundred.pixelboat.CollisionBounds;
import com.teamonehundred.pixelboat.CollisionObject;
import com.teamonehundred.pixelboat.util.CollisionTree;
import io.github.eng1team11.pixelboattests.TestMain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestMain.class)
public class TestCollisionTree {

    // Non-abstract version of CollisionObject
    private static class TCollisionObject implements CollisionObject {

        public final float x;
        public final float y;

        TCollisionObject(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void hasCollided(CollisionObject other) {

        }

        @Override
        public CollisionBounds getBounds() {
            return null;
        }

        @Override
        public boolean isShown() {
            return false;
        }

        @Override
        public float getCollisionValue() {
            return 0;
        }
    }

    @Test
    @DisplayName("Object added to quadtree properly")
    void testAddToTree() {
        CollisionTree cTree = new CollisionTree(CollisionTree.MIN_WIDTH * 2.0f, CollisionTree.MIN_HEIGHT * 2.0f, 0.0f, 0.0f);
        TCollisionObject obj = new TCollisionObject(CollisionTree.MIN_WIDTH * 1.5f, CollisionTree.MIN_HEIGHT * 1.5f);
        cTree.add(obj.x, obj.y, obj);
        Assertions.assertTrue(cTree.get(obj.x,obj.y).contains(obj));
    }

    @Test
    @DisplayName("Object removed from quadtree successfully")
    void testRemoveFromTree() {
        CollisionTree cTree = new CollisionTree(CollisionTree.MIN_WIDTH * 2.0f, CollisionTree.MIN_HEIGHT * 2.0f, 0.0f, 0.0f);
        TCollisionObject obj = new TCollisionObject(CollisionTree.MIN_WIDTH * 1.5f, CollisionTree.MIN_HEIGHT * 1.5f);
        cTree.add(obj.x, obj.y, obj);
        cTree.remove(obj.x, obj.y, obj);
        Assertions.assertFalse(cTree.get(obj.x,obj.y).contains(obj));
    }

    @Test
    @DisplayName("Default tree is empty")
    void testEmptyTree() {
        CollisionTree cTree = new CollisionTree(CollisionTree.MIN_WIDTH * 2.0f, CollisionTree.MIN_HEIGHT * 2.0f, 0.0f, 0.0f);
        Assertions.assertTrue(cTree.get(0.0f, 0.0f).isEmpty());
    }

    @Test
    @DisplayName("Out of bounds lookup doesn't crash")
    void testOutOfBounds() {
        CollisionTree cTree = new CollisionTree(CollisionTree.MIN_WIDTH * 2.0f, CollisionTree.MIN_HEIGHT * 2.0f, 0.0f, 0.0f);
        Assertions.assertTrue(cTree.get(CollisionTree.MIN_WIDTH * 3.0f, CollisionTree.MIN_HEIGHT * 3.0f).isEmpty());
    }

}

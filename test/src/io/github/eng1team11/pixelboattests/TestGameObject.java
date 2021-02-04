package io.github.eng1team11.pixelboattests;

import com.teamonehundred.pixelboat.GameObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestMain.class)
public class TestGameObject {

    class TGameObject extends GameObject {

        TGameObject() {
            super(0,0,100,100,"boat.png");
        }

    }

    @Test
    @DisplayName("GameObject hidden when requested")
    void testHideObject() {
        GameObject go = new TGameObject();
        go.setIsShown(false);
        Assertions.assertFalse(go.isShown());
    }

    @Test
    @DisplayName("GameObject shown when requested")
    void testShowObject() {
        GameObject go = new TGameObject();
        go.setIsShown(true);
        Assertions.assertTrue(go.isShown());
    }
}

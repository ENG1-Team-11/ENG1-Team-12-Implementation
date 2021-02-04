package io.github.eng1team11.pixelboattests.ui;

import com.teamonehundred.pixelboat.GameObject;

import com.teamonehundred.pixelboat.ui.Button;
import io.github.eng1team11.pixelboattests.TestMain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestMain.class)
public class TestButton {

    @Test
    @DisplayName("Button hover state works when mouse is over button")
    void testHover() {
        final boolean[] r = {false};
        Button button = new Button(0, 0, "ui/options/back.png", "ui/options/back.png", "ui/options/back.png")
        {
            @Override
            protected void onHover() {
                super.onHover();
                r[0] = true;
            }


        };
        button.getSprite().setSize(240, 90);

        button.update(5,5);

        Assertions.assertTrue(r[0]);
    }


}

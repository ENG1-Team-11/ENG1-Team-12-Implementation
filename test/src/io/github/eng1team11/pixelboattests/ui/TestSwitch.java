package io.github.eng1team11.pixelboattests.ui;

import com.teamonehundred.pixelboat.ui.Switch;
import io.github.eng1team11.pixelboattests.TestMain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestMain.class)
public class TestSwitch {

    @Test
    @DisplayName("Switch hover state works when mouse is over button")
    void testHover() {
        final boolean[] r = {false};
        Switch sw = new Switch(0, 0, "ui/options/back.png", "ui/options/back.png", "ui/options/back.png")
        {
            @Override
            protected void onHover() {
                super.onHover();
                r[0] = true;
            }
        };
        sw.getSprite().setSize(240, 90);

        sw.update(5,5);

        Assertions.assertTrue(r[0]);
    }
}

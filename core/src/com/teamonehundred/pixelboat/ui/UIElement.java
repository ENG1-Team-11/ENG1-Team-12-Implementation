package com.teamonehundred.pixelboat.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface UIElement {

    void draw(final SpriteBatch batch);

    void update(final float mouseX, final float mouseY);

}

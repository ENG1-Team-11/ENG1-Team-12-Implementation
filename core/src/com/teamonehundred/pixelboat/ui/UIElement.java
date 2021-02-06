package com.teamonehundred.pixelboat.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * An interface providing the two basic functions that all UI elements should implement, drawing and updating
 */
public interface UIElement {

    /**
     * Draw the UI element
     * @param batch The SpriteBatch to draw the element to
     */
    void draw(final SpriteBatch batch);

    /**
     * Update the UI element
     * @param mouseX The x position of the cursor
     * @param mouseY The y position of the cursor
     */
    void update(final float mouseX, final float mouseY);

}

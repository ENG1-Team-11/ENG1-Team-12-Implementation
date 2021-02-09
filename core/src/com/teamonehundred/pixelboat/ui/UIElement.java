package com.teamonehundred.pixelboat.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * An interface providing the two basic functions that all UI elements should implement, drawing and updating
 */
public abstract class UIElement {

    protected boolean visible = true;

    /**
     * Draw the UI element
     *
     * @param batch The SpriteBatch to draw the element to
     */
    public void draw(final SpriteBatch batch) {}

    /**
     * Update the UI element
     *
     * @param mouseX The x position of the cursor
     * @param mouseY The y position of the cursor
     */
    public void update(final float mouseX, final float mouseY) {}

    /** Set whether the object should be visible and updated or not **/
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    /** Get whether or not the element is visible **/
    public boolean getVisible() {
        return visible;
    }

}

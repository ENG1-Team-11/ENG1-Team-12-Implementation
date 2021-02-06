package com.teamonehundred.pixelboat.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A basic UI image class which allows an image to be drawn in a scene
 */
public class Image implements UIElement {
    private final Sprite sprite;

    /**
     * Construct a new image UI element
     * @param x The x position of the image
     * @param y The y position of the image
     * @param texturePath The path to the image's texture
     */
    public Image(final float x, final float y, final String texturePath) {
        Texture tx = new Texture(Gdx.files.internal(texturePath));
        sprite = new Sprite(tx);
        sprite.setPosition(x,y);
    }

    /** Get the image's sprite **/
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Draw the image
     * @param batch The SpriteBatch to draw the image to
     */
    @Override
    public void draw(final SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * Update the image (does nothing, can be overridden)
     * @param mouseX The x position of the cursor
     * @param mouseY The y position of the cursor
     */
    @Override
    public void update(final float mouseX, final float mouseY) {
        // Do nothing
    }
}

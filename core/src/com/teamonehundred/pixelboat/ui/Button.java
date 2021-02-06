package com.teamonehundred.pixelboat.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A basic UI button class with three states - normal, hovered, and pressed
 * Its callbacks can be overridden to implement custom behaviours
 */
public abstract class Button implements UIElement {

    protected final Sprite sprite;
    protected final Texture hoverTexture;
    protected Texture regularTexture;
    protected Texture pressedTexture;
    protected boolean isPressed;

    /**
     * Constructor for buttons with regular/hovered/pressed states
     * @param x The x position of the button
     * @param y The y position of the button
     * @param texturePath The path to the button's regular texture
     * @param pressedTexturePath The path to the button's texture when pressed
     * @param hoverTexturePath The path to the button's texture when hovered over
     */
    public Button(float x, float y, String texturePath, String pressedTexturePath, String hoverTexturePath) {
        regularTexture = new Texture(Gdx.files.internal(texturePath));
        pressedTexture = new Texture(Gdx.files.internal(pressedTexturePath));
        hoverTexture = new Texture(Gdx.files.internal(hoverTexturePath));
        sprite = new Sprite(regularTexture);
        sprite.setPosition(x, y);

        isPressed = false;
    }

    /**
     * Constructor for buttons that are intended to vanish on press, so a pressed texture is unnecessary
     * @param x The x position of the button
     * @param y The y position of the button
     * @param texturePath The path to the button's regular texture
     * @param hoverTexturePath The path to the button's texture when hovered over
     */
    public Button(float x, float y, String texturePath, String hoverTexturePath) {
        this(x,y,texturePath,hoverTexturePath,hoverTexturePath);
    }

    /**
     * Checks if the mouse is inside the button
     * @param mouseX The x position of the cursor
     * @param mouseY The y position of the cursor
     * @return True if the mouse is inside, false otherwise
     */
    protected boolean isMouseInside(final float mouseX, final float mouseY) {
        if (mouseX > sprite.getX() && mouseX < sprite.getX() + sprite.getWidth()) {
            return mouseY > sprite.getY() && mouseY < sprite.getY() + sprite.getHeight();
        }
        return false;
    }

    /**
     * Update the button, automatically changing states if the user is interacting with the button
     * @param mouseX The x coordinate of the cursor in world space
     * @param mouseY The y coordinate of the cursor in world space
     */
    @Override
    public void update(final float mouseX, final float mouseY) {
        if (isMouseInside(mouseX, mouseY)) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                isPressed = true;
                onPress();
            } else if (isPressed) {
                onRelease();
                isPressed = false;
            } else {
                onHover();
            }
        } else {
            sprite.setTexture(regularTexture);
        }
    }

    /**
     * Draw the button
     * @param batch The SpriteBatch to draw the button to
     */
    @Override
    public void draw(final SpriteBatch batch) {
        sprite.draw(batch);
    }

    /** Get the button's sprite **/
    public Sprite getSprite() {
        return sprite;
    }

    /** Get the button's width **/
    public float getWidth() {
        return sprite.getWidth();
    }

    /** Get the button's height **/
    public float getHeight() {
        return sprite.getHeight();
    }

    /** Called when the button is pressed **/
    protected void onPress() {
        sprite.setTexture(pressedTexture);
    }

    /** Called when the button is released **/
    protected void onRelease() {
        sprite.setTexture(regularTexture);
    }

    /** Called when the button is hovered over **/
    protected void onHover() {
        sprite.setTexture(hoverTexture);
    }

}

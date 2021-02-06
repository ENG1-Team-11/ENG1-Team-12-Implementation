package com.teamonehundred.pixelboat.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * A basic UI switch class which has four states - regular, hovered, on, and off
 * Its callbacks can be overridden to implement custom behaviour
 */
public class Switch extends Button implements UIElement {

    boolean state;

    /**
     * Constructor for switch with regular/hovered/pressed states
     * @param x The x position of the switch
     * @param y The y position of the switch
     * @param texturePath The path to the button's regular texture
     * @param pressedTexturePath The path to the button's texture when pressed
     * @param hoverTexturePath The path to the button's texture when hovered over
     */
    public Switch(final float x, final float y, final String texturePath, final String pressedTexturePath, final String hoverTexturePath) {
        super(x, y, texturePath, pressedTexturePath, hoverTexturePath);
    }

    /**
     * Update the switch, automatically changing states if the user is interacting with it
     * @param mouseX The x coordinate of the cursor in world space
     * @param mouseY The y coordinate of the cursor in world space
     */
    @Override
    public final void update(final float mouseX, final float mouseY) {
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
            if (state)
                sprite.setTexture(pressedTexture);
            else
                sprite.setTexture(regularTexture);
        }
    }

    /** Called when the switch is hovered over **/
    @Override
    protected void onHover() {
        if (state) sprite.setTexture(regularTexture);
        else sprite.setTexture(hoverTexture);
    }

    /** Called when the switch is released **/
    @Override
    protected void onRelease() {
        super.onRelease();
        setState(!state);
        if (state) onStateOn();
        else onStateOff();
    }

    /** Set the state of the switch **/
    void setState(boolean state) {
        this.state = state;
    }

    /** Called when the switch state is set to off **/
    protected void onStateOff() {
    }

    /** Called when the switch state is set to on **/
    protected void onStateOn() {
    }

}

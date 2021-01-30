package com.teamonehundred.pixelboat.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Switch extends Button implements UIElement {

    boolean state;

    public Switch(final float x, final float y, final String texturePath, final String pressedTexturePath, final String hoverTexturePath) {
        super(x, y, texturePath, pressedTexturePath, hoverTexturePath);
    }

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

    @Override
    protected void onHover() {
        if (state) sprite.setTexture(regularTexture);
        else sprite.setTexture(hoverTexture);
    }

    @Override
    protected void onRelease() {
        super.onRelease();
        setState(!state);
        if (state) onStateOn();
        else onStateOff();
    }

    protected boolean getState() {
        return state;
    }

    void setState(boolean state) {
        this.state = state;
    }

    protected void onStateOff() {
    }

    protected void onStateOn() {
    }

}

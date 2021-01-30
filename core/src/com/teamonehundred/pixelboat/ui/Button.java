package com.teamonehundred.pixelboat.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Button implements UIElement {

    protected final Sprite sprite;
    protected final Texture hoverTexture;
    protected Texture regularTexture;
    protected Texture pressedTexture;
    protected boolean isPressed;

    public Button(float x, float y, String texturePath, String pressedTexturePath, String hoverTexturePath) {
        regularTexture = new Texture(Gdx.files.internal(texturePath));
        pressedTexture = new Texture(Gdx.files.internal(pressedTexturePath));
        hoverTexture = new Texture(Gdx.files.internal(hoverTexturePath));
        sprite = new Sprite(regularTexture);
        sprite.setPosition(x, y);

        isPressed = false;
    }

    protected boolean isMouseInside(final float mouseX, final float mouseY) {
        if (mouseX > sprite.getX() && mouseX < sprite.getX() + sprite.getWidth()) {
            return mouseY > sprite.getY() && mouseY < sprite.getY() + sprite.getHeight();
        }
        return false;
    }

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

    @Override
    public void draw(final SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public float getHeight() {
        return sprite.getHeight();
    }

    protected void onPress() {
        sprite.setTexture(pressedTexture);
    }

    protected void onRelease() {
        sprite.setTexture(regularTexture);
    }

    protected void onHover() {
        sprite.setTexture(hoverTexture);
    }

}

package com.teamonehundred.pixelboat.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Image implements UIElement {
    private final Sprite sprite;

    public Image(final float x, final float y, final String texturePath) {
        Texture tx = new Texture(Gdx.files.internal(texturePath));
        sprite = new Sprite(tx);
        sprite.setPosition(x,y);
    }

    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void draw(final SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public void update(final float mouseX, final float mouseY) {
        // Do nothing
    }
}

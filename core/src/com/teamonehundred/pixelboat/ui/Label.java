package com.teamonehundred.pixelboat.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

public class Label implements UIElement {

    private String text;
    private final BitmapFont font;
    private final GlyphLayout glyphLayout;
    private final float x;
    private final float y;
    private final int size;
    private final boolean drawCentreAligned;

    public Label(float x, float y, int size, String text) {
        this(x,y,size, text,false);
    }

    public Label(float x, float y, int size, String text, boolean drawCentreAligned) {
        font = new BitmapFont();
        glyphLayout = new GlyphLayout();

        this.x = x;
        this.y = y;

        this.drawCentreAligned = drawCentreAligned;

        this.size = size;
        font.getData().setScale(size);
        setText(text);

    }

    void drawCentreAligned(SpriteBatch batch) {
        font.draw(batch, glyphLayout, x - glyphLayout.width / 2, y );
    }

    void drawLeftAligned(SpriteBatch batch) {
        font.draw(batch, glyphLayout, x, y);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (drawCentreAligned)
            drawCentreAligned(batch);
        else
            drawLeftAligned(batch);
    }

    @Override
    public void update(float mouseX, float mouseY) {

    }

    public void setText(String text) {
        this.text = text;
        glyphLayout.setText(font, text);

    }
}

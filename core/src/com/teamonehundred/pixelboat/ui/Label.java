package com.teamonehundred.pixelboat.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A basic UI label class which allows text to be drawn to the screen
 * either centre or left aligned
 */
public class Label extends UIElement {

    private final BitmapFont font;
    private final GlyphLayout glyphLayout;
    private final float x;
    private final float y;
    private final boolean drawCentreAligned;

    /**
     * Construct a new label UI element
     *
     * @param x                 The x position of the label
     * @param y                 The y position of the label
     * @param size              The size of the text as a decimal percentage of 72px
     * @param text              The text the label should show
     * @param drawCentreAligned Whether the x position is the centre of the label or the left
     */
    public Label(float x, float y, float size, String text, boolean drawCentreAligned) {
        font = new BitmapFont(Gdx.files.internal("ui/Segoe UI.fnt"));
        glyphLayout = new GlyphLayout();

        this.x = x;
        this.y = y;

        this.drawCentreAligned = drawCentreAligned;


        font.getData().setScale(size);
        setText(text);

    }

    /**
     * Draws the label centre-aligned, accounting for its width
     **/
    private void drawCentreAligned(SpriteBatch batch) {
        font.draw(batch, glyphLayout, x - glyphLayout.width / 2, y);
    }

    /**
     * Draws the label left-aligned
     **/
    private void drawLeftAligned(SpriteBatch batch) {
        font.draw(batch, glyphLayout, x, y);
    }

    /**
     * Draw the label
     *
     * @param batch The SpriteBatch to draw the label to
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (!visible) return;
        // 6 years of doing C++ and I can't figure out function pointers
        // in a language with reflection and metaprogramming
        // I don't like Java =(
        if (drawCentreAligned)
            drawCentreAligned(batch);
        else
            drawLeftAligned(batch);
    }

    /**
     * Set the text that the label should show
     **/
    public void setText(String text) {
        glyphLayout.setText(font, text);

    }
}

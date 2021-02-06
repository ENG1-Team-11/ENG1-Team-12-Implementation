package com.teamonehundred.pixelboat.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Label implements UIElement {

    private final BitmapFont font;
    private final GlyphLayout glyphLayout;
    private final float x;
    private final float y;
    private final boolean drawCentreAligned;

    public Label(float x, float y, float size, String text) {
        this(x,y,size, text,false);
    }

    public Label(float x, float y, float size, String text, boolean drawCentreAligned) {
        font = new BitmapFont(Gdx.files.internal("ui/Segoe UI.fnt"));
        glyphLayout = new GlyphLayout();

        this.x = x;
        this.y = y;

        this.drawCentreAligned = drawCentreAligned;

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
        glyphLayout.setText(font, text);

    }
}

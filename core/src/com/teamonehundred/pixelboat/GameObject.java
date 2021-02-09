package com.teamonehundred.pixelboat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Represents a game object.
 * <p>
 * The GameObject class that everything visible in the game is derived from.
 * Contains texture and positional information.
 *
 * @author James Frost
 * @author William Walton
 * JavaDoc by Umer Fakher
 */
public abstract class GameObject {
    /* ################################### //
                   ATTRIBUTES
    // ################################### */

    // coordinates are world coordinates relative to the bottom left of object
    // rotation is in degrees
    // width, height, x, y, and rotation are stored in sprite

    /**
     * Stores the image rendered when the object is shown.
     */
    private final Texture texture;
    /**
     * Stores the texture of the object and positional information (x and y coordinates, width and height, rotation)
     */
    private final Sprite sprite;
    /**
     * The array of frames used for animations, stored as TextureRegion s
     */
    protected TextureRegion[] animationRegions;

    // set to null if not animated
    /**
     * Used to determine if the object should be rendered or not. Also used in collision detection
     */
    private Boolean isShown;

    /* ################################### //
                  CONSTRUCTORS
    // ################################### */

    /**
     * A constructor for GameObject for static textures.
     *
     * @param x           int for horizontal position of object
     * @param y           int for vertical position of object
     * @param w           int for width of object
     * @param h           int for height of object
     * @param texturePath String of object's file path
     */
    public GameObject(float x, float y, int w, int h, final String texturePath) {
        texture = new Texture(texturePath);
        isShown = true;

        animationRegions = null;

        sprite = new Sprite(texture);
        sprite.setPosition(x, y);
        sprite.setSize(w, h);
        sprite.setOriginCenter();
    }

    // animation

    /**
     * A constructor for GameObject for animation textures.
     *
     * @param x           int for horizontal position of object
     * @param y           int for vertical position of object
     * @param w           int for width of object
     * @param h           int for height of object
     * @param texturePath String of object's file path
     * @param frameCount  int frame count
     */
    public GameObject(float x, float y, int w, int h, final String texturePath, int frameCount) {
        texture = new Texture(texturePath);
        isShown = true;

        animationRegions = new TextureRegion[frameCount];
        float textureWidth = 1f / (frameCount);
        for (int i = 0; i < frameCount; i++) {
            animationRegions[i] = new TextureRegion(texture, i * textureWidth, 0f, (i + 1) * textureWidth, 1f);
        }

        sprite = new Sprite(animationRegions[0]);
        sprite.setPosition(x, y);
        sprite.setSize(w, h);
        sprite.setOriginCenter();
    }

    /**
     * A constructor for GameObject.
     *
     * @param x          int for horizontal position of object
     * @param y          int for vertical position of object
     * @param w          int for width of object
     * @param h          int for height of object
     * @param texture    Direct Texture
     * @param frameCount int frame count
     */
    public GameObject(float x, float y, int w, int h, Texture texture, int frameCount) {
        this.texture = texture;
        isShown = true;

        animationRegions = new TextureRegion[frameCount];
        float textureWidth = 1f / (frameCount);
        for (int i = 0; i < frameCount; i++) {
            animationRegions[i] = new TextureRegion(texture, i * textureWidth, 0f, (i + 1) * textureWidth, 1f);
        }

        sprite = new Sprite(animationRegions[0]);
        sprite.setPosition(x, y);
        sprite.setSize(w, h);
        sprite.setOriginCenter();
    }

    /**
     * Destructor disposes of this texture once it is no longer referenced.
     */
    protected void finalize() {
        texture.dispose();
    }

    /* ################################### //
                    METHODS
    // ################################### */

    /**
     * Returns true if GameObject should be shown otherwise false.
     *
     * @return is_shown boolean
     */
    public boolean isShown() {
        return isShown;
    }

    /**
     * Set whether or not the object is shown
     *
     * @param state A booleam where true means the object should be shown
     */
    public void setIsShown(boolean state) {
        isShown = state;
    }

    /**
     * Getter for GameObject sprite.
     *
     * @return Sprite
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Returns a new collision bounds object for the GameObject.
     * <p>
     * <p>
     * Creates a new collision bounds object representing the current position of this GameObject.
     * See the collision bounds visualisation folder in assets for a visual representation.
     *
     * @return CollisionBounds object of GameObject
     * @author James Frost
     * @author William Walton
     */
    public CollisionBounds getBounds() {
        CollisionBounds myBounds = new CollisionBounds();
        Rectangle mainRect = sprite.getBoundingRectangle();  // default is to use whole sprite
        myBounds.addBound(mainRect);
        return myBounds;
    }

    /**
     * Sets GameObject sprite's animation region according to integer passed in as long as the animation
     * regions is not null.
     *
     * @param i int
     */
    public void setAnimationFrame(int i) {
        if (animationRegions != null)
            sprite.setRegion(animationRegions[i % animationRegions.length]);
    }
}

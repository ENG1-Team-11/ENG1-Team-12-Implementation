package com.teamonehundred.pixelboat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the user player's boat.
 *
 * @author James Frost
 * @author William Walton
 * JavaDoc by Umer Fakher
 */
class PlayerBoat extends Boat {
    /* ################################### //
                   ATTRIBUTES
    // ################################### */

    private final OrthographicCamera camera;

    private final Texture stamina_texture;
    private final Texture durability_texture;

    private final Sprite stamina_bar;
    private final Sprite durability_bar;

    private final int ui_bar_width = 500;

    /* ################################### //
                  CONSTRUCTORS
    // ################################### */

    /**
     * Construct a PlayerBoat object at point (x,y) with default size, texture and animation.
     *
     * @param x int coordinate for the bottom left point of the boat
     * @param y int coordinate for the bottom left point of the boat
     * @author William Walton
     */
    PlayerBoat(int x, int y) {
        super(x, y);

        stamina_texture = new Texture("stamina_texture.png");
        durability_texture = new Texture("durability_texture.png");

        stamina_bar = new Sprite(stamina_texture);
        durability_bar = new Sprite(durability_texture);

        stamina_bar.setSize(ui_bar_width, 10);
        durability_bar.setSize(ui_bar_width, 10);


        stamina_bar.setPosition(-ui_bar_width / 2.0f, 5);
        durability_bar.setPosition(-ui_bar_width / 2.0f, 20);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0, Gdx.graphics.getHeight() / 3.0f, 0);
        camera.update();
    }

    /**
     * Destructor disposes of this texture once it is no longer referenced.
     */
    protected void finalize() {
        super.finalize();
        stamina_texture.dispose();
        durability_texture.dispose();
    }

    /* ################################### //
                    METHODS
    // ################################### */

    /**
     * Sets the spec type of boat.
     * <p>
     * Can be in these states:
     * - debug
     * - default
     * - fast low durability
     *
     * @param spec_id int for boat spec
     */
    public void setSpec(int spec_id) {
        switch (spec_id) {
            case 0:
                // debug
                stamina_usage = 0f;
                durability_per_hit = 0f;
                break;
            case 1:
                // default
                break;
            case 2:
                // fast low durability
                max_speed = 20;
                durability_per_hit = .2f;
            default:
                break;
        }
    }

    /**
     * Updates the position based on the user's input.
     * <p>
     * 'W' key accelerates the boat.
     * 'A' Turns the boat to the left
     * 'D' Turns the boat to the right
     * <p>
     * Updates the x and y position of the sprite with new x and y according to which input has been requested.
     * The camera will follow the player's boat
     *
     * @author William Walton
     */
    @Override
    public void updatePosition() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.accelerate();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.turn(1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.turn(-1);
        }

        float old_x = getSprite().getX();
        float old_y = getSprite().getY();

        super.updatePosition();

        // only follow player in x axis if they go off screen
        float dx = Math.abs(getSprite().getX()) > Gdx.graphics.getWidth() / 3.0f ? getSprite().getX() - old_x : 0;
        float dy = getSprite().getY() - old_y;

        // move camera to follow player
        camera.translate(dx, dy, 0);
    }

    /**
     * Returns the all sprites for PlayerBoat UI.
     * <p>
     * This includes the stamina bar and durability bar.
     *
     * @return List of Sprites
     */
    public List<Sprite> getUISprites() {
        updateUISprites();

        List<Sprite> ret = new ArrayList<>();
        ret.add(stamina_bar);
        ret.add(durability_bar);
        return ret;
    }

    /**
     * Getter for PlayerBoat Camera.
     *
     * @return OrthographicCamera
     */
    public OrthographicCamera getCamera() {
        return camera;
    }

    /**
     * Get the width of the UI bar
     * @return The width of the UI bar, as a float
     */
    public float getUiBarWidth() { return ui_bar_width; }

    /**
     * Resets PlayerBoat Camera position
     */
    public void resetCameraPos() {
        camera.position.set(getSprite().getX(), Gdx.graphics.getHeight() / 3.0f, 0);
        camera.update();
    }

    /**
     * Update the position and size of the UI elements (e.g. stamina bar and durability bar) according to their values.
     * <p>
     * The stamina decreases as player requests the boat to row and move. It increases when this is not the case.
     * Durability decreases according to the collisions with other obstacles.
     * Dynamically updates the size of the stamina bar and durability bar
     * based on the PlayerBoat attributes as they change.
     */
    private void updateUISprites() {
        stamina_bar.setPosition(-ui_bar_width / 2.0f + getSprite().getX() + getSprite().getWidth() / 2, -50 + getSprite().getY());
        durability_bar.setPosition(-ui_bar_width / 2.0f + getSprite().getX() + getSprite().getWidth() / 2, -35 + getSprite().getY());

        stamina_bar.setSize(ui_bar_width * stamina, 10.0f);
        durability_bar.setSize(ui_bar_width * durability, 10.0f);
    }

}

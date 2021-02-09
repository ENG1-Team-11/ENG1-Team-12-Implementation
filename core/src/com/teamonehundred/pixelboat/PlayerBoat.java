package com.teamonehundred.pixelboat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
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
public class PlayerBoat extends Boat {
    /* ################################### //
                   ATTRIBUTES
    // ################################### */

    private final static int UI_BAR_WIDTH = 500;
    private final static float FORWARD_LOCK_TIME = 1.0f;
    private final OrthographicCamera camera;
    private final Texture staminaTexture;
    private final Texture durabilityTexture;
    private final Sprite staminaBar;
    private final Sprite durabilityBar;
    private int specID;
    // Used to stop the player mashing W to game the acceleration system
    private float accelerationCooldown;
    private boolean forwardPressed;
    private boolean forwardLocked;

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
    PlayerBoat(float x, float y) {
        super(x, y);

        accelerationCooldown = 0;
        forwardPressed = false;
        forwardLocked = false;

        staminaTexture = new Texture("stamina_texture.png");
        durabilityTexture = new Texture("durability_texture.png");

        staminaBar = new Sprite(staminaTexture);
        durabilityBar = new Sprite(durabilityTexture);

        staminaBar.setSize(UI_BAR_WIDTH, 10);
        durabilityBar.setSize(UI_BAR_WIDTH, 10);


        staminaBar.setPosition(-UI_BAR_WIDTH / 2.0f, 5);
        durabilityBar.setPosition(-UI_BAR_WIDTH / 2.0f, 20);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0, Gdx.graphics.getHeight() / 3.0f, 0);
        camera.update();
    }

    /**
     * Destructor disposes of this texture once it is no longer referenced.
     */
    protected void finalize() {
        super.finalize();
        staminaTexture.dispose();
        durabilityTexture.dispose();
    }

    /* ################################### //
                    METHODS
    // ################################### */

    /**
     * Get the player boat specification ID
     **/
    public int getSpec() {
        return specID;
    }

    /**
     * Sets the spec type of boat.
     * <p>
     * Can be in these states:
     * - debug
     * - default
     * - fast low durability
     *
     * @param specID int for boat spec
     */
    public void setSpec(int specID) {
        this.specID = specID;
        switch (specID) {
            case 0:
                // debug
                staminaUsage = 0f;
                durabilityPerHit = 0f;
                break;
            case 1:
                setMaxSpeed(20.0f);
                durabilityPerHit = 0.1f;
                break;
            case 2:
                // fast low durability
                setMaxSpeed(25.0f);
                durabilityPerHit = 0.2f;
                staminaRegen = 0.0015f;
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
     */
    @Override
    public boolean update(float deltaTime) {
        // If movement is unlocked or the forward key is held down...
        if (!forwardLocked || forwardPressed) {
            // If the key is still held, accelerate and lock forward
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                accelerate(deltaTime);
                forwardPressed = true;
                forwardLocked = true;
            }
            // If the key is released, mark the key as not pressed and update the acceleration cooldown
            else {
                forwardPressed = false;
                accelerationCooldown = FORWARD_LOCK_TIME;
            }
        }
        // If movement is not unlocked and the forward key is not held down
        else {
            // Subtract deltaTime from the acceleration cooldown, limited to 0.0f-infinity
            accelerationCooldown = Math.max(0.0f, accelerationCooldown - deltaTime);
            // If the acceleration cooldown is less than epsilon, unlock foward
            if (accelerationCooldown < 0.001f) {
                forwardLocked = false;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
            setMaxSpeed(200.0f);
            changeSpeed(10.0f);
        }

        // If A or D are pressed, turn left or right respectively
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.turn(deltaTime, 15.0f);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.turn(deltaTime, -15.0f);
        }

        float oldX = getSprite().getX();
        float oldY = getSprite().getY();

        super.update(deltaTime);

        // only follow player in x axis if they go off screen
        float dx = Math.abs(getSprite().getX()) > Gdx.graphics.getWidth() / 3.0f ? getSprite().getX() - oldX : 0;
        float dy = getSprite().getY() - oldY;

        // move camera to follow player
        camera.translate(dx, dy, 0);
        return true;
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
        ret.add(staminaBar);
        ret.add(durabilityBar);
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
     *
     * @return The width of the UI bar, as a float
     */
    public float getUiBarWidth() {
        return UI_BAR_WIDTH;
    }

    /**
     * Resets PlayerBoat Camera position
     */
    public void resetCameraPos() {
        camera.position.set(getSprite().getX(), Gdx.graphics.getHeight() / 3.0f, 0);
        camera.update();
    }

    /**
     * Reset max_speed, durability and stamina to defaults
     */
    @Override
    public void reset() {
        super.reset();
        accelerationCooldown = 0;
        forwardLocked = false;
        forwardPressed = false;
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
        staminaBar.setPosition(-UI_BAR_WIDTH / 2.0f + getSprite().getX() + getSprite().getWidth() / 2, -50 + getSprite().getY());
        durabilityBar.setPosition(-UI_BAR_WIDTH / 2.0f + getSprite().getX() + getSprite().getWidth() / 2, -35 + getSprite().getY());

        staminaBar.setSize(UI_BAR_WIDTH * stamina, 10.0f);
        if (forwardLocked) staminaBar.setColor(Color.RED);
        else staminaBar.setColor(Color.YELLOW);
        durabilityBar.setSize(UI_BAR_WIDTH * durability, 10.0f);
    }
}

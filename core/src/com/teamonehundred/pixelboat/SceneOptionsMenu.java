package com.teamonehundred.pixelboat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Represents the Options Menu Scene for when the player wants to select/edit the options before the race starts.
 *
 * @author William Walton
 * JavaDoc by Umer Fakher
 */
public class SceneOptionsMenu implements Scene {
    private final int sceneID = 2;
    private final Texture fullCheckYes;
    private final Texture fullCheckNo;
    private final Texture back;
    private final Texture backHovered;
    private final Sprite bgSprite;
    private final Sprite fullSprite;
    private final Sprite fullCheckSprite;
    private final Sprite leftSprite;
    private final Sprite rightSprite;
    private final Sprite backSprite;
    private final Sprite accelSprite;
    private final Viewport fillViewport;
    private final OrthographicCamera fillCamera;
    private boolean isFullscreen = false;

    /**
     * Main constructor for a SceneOptionsMenu.
     * <p>
     * Initialises a Scene textures for Options Menu and camera.
     * When options are hovered over they will change texture indicating what the user is about to select.
     *
     * @author William Walton
     */
    public SceneOptionsMenu() {
        fillCamera = new OrthographicCamera();
        fillViewport = new FillViewport(1280, 720, fillCamera);
        fillViewport.apply();
        fillCamera.position.set(fillCamera.viewportWidth / 2, fillCamera.viewportHeight / 2, 0);
        fillViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Texture full = new Texture("options_menu_fullscreen.png");
        Texture accel = new Texture("options_menu_fullscreen.png");
        Texture left = new Texture("options_menu_fullscreen.png");
        Texture bg = new Texture("start_screen.png");
        Texture right = new Texture("options_menu_fullscreen.png");
        back = new Texture("options_menu_back.png");
        backHovered = new Texture("options_menu_back_hovered.png");
        fullCheckYes = new Texture("options_menu_checkbox_yes.png");
        fullCheckNo = new Texture("options_menu_checkbox_no.png");

        bgSprite = new Sprite(bg);
        bgSprite.setPosition(0, 0);
        bgSprite.setSize(1280, 720);

        fullSprite = new Sprite(full);
        fullCheckSprite = new Sprite(fullCheckNo);
        fullSprite.setSize(256.0f, 64.0f);
        fullSprite.setPosition((fillCamera.viewportWidth / 2) - (fullSprite.getWidth()), (Gdx.graphics.getHeight() / 2.0f) + (fullSprite.getHeight() * 1.5f));
        fullCheckSprite.setSize(64.0f, 64.0f);
        fullCheckSprite.setPosition((fillCamera.viewportWidth / 2) + (fullSprite.getWidth() / 2), (Gdx.graphics.getHeight() / 2.0f) + (fullSprite.getHeight() * 1.5f));

        accelSprite = new Sprite(accel);
        accelSprite.setSize(256.0f, 64.0f);
        accelSprite.setPosition((fillCamera.viewportWidth / 2) - (fullSprite.getWidth()), (Gdx.graphics.getHeight() / 2.0f) + (fullSprite.getHeight() * .5f));

        leftSprite = new Sprite(left);
        leftSprite.setSize(256.0f, 64.0f);
        leftSprite.setPosition((fillCamera.viewportWidth / 2) - (fullSprite.getWidth()), (Gdx.graphics.getHeight() / 2.0f) - (fullSprite.getHeight() * .5f));

        rightSprite = new Sprite(right);
        rightSprite.setSize(256.0f, 64.0f);
        rightSprite.setPosition((fillCamera.viewportWidth / 2) - (fullSprite.getWidth()), (Gdx.graphics.getHeight() / 2.0f) - (fullSprite.getHeight() * 1.5f));

        backSprite = new Sprite(back);
        backSprite.setSize(256.0f, 64.0f);
        backSprite.setPosition((fillCamera.viewportWidth / 2) - (fullSprite.getWidth()), 70);
    }

    /**
     * Draw function for SceneOptionsMenu.
     * <p>
     * Draws Options Menu for the PixelBoat game.
     *
     * @param batch SpriteBatch used for drawing to screen.
     * @author William Walton
     */
    public void draw(SpriteBatch batch) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(fillCamera.combined);
        batch.begin();
        bgSprite.draw(batch);
        fullSprite.draw(batch);
        accelSprite.draw(batch);
        leftSprite.draw(batch);
        rightSprite.draw(batch);
        backSprite.draw(batch);
        fullCheckSprite.draw(batch);
        batch.end();
    }

    /**
     * Update function for SceneOptionsMenu. Ends SceneOptionsMenu based on user input otherwise stays in scene.
     * <p>
     * Returns an specified integer when you want to exit the screen else return scene_id if you want to stay in scene.
     * In this case left clicking with the mouse on  the back button will stop the Options Menu Scene
     * and continue with the Main Menu Scene.
     *
     * @return returns an integer which is the scene_id of which screen is next (either this screen still or another)
     * @author William Walton
     */
    public int update() {
        Vector3 mouse_pos = fillCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        if (backSprite.getBoundingRectangle().contains(mouse_pos.x, mouse_pos.y)) {
            backSprite.setTexture(backHovered);
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                return 0;
            }
        } else
            backSprite.setTexture(back);

        // todo add single click detection to stop this changing every frame
        if (fullCheckSprite.getBoundingRectangle().contains(mouse_pos.x, mouse_pos.y)) {
            //full_check_sprite.setTexture(full_check_);
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                fullCheckSprite.setTexture(isFullscreen ? fullCheckNo : fullCheckYes);
                isFullscreen = !isFullscreen;
            }
        }

        return sceneID;
    }

    /**
     * Temp resize method if needed for camera extension.
     *
     * @param width  Integer width to be resized to
     * @param height Integer height to be resized to
     * @author William Walton
     */
    public void resize(int width, int height) {
        fillViewport.update(width, height);
        fillCamera.position.set(fillCamera.viewportWidth / 2, fillCamera.viewportHeight / 2, 0);
    }
}

package com.teamonehundred.pixelboat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Represents the Tutorial Scene for when the player wants to see the controls and learn the purpose of UI elements
 * before the race starts.
 *
 * @author William Walton
 * JavaDoc by Umer Fakher
 */
public class SceneTutorial implements Scene {
    private static final int SCENE_ID = 3;

    private final Texture bg;
    private final Sprite bgSprite;

    private final Viewport fillViewport;
    private final OrthographicCamera fillCamera;

    /**
     * Main constructor for a Tutorial Scene.
     * <p>
     * Initialises a Scene textures for Tutorial Screen Overlay and camera.
     *
     * @author William Walton
     */
    SceneTutorial() {
        fillCamera = new OrthographicCamera();
        fillViewport = new FillViewport(1280, 720, fillCamera);
        fillViewport.apply();
        fillCamera.position.set(fillCamera.viewportWidth / 2, fillCamera.viewportHeight / 2, 0);
        fillViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        bg = new Texture("tutorial_screen.png");
        bgSprite = new Sprite(bg);
        bgSprite.setPosition(0, 0);
        bgSprite.setSize(1280, 720);
    }

    /**
     * Destructor disposes of this texture once it is no longer referenced.
     */
    protected void finalize() {
        bg.dispose();
    }

    /**
     * Draw function for SceneTutorial.
     * <p>
     * Draws SceneTutorial for the PixelBoat game.
     *
     * @param batch SpriteBatch used for drawing to screen.
     * @author William Walton
     */
    public void draw(SpriteBatch batch) {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(fillCamera.combined);
        batch.begin();
        bgSprite.draw(batch);
        batch.end();
    }

    /**
     * Update function for SceneTutorial. Ends SceneTutorial based on user input otherwise stays in scene.
     * <p>
     * Returns an specified integer when you want to exit the screen else return scene_id if you want to stay in scene.
     * In this case any key pressed on the keyboard will stop the Tutorial Scene and continue with the main game.
     *
     * @return returns an integer which is the scene_id of which screen is next (either this screen still or another)
     * @author William Walton
     */
    public int update(float deltaTime) {
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY))
            return 1; // Move onto next Scene (Back to main game)

        return SCENE_ID; // Stay on current Tutorial Scene.
    }

    /**
     * Temp resize method if needed for camera extension.
     *
     * @param width  Integer width to be resized to
     * @param height Integer height to be resized to
     * @author Umer Fakher
     */
    public void resize(int width, int height) {
        fillViewport.update(width, height);
        fillCamera.position.set(fillCamera.viewportWidth / 2, fillCamera.viewportHeight / 2, 0);
    }

    /**
     * Called whenever a scene is switched to
     */
    @Override
    public void show() {

    }
}

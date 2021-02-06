package com.teamonehundred.pixelboat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teamonehundred.pixelboat.ui.Button;
import com.teamonehundred.pixelboat.ui.Image;
import com.teamonehundred.pixelboat.ui.Label;
import com.teamonehundred.pixelboat.ui.UIScene;

/**
 * Represents the Boat Selection Scene for when the player wants to select which boat to play with before the race
 * starts.
 *
 * @author William Walton
 * JavaDoc by Umer Fakher
 */
public class ScenePreRace implements Scene {
    private static final int SCENE_ID = 5;
    private int exitCode = SCENE_ID;

    private final UIScene scene;

    private final OrthographicCamera fillCamera;
    private int specID = 0;

    /**
     * Main constructor for a SceneBoatSelection.
     * <p>
     * Initialises a Scene textures for Boat Selection and camera.
     *
     * @author William Walton
     */
    public ScenePreRace() {
        fillCamera = new OrthographicCamera();
        Viewport fillViewport = new FillViewport(1280, 720, fillCamera);
        fillViewport.apply();
        fillCamera.position.set(fillCamera.viewportWidth / 2, fillCamera.viewportHeight / 2, 0);
        fillViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        scene = new UIScene();

        Image bg = new Image(0, 0, "ui/main_bg.png");
        bg.getSprite().setSize(1280, 720);

        // niceE1
        Label selectABoat = new Label(640, 690, 1.0f, "Select a boat", true);

        Button buttonDefault = new Button(512.0f, 370.0f, "ui/pre_race/boat_default.png", "ui/pre_race/boat_default.png")
        {
            @Override
            protected void onPress() {
                super.onPress();
                specID = 1;
                exitCode = 3;
            }
        };
        buttonDefault.getSprite().setSize(256.0f, 128.0f);

        Button buttonFast = new Button(512.0f, 222.0f, "ui/pre_race/boat_fast.png", "ui/pre_race/boat_fast.png")
        {
            @Override
            protected void onPress() {
                super.onPress();
                specID = 2;
                exitCode = 3;
            }
        };
        buttonFast.getSprite().setSize(256.0f, 128.0f);

        Button buttonLoad = new Button(512.0f, 74.0f, "ui/pre_race/load_save.png", "ui/pre_race/load_save.png")
        {
            @Override
            protected void onPress() {
                super.onPress();
                exitCode = -1;
            }
        };
        buttonLoad.getSprite().setSize(256.0f, 128.0f);

        scene.addElement(0, "bg", bg);
        scene.addElement(1, "text", selectABoat);
        scene.addElement(1, "btnDefault", buttonDefault);
        scene.addElement(1, "btnFast", buttonFast);
        scene.addElement(1, "btnLoad", buttonLoad);
    }

    /**
     * Update function for SceneBoatSelection. Ends SceneBoatSelection based on user input otherwise stays in scene.
     * <p>
     * Returns an specified integer when you want to exit the screen else return scene_id if you want to stay in scene.
     *
     * @return returns an integer which is the scene_id of which screen is next (either this screen still or another)
     * @author William Walton
     */
    public int update(float deltaTime) {
        exitCode = SCENE_ID;

        Vector3 mousePos = fillCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        scene.update(mousePos.x, mousePos.y);

        return exitCode;
    }

    /**
     * Draw function for SceneBoatSelection.
     * <p>
     * Draws BoatSelection for the PixelBoat game.
     *
     * @param batch SpriteBatch used for drawing to screen.
     * @author William Walton
     */
    public void draw(SpriteBatch batch) {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(fillCamera.combined);
        batch.begin();
        scene.draw(batch);
        batch.end();
    }

    /**
     * Temp resize method if needed for camera extension.
     *
     * @param width  Integer width to be resized to
     * @param height Integer height to be resized to
     * @author Umer Fakher
     */
    public void resize(int width, int height) {
    }

    /**
     * Called whenever a scene is switched to
     */
    @Override
    public void show() {
        scene.lockScene();
    }

    /**
     * Getter method for the specified boat's spec_id.
     *
     * @return boat's spec id
     * @author William Walton
     */
    public int getSpecID() {
        return specID;
    }
}

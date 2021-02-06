package com.teamonehundred.pixelboat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teamonehundred.pixelboat.ui.*;

/**
 * Represents the Options Menu Scene for when the player wants to select/edit the options before the race starts.
 *
 * @author William Walton
 * JavaDoc by Umer Fakher
 */
public class SceneOptionsMenu implements Scene {
    private static final int SCENE_ID = 2;
    private int exitCode = 2;

    private final Viewport fillViewport;
    private final OrthographicCamera fillCamera;

    UIScene uiScene;


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

        final Image background;
        final Switch fullscreenButton;
        final Button difficultyButtonLeft;
        final Button difficultyButtonRight;
        final Button backButton;
        final Label difficultyLabel;

        // Make background
        background = new Image(0, 0, "ui/main_bg.png");
        background.getSprite().setSize(1280,720);

        difficultyButtonLeft = new Button(
                512.0f,
                428.0f,
                "ui/options/arrow_left.png",
                "ui/options/arrow_left_pressed.png",
                "ui/options/arrow_left_pressed.png")
        {
            @Override
            protected void onRelease() {
                super.onRelease();
                Difficulty.getInstance().decreaseDifficulty();
            }
        };
        difficultyButtonLeft.getSprite().setSize(32.0f, 64.0f);

        difficultyButtonRight = new Button(
                736.0f,
                428.0f,
                "ui/options/arrow_right.png",
                "ui/options/arrow_right_pressed.png",
                "ui/options/arrow_right_pressed.png")
        {
            @Override
            protected void onRelease() {
                super.onRelease();
                Difficulty.getInstance().increaseDifficulty();
            }
        };
        difficultyButtonRight.getSprite().setSize(32.0f, 64.0f);

        difficultyLabel = new Label(640.0f, 508.0f, 0.65f, "Medium", true) {
            @Override
            public void update(float mouseX, float mouseY) {
                setText(Difficulty.getInstance().toString());
            }
        };

        fullscreenButton = new Switch(
                512.0f,
                512.0f,
                "ui/options/fullscreen.png",
                "ui/options/fullscreen_pressed.png",
                "ui/options/fullscreen_pressed.png")
        {
            @Override
            protected void onStateOff() {
                Gdx.graphics.setWindowedMode(1280, 720);
            }
            @Override
            protected void onStateOn() {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        };
        fullscreenButton.getSprite().setSize(256.0f, 64.0f);

        backButton = new Button(
                512.0f,
                128.0f,
                "ui/options/back.png",
                "ui/options/back_hovered.png",
                "ui/options/back_hovered.png")
        {
            @Override
            protected void onRelease() {
                super.onRelease();
                exitCode = 0;
            }
        };
        backButton.getSprite().setSize(256.0f, 64.0f);

        fillViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        uiScene = new UIScene();
        uiScene.addElement(0, "bg", background);
        uiScene.addElement(1, "diff_left", difficultyButtonLeft);
        uiScene.addElement(1, "diff_right", difficultyButtonRight);
        uiScene.addElement(1, "fullscreen", fullscreenButton);
        uiScene.addElement(2,"diff_text", difficultyLabel);
        uiScene.addElement(2, "back", backButton);

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
        uiScene.draw(batch);

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
    public int update(float deltaTime) {
        exitCode = SCENE_ID;
        Vector3 mouse_pos = fillCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        uiScene.update(mouse_pos.x, mouse_pos.y);

        return exitCode;
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

    /**
     * Called whenever a scene is switched to
     */
    @Override
    public void show() {
        uiScene.lockScene();
    }
}

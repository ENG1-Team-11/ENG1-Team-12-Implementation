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
import com.teamonehundred.pixelboat.ui.UIScene;

/**
 * Represents the Main Game Scene for when the boat race starts.
 *
 * @author William Walton
 * JavaDoc by Umer Fakher
 */
public class SceneStartScreen implements Scene {
    private static final int SCENE_ID = 0;
    private final UIScene scene;
    private final Viewport fillViewport;
    private final OrthographicCamera fillCamera;
    private int exitCode = 0;

    /**
     * Main constructor for a SceneStartGame.
     * <p>
     * Initialises a Scene textures for StartScreen. Including start menu for playing the game and options.
     * When options are hovered over they will change texture indicating what the user is about to select.
     *
     * @author William Walton
     */
    SceneStartScreen() {
        fillCamera = new OrthographicCamera();
        fillViewport = new FillViewport(1280, 720, fillCamera);
        fillViewport.apply();
        fillCamera.position.set(fillCamera.viewportWidth / 2, fillCamera.viewportHeight / 2, 0);

        final Button playButton;
        final Button optionsButton;
        final Image background;

        scene = new UIScene();

        background = new Image(0, 0, "ui/main_bg.png");
        background.getSprite().setSize(1280, 720);

        playButton = new Button(
                512.0f,
                403.0f,
                "ui/start_menu/play.png",
                "ui/start_menu/play_hovered.png",
                "ui/start_menu/play_hovered.png"
        ) {
            @Override
            public void onPress() {
                super.onRelease();
                exitCode = 5;
            }
        };
        playButton.getSprite().setSize(256.0f, 64.0f);

        optionsButton = new Button(
                512.0f,
                317.0f,
                "ui/start_menu/options.png",
                "ui/start_menu/options_hovered.png",
                "ui/start_menu/options_hovered.png"
        ) {
            @Override
            protected void onPress() {
                super.onRelease();
                exitCode = 2;
            }
        };
        optionsButton.getSprite().setSize(256.0f, 64.0f);

        scene.addElement(0, "bg", background);
        scene.addElement(1, "button_play", playButton);
        scene.addElement(1, "button_options", optionsButton);
    }

    /**
     * Destructor disposes of this texture once it is no longer referenced.
     */
    protected void finalize() {

    }

    /**
     * Draw function for SceneStartScreen.
     * <p>
     * Draws StartScreen for the PixelBoat game.
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
     * Update function for SceneStartScreen. Ends SceneStartScreen based on user input otherwise stays in scene.
     * <p>
     * Returns an specified integer when you want to exit the screen else return scene_id if you want to stay in scene.
     *
     * @return returns an integer which is the scene_id of which screen is next (either this screen still or another)
     * @author William Walton
     */
    public int update(float deltaTime) {
        exitCode = SCENE_ID;

        Vector3 mouse_pos = fillCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        scene.update(mouse_pos.x, mouse_pos.y);

        // Stay in SceneStartScreen
        return exitCode;
    }

    /**
     * Resize method if for camera extension.
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
        scene.lockScene();
    }
}

package com.teamonehundred.pixelboat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main class for the PixelBoat game.
 * <p>
 * Extends Libgdx ApplicationAdapter.
 *
 * @author William Walton
 * @author James Frost
 * @author Umer Fakher
 * JavaDoc by Umer Fakher
 */
public class PixelBoat extends ApplicationAdapter {
    // id of current game state
    // 0 = start menu
    // 1 = game
    // 2 = options
    // 3 = tutorial
    // 4 = results
    // 5 = boat selection
    protected int sceneID = 0;
    private Scene[] scenes;  // stores all game scenes and their data
    private SpriteBatch batch;  // thing that draws the sprites
    private float deltaTime;

    /**
     * Create method runs when the game starts.
     * <p>
     * Runs every scene in Game.
     */
    @Override
    public void create() {
        scenes = new Scene[6];
        scenes[0] = new SceneStartScreen();
        scenes[1] = new SceneMainGame();
        scenes[2] = new SceneOptionsMenu();
        scenes[3] = new SceneTutorial();
        scenes[4] = new SceneResultsScreen();
        scenes[5] = new SceneBoatSelection();

        batch = new SpriteBatch();

        // Instantiate difficulty
        Difficulty.getInstance();
    }

    /**
     * Render function runs every frame.
     * <p>
     * Controls functionality of frame switching.
     */
    @Override
    public void render() {
        // run the current scene
        // Calculate the time since the last frame began
        deltaTime = Gdx.graphics.getDeltaTime();

        int newSceneID = scenes[sceneID].update(deltaTime);
        scenes[sceneID].draw(batch);

        if (sceneID != newSceneID) {
            // special case updates
            if (newSceneID == 4)
                ((SceneResultsScreen) scenes[4]).setBoats(((SceneMainGame) scenes[1]).getAllBoats());
            else if (newSceneID == 3 && sceneID == 5)
                ((SceneMainGame) scenes[1]).setPlayerSpec(((SceneBoatSelection) scenes[5]).getSpecID());


            // check if we need to change scene
            sceneID = newSceneID;
        }
    }

    /**
     * Disposes unneeded SpriteBatch and exits application.
     * <p>
     * Runs when the game needs to close.
     */
    @Override
    public void dispose() {
        batch.dispose();

        Gdx.app.exit();
        System.exit(0);
    }

    /**
     * Resize used and passed to resize method of each scene based on width and height attributes.
     *
     * @param width  int for scene
     * @param height int for scene
     */
    @Override
    public void resize(int width, int height) {
        scenes[sceneID].resize(width, height);
    }
}

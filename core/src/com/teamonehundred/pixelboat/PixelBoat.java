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
    private SpriteBatch batch;  // thing that draws the sprites
    private float deltaTime;

    private SceneStartScreen startScreen;
    private SceneMainGame mainGame;
    private SceneOptionsMenu optionsMenu;
    private SceneTutorial tutorial;
    private SceneResultsScreen resultsScreen;
    private ScenePreRace preRace;

    private Scene currentScene;
    private Scene nextScene;

    private SaveManager saveManager;

    /**
     * Create method runs when the game starts.
     * <p>
     * Runs every scene in Game.
     */
    @Override
    public void create() {
        startScreen = new SceneStartScreen();
        mainGame = new SceneMainGame();
        optionsMenu = new SceneOptionsMenu();
        tutorial = new SceneTutorial();
        resultsScreen = new SceneResultsScreen();
        preRace = new ScenePreRace();

        saveManager = new SaveManager(mainGame);

        currentScene = nextScene = startScreen;

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

        currentScene = nextScene;

        int nextSceneID = currentScene.update(deltaTime);

        switch (nextSceneID) {
            case 0:
                nextScene = startScreen;
                break;
            case 1:
                nextScene = mainGame;
                if (currentScene == tutorial) {
                    mainGame.setPlayerSpec(preRace.getSpecID());
                }
                break;
            case 2:
                nextScene = optionsMenu;
                break;
            case 3:
                nextScene = tutorial;
                break;
            case 4:
                nextScene = resultsScreen;
                // If we're just switching to this scene, give it the boats so it can generate a results listing
                if (currentScene != nextScene) {
                    resultsScreen.setBoats(mainGame.getAllBoats());
                }
                break;
            case 5:
                nextScene = preRace;
                break;
            case -1: {
                // Special case for handling loading a game
                boolean result = saveManager.loadState();
                if (result) {
                    resultsScreen.setBoats(mainGame.getAllBoats());
                    nextScene = resultsScreen;
                }
                else {
                    System.out.println("Could not load save");
                    nextScene = startScreen;
                }
                break;
            }
            case -2: {
                // Special case for handling saving a game
                boolean result = saveManager.saveState();
                if (result)
                    nextScene = startScreen;
                else {
                    System.out.println("Could not save game");
                    nextScene = resultsScreen;
                }
            }
        }

        if (currentScene != nextScene) {
            nextScene.show();
        }

        currentScene.draw(batch);
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
        currentScene.resize(width, height);
    }
}

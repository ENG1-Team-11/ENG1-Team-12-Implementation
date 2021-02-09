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

    private SceneStartScreen startScreen;
    private SceneMainGame mainGame;
    private SceneOptionsMenu optionsMenu;
    private SceneTutorial tutorial;
    private SceneResultsScreen resultsScreen;
    private ScenePreRace preRace;
    private SceneEndScreen endScreen;

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
        endScreen = new SceneEndScreen();

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
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Set the current scene to the next scene
        currentScene = nextScene;

        // Update the current scene
        int nextSceneID = currentScene.update(deltaTime);

        // Check against the return value of the update function
        switch (nextSceneID) {

            case 0: {
                /*
                If 0, go to the start screen
                If we're coming from the end screen, reset the game
                (Just make a new one)
                */
                nextScene = startScreen;
                if (currentScene == endScreen) {
                    mainGame = new SceneMainGame();
                }
                break;
            }
            case 1: {
                /*
                If 1, go to the main game (unless the current scene is tutorial
                which in that case also set the player spec)
                */
                nextScene = mainGame;
                if (currentScene == tutorial) {
                    mainGame.setPlayerSpec(preRace.getSpecID());
                }
                break;
            }

            case 2: {
                /*
                If 2, go to the options screen
                */
                nextScene = optionsMenu;
                break;
            }
            case 3: {
                /*
                If 3, go to the tutorial
                */
                nextScene = tutorial;
                break;
            }
            case 4: {
                /*
                If 4, go to the results screen.
                If the currentScene is not the results screen, also give the boats to the results screen
                 */
                nextScene = resultsScreen;
                if (currentScene != nextScene) {
                    resultsScreen.setBoats(mainGame.getAllBoats());
                }
                break;
            }
            case 5: {
                /*
                If 5, go to the pre-race screen
                 */
                nextScene = preRace;
                break;
            }
            case 6: {
                /*
                If 6, go to the pre-race screen
                If the current scene is the game screen, also give it the boats to calculate the results with
                 */
                nextScene = endScreen;
                if (currentScene == mainGame) {
                    endScreen.updateScreen(mainGame.getAllBoats(), mainGame.getPlayer());
                }
                break;
            }
            case -1: {
                // Special case for handling loading a game
                boolean result = saveManager.loadState();
                // If the save was loaded successfully, go to the results screen
                if (result) {
                    resultsScreen.setBoats(mainGame.getAllBoats());
                    nextScene = resultsScreen;
                    // If not, exit to the main menu
                } else {
                    System.out.println("Could not load save");
                    nextScene = startScreen;
                }
                break;
            }
            case -2: {
                // Special case for handling saving a game
                boolean result = saveManager.saveState();
                // If the game saved successfully, go to the main menu
                if (result)
                    nextScene = startScreen;
                    // If not, stay on the results screen
                else {
                    System.out.println("Could not save game");
                    nextScene = resultsScreen;
                }
                break;
            }
        }

        // If the current scene is not the same as the next screen,
        // call the show() function in the next screen
        if (currentScene != nextScene) {
            nextScene.show();
        }

        // Draw the current scene
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

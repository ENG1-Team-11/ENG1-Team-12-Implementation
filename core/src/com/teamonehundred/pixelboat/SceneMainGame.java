package com.teamonehundred.pixelboat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents the Main Game Scene for when the boat race starts.
 *
 * @author William Walton
 * @author Umer Fakher
 * JavaDoc by Umer Fakher
 */
public class SceneMainGame implements Scene {

    private static final int SCENE_ID = 1;
    private final PlayerBoat player;
    private final List<Boat> boats;
    private final Texture bg;
    private int legNumber = 0;
    private BoatRace race;

    private final static int BOATS_PER_RACE = 7;
    private final static int GROUPS_PER_GAME = 1;



    /**
     * Main constructor for a SceneMainGame.
     * <p>
     * Initialises a BoatRace, player's boat, AI boats and scene textures.
     *
     * @author William Walton
     */
    SceneMainGame() {
        player = new PlayerBoat(-15, 0);
        player.setName("Player");
        boats = new ArrayList<>();

        Difficulty difficulty = Difficulty.getInstance();

        boats.add(player);
        for (int i = 0; i < (BOATS_PER_RACE * GROUPS_PER_GAME) - 1; i++) {
            boats.add(new AIBoat(0, 40, difficulty.getBoatTargetSpeed()));
            boats.get(boats.size() - 1).setName("AI Boat " + i);
        }

        Collections.swap(boats, 0, (boats.size() / GROUPS_PER_GAME) / 2); // move player to middle of first group

        bg = new Texture("water_background.png");
        bg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        race = new BoatRace(boats.subList(0, BOATS_PER_RACE), player);
        legNumber++;
    }


    /**
     * Destructor disposes of this texture once it is no longer referenced.
     */
    protected void finalize() {
        bg.dispose();
    }


    /**
     * Draws SpriteBatch on display along with updating player camera and player overlay Using BoatRace.
     *
     * @param batch Spritebatch passed for drawing graphic objects onto screen.
     * @author William Walton
     */
    public void draw(SpriteBatch batch) {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.getCamera().update();
        batch.setProjectionMatrix(player.getCamera().combined);

        batch.begin();

        batch.draw(bg, -10000, -2000, 0, 0, 1000000, 10000000);
        race.draw(batch);

        batch.end();
    }

    /**
     * Calls main runStep method for BoatRace which is repeatedly called for updating the game state.
     * <p>
     * The BoatRace runStep method checks for started or finished boats in a leg, calls update methods for
     * the movements for player boat and AI boats obstacles as well as checking for collisions.
     *
     * @author William Walton
     */
    public int update(float deltaTime) {
        if (player.hasFinishedLeg()) {
            // Generate times for boats rather than simulating the race properly
            race.generateTimesForUnfinishedBoats();
        }
        if (!race.isFinished()) race.runStep(deltaTime);
            // only run 3 guaranteed legs
        else if (legNumber < 3) {
            race = new BoatRace(boats.subList(0, BOATS_PER_RACE), player);

            legNumber++;


            // generate some "realistic" times for all boats not shown
            for (int i = BOATS_PER_RACE; i < boats.size(); i++) {
                boats.get(i).setCurrentRaceTime((int) (65000 + 10000 * Math.random()));
                boats.get(i).setLegTime();
            }

            return 4;

        } else if (legNumber == 3) {
            // sort boats based on best time
            boats.sort(Comparator.comparingInt(Boat::getBestTime));

            race = new BoatRace(boats.subList(0, BOATS_PER_RACE), player);
            legNumber++;

            return 4;
        }

        // stay in results after all legs done
        if (race.isFinished() && legNumber > 3) return 4;


        return SCENE_ID;
    }

    /**
     * Resize method if for camera extension.
     *
     * @param width  Integer width to be resized to
     * @param height Integer height to be resized to
     * @author Umer Fakher
     */
    public void resize(int width, int height) {
        player.getCamera().viewportHeight = height;
        player.getCamera().viewportWidth = width;
    }

    /**
     * Called whenever a scene is switched to
     */
    @Override
    public void show() {

    }

    /**
     * Getter method for returning list of boats which contain all boats in scene.
     *
     * @return list of boats
     * @author Umer Fakher
     */
    public List<Boat> getAllBoats() {
        return boats;
    }

    /**
     * Setter method for player boat spec in the scene.
     *
     * @param spec Integer for player spec.
     * @author Umer Fakher
     */
    public void setPlayerSpec(int spec) {
        player.setSpec(spec);
    }

    public int getLegNumber() {
        return legNumber;
    }

    public void setLegNumber(int legNumber) {
        this.legNumber = Math.min(3, Math.max(0, legNumber));
    }

    public PlayerBoat getPlayer() {
        return player;
    }

}

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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Scene Class for Results Screen. Contains all functionality for Displaying results screen after each leg.
 *
 * @author Umer Fakher
 * JavaDoc by Umer Fakher
 */
public class SceneResultsScreen implements Scene {
    private static final int SCENE_ID = 4;
    private final Viewport fillViewport;
    private final OrthographicCamera fillCamera;
    UIScene uiScene;
    private int exitCode = SCENE_ID;
    private List<Boat> boats;

    SceneResultsScreen() {
        fillCamera = new OrthographicCamera();
        fillViewport = new FillViewport(1280, 720, fillCamera);
        fillViewport.apply();
        fillCamera.position.set(fillCamera.viewportWidth / 2, fillCamera.viewportHeight / 2, 0);
        fillViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        uiScene = new UIScene();

        boats = null;
    }

    /**
     * Update function for SceneResultsScreen. Ends SceneResultsScreen based on user input otherwise stays in scene.
     * <p>
     * Returns 1 when you want to exit the results screen else return scene_id if you want to stay in scene.
     *
     * @return returns an integer which is either the scene_id or 1
     * @author Umer Fakher
     */
    public int update(float deltaTime) {
        exitCode = SCENE_ID;
        Vector3 mouse_pos = fillCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        uiScene.update(mouse_pos.x, mouse_pos.y);

        return exitCode;

    }

    /**
     * Draw function for SceneResultsScreen.
     * <p>
     * Draws ResultsScreen which includes the leg time for all boats (AI and player boats) that have just completed a
     * leg. This table will wrap according to how many boat times need to be displayed. Using label template format it
     * draws the name of boat, time of just completed leg, race penalty added for each boat that finished the leg.
     *
     * @param batch SpriteBatch used for drawing to screen.
     * @author Umer Fakher
     */
    public void draw(SpriteBatch batch) {
        // Initialise colouring
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(fillCamera.combined);

        // Find player's boat in list of boats in order to use x and y axis
        PlayerBoat player = null;
        for (Boat b : boats) {
            if (b instanceof PlayerBoat) {
                player = (PlayerBoat) b;
            }
        }

        // Make sure player is not null
        assert player != null;

        // Begin a sprite batch drawing
        batch.begin();

        uiScene.draw(batch);

        // End a sprite batch drawing
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
        fillViewport.update(width, height);
        fillCamera.position.set(fillCamera.viewportWidth / 2, fillCamera.viewportHeight / 2, 0);
    }

    /**
     * Called whenever a scene is switched to
     */
    @Override
    public void show() {
        uiScene.clear();

        // Generate all the UI whenever this scene is shown
        // Horrifically inefficient, but it does work nicely

        Button saveButton = new Button(50.0f, 50.0f, "ui/results/save.png", "ui/results/save_pressed.png", "ui/results/save_hovered.png") {
            @Override
            protected void onRelease() {
                super.onRelease();
                exitCode = -2;
            }
        };
        saveButton.getSprite().setSize(120.0f, 60.0f);

        Button nextButton = new Button(200.0f, 50.0f, "ui/results/next.png", "ui/results/next_pressed.png") {
            @Override
            protected void onRelease() {
                super.onRelease();
                exitCode = 1;
            }
        };
        nextButton.getSprite().setSize(120.0f, 60.0f);

        uiScene.addElement(1, "save", saveButton);
        uiScene.addElement(1, "next", nextButton);

        Image bg = new Image(0.0f, 0.0f, "ui/results/bg.png");
        uiScene.addElement(0, "bg", bg);

        Label t1 = new Label(640.0f, 700.0f, 0.6f, "Click on the screen to skip and start the next leg!", true);
        Label tName = new Label(480.0f, 550.0f, 0.2f, "BOAT NAME", true);
        Label tTime = new Label(640.0f, 550.0f, 0.2f, "RACE TIME", true);
        Label tAdd = new Label(800.0f, 550.0f, 0.2f, "RACE PENALTY", true);

        uiScene.addElement(1, "t1", t1);
        uiScene.addElement(1, "tn", tName);
        uiScene.addElement(1, "tt", tTime);
        uiScene.addElement(1, "ta", tAdd);

        /*
        I don't like this code
        Please burn it once you've seen it
        This was a Haiku
         */

        class Timing {
            final String name;
            final int time;
            final int add;

            Timing(String n, int t, int a) {
                name = n;
                time = t;
                add = a;
            }
        }
        List<Timing> timings = new ArrayList<>();

        for (Boat b : boats) {
            timings.add(new Timing(b.getName(), b.getLegTimes().get(b.getLegTimes().size() - 1), b.getTimeToAdd()));
        }

        // Sort the timings in descending order
        timings.sort(Comparator.comparingInt(o -> (o.time + o.add)));

        for (int i = 0; i < timings.size(); ++i) {
            Timing t = timings.get(i);
            Label lName = new Label(480.0f, 500.0f - (i * 40.0f), 0.3f, t.name, true);
            Label lTime = new Label(640.0f, 500.0f - (i * 40.0f), 0.3f,  t.time + " ms", true);
            Label lAdditional = new Label(800.0f, 500.0f - (i * 40.0f), 0.3f, t.add + " ms", true);
            uiScene.addElement(1, "labelName" + i, lName);
            uiScene.addElement(1, "labelTime" + i, lTime);
            uiScene.addElement(1, "labelAdditional" + i, lAdditional);
        }
    }

    /**
     * Setter method for list of boats for all boats in scene.
     *
     * @param boats List of boats to be set to current instance.
     * @author Umer Fakher
     */
    public void setBoats(List<Boat> boats) {
        this.boats = boats;
    }
}

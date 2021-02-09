package com.teamonehundred.pixelboat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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

import java.util.List;

/**
 * Represents the End Screen Scene
 */
public class SceneEndScreen implements Scene {
    private static final int SCENE_ID = 6;
    private final UIScene scene;
    private final Label endText;
    private final Image medal;
    private final Image medalRibbon;
    private final Viewport fillViewport;
    private final OrthographicCamera fillCamera;
    private int exitCode = SCENE_ID;

    /**
     * Main constructor for a End Screen Scene.
     */
    SceneEndScreen() {
        fillCamera = new OrthographicCamera();
        fillViewport = new FillViewport(1280, 720, fillCamera);
        fillViewport.apply();
        fillCamera.position.set(fillCamera.viewportWidth / 2, fillCamera.viewportHeight / 2, 0);
        fillViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        scene = new UIScene();

        Image bg = new Image(0, 0, "ui/end/bg.png");
        bg.getSprite().setSize(1280, 720);

        Button btnExit = new Button(32.0f, 32.0f, "ui/end/exit.png", "ui/end/exit_hovered.png") {
            /**
             * Called when the button is pressed
             **/
            @Override
            protected void onPress() {
                super.onPress();
                exitCode = 0;
            }
        };
        btnExit.getSprite().setSize(256.0f, 100.0f);

        medalRibbon = new Image(512.0f, 232.0f, "ui/end/medal_ribbon.png");
        medalRibbon.getSprite().setSize(256.0f, 256.0f);

        medal = new Image(512.0f, 232.0f, "ui/end/medal.png");
        medal.getSprite().setSize(256.0f, 256.0f);

        endText = new Label(640.0f, 690.0f, 1.0f, "ERROR", true);

        scene.addElement(0, "bg", bg);
        scene.addElement(1, "btnExit", btnExit);
        scene.addElement(1, "medal", medal);
        scene.addElement(1, "medalRibbon", medalRibbon);
        scene.addElement(2, "text", endText);
    }

    /**
     * Draws the scene
     *
     * @param batch SpriteBatch used for drawing to screen.
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
     * Updates the scene
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
     * Updates the screen to reflect the position the player came in
     *
     * @param boats  The list of boats in the game
     * @param player A reference to the player boat
     */
    public void updateScreen(List<Boat> boats, PlayerBoat player) {
        // Sort the list by final leg time
        boats.sort((o1, o2) -> {
            int o1time = o1.getLegTimes().get(o1.getLegTimes().size() - 1);
            int o2time = o2.getLegTimes().get(o2.getLegTimes().size() - 1);

            return o1time - o2time;
        });

        // Get the position of the player by comparing against the sorted list
        int position = 1;
        for (Boat b : boats) {
            if (b == player) {
                break;
            }
            ++position;
        }

        medal.setVisible(true);
        medalRibbon.setVisible(true);

        // Set the text based on position
        String text;
        switch (position) {
            case 1:
                text = "Congratulation, you won!";
                medal.getSprite().setColor(Color.valueOf("c9aa00"));
                break;
            case 2:
                text = "You came 2nd!";
                medal.getSprite().setColor(Color.valueOf("dcdcdc"));
                break;
            case 3:
                text = "You came 3rd!";
                medal.getSprite().setColor(Color.valueOf("885608"));
                break;
            default:
                text = String.format("You came %dth.  Better luck next time.", position);
                medal.setVisible(false);
                medalRibbon.setVisible(false);
        }

        // Set the actual text
        endText.setText(text);
    }

    /**
     * Called whenever a scene is switched to
     */
    @Override
    public void show() {

    }
}

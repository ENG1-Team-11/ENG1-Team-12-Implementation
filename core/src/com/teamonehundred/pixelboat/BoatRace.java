package com.teamonehundred.pixelboat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a BoatRace. Call functionality for sprite objects such as boats and obstacles.
 *
 * @author William Walton
 * @author Umer Fakher
 * JavaDoc by Umer Fakher
 */
public class BoatRace {
    private final List<Boat> boats;
    private final PlayerBoat player;

    private final BitmapFont font; //TimingTest
    private final Texture laneSeparator;
    private final Texture startBanner;
    private final Texture bleachersLeft;
    private final Texture bleachersRight;

    private final List<CollisionObject> laneObjects;

    private static final int START_Y = 200;
    private static final int END_Y = 40000;

    private static final int LANE_WIDTH = 400;
    private static final int PENALTY_PER_FRAME = 1; // ms to add per frame when over the lane

    private boolean isFinished = false;
    private long totalFrames = 0;

    /**
     * Main constructor for a BoatRace.
     * <p>
     * Initialises lists of boats and obstacles as well as the colour of the Time Elapsed Overlay.
     *
     * @param boats List of Boat A list of ai boats and the player boat.
     * @author William Walton
     * @author Umer Fakher
     * JavaDoc by Umer Fakher
     */
    BoatRace(List<Boat> boats, PlayerBoat player) {
        laneSeparator = new Texture("lane_buoy.png");
        startBanner = new Texture("start_banner.png");
        bleachersLeft = new Texture("bleachers_l.png");
        bleachersRight = new Texture("bleachers_r.png");

        this.boats = new ArrayList<>();
        this.boats.addAll(boats);
        this.player = player;

        for (int i = 0; i < this.boats.size(); i++) {
            this.boats.get(i).setHasStartedLeg(false);
            this.boats.get(i).setHasFinishedLeg(false);

            this.boats.get(i).resetMotion();
            this.boats.get(i).getSprite().setPosition(getLaneCentre(i), 40);  // reset boats y and place in lane
            this.boats.get(i).setFramesRaced(0);
            this.boats.get(i).reset();
        }

        player.resetCameraPos();

        laneObjects = new ArrayList<>();

        // add some random obstacles
        for (int i = 0; i < 100; i++)
            laneObjects.add(new ObstacleBranch(
                    (int) (-(LANE_WIDTH * this.boats.size() / 2) + Math.random() * (LANE_WIDTH * this.boats.size())),
                    (int) (START_Y + 50 + Math.random() * (END_Y - START_Y - 50)))
            );

        for (int i = 0; i < 100; i++)
            laneObjects.add(new ObstacleFloatingBranch(
                    (int) (-(LANE_WIDTH * this.boats.size() / 2) + Math.random() * (LANE_WIDTH * this.boats.size())),
                    (int) (START_Y + 50 + Math.random() * (END_Y - START_Y - 50)))
            );

        for (int i = 0; i < 100; i++)
            laneObjects.add(new ObstacleDuck(
                    (int) (-(LANE_WIDTH * this.boats.size() / 2) + Math.random() * (LANE_WIDTH * this.boats.size())),
                    (int) (START_Y + 50 + Math.random() * (END_Y - START_Y - 50)))
            );

        // add some powerups
        for (int i = 0; i < 100; ++i)
            laneObjects.add(new Powerup(
                    (int) (-(LANE_WIDTH * this.boats.size() / 2) + Math.random() * (LANE_WIDTH * this.boats.size())),
                    (int) (START_Y + 50 + Math.random() * (END_Y - START_Y - 50)),
                    Powerup.Type.values()[(int)(Math.random() * Powerup.Type.values().length)])
            );

        // add the lane separators
        for (int lane = 0; lane <= this.boats.size(); lane++) {
            for (int height = 0; height <= END_Y; height += ObstacleLaneWall.TEXTURE_HEIGHT) {
                laneObjects.add(new ObstacleLaneWall(getLaneCentre(lane) - LANE_WIDTH / 2, height, laneSeparator));
            }
        }

        // Initialise colour of Time Elapsed Overlay
        font = new BitmapFont();
        font.setColor(Color.RED);
    }

    private int getLaneCentre(int index) {
        int raceWidth = boats.size() * LANE_WIDTH;
        return (-raceWidth / 2) + (LANE_WIDTH * (index + 1)) - (LANE_WIDTH / 2);
    }

    /**
     * Main method called for BoatRace.
     * <p>
     * This method is the main game loop that checks if any boats have started or finished a leg and
     * calls the update methods for the movements for player boat and AI boats obstacles.
     * Also this method checks for collisions.
     *
     * @author William Walton
     * @author Umer Fakher
     */
    public void runStep() {
        // dnf after 5 minutes
        if (totalFrames++ > 18000) {
            isFinished = true;
            for (Boat b : boats) {
                if (!b.hasFinishedLeg()) {
                    b.setStartTime(0);
                    b.setEndTime((long) (b.getStartTime(false) + ((1000.0 / 60.0) * b.getFramesRaced())));
                    b.setLegTime();
                }
            }
        }

        for (CollisionObject c : laneObjects) {
            ((MovableObject) c).updatePosition();
            if (c instanceof ObstacleLaneWall) {
                ((ObstacleLaneWall) c).setAnimationFrame(0);
            }
        }

        for (Boat boat : boats) {
            // check if any boats have finished
            if (!boat.hasFinishedLeg() && boat.getSprite().getY() > END_Y) {
                // store the leg time in the object
                boat.setStartTime(0);
                boat.setEndTime((long) (boat.getStartTime(false) + ((1000.0 / 60.0) * boat.getFramesRaced())));
                boat.setLegTime();

                boat.setHasFinishedLeg(true);
            }
            // check if any boats have started
            else if (!boat.hasStartedLeg() && boat.getSprite().getY() > START_Y) {
                boat.setStartTime(System.currentTimeMillis());
                boat.setHasStartedLeg(true);
                boat.setFramesRaced(0);
            } else {
                // if not start or end, must be racing
                boat.addFrameRaced();
            }
        }

        boolean notFinished = false;

        for (int i = 0; i < boats.size(); i++) {
            // all boats
            if (!boats.get(i).hasFinishedLeg()) notFinished = true;

            // update boat (handles inputs if player, etc)
            if (boats.get(i) instanceof AIBoat) {
                ((AIBoat) boats.get(i)).updatePosition(laneObjects);
            } else if (boats.get(i) instanceof PlayerBoat) {
                boats.get(i).updatePosition();
            }

            // check for collisions
            for (CollisionObject obstacle : laneObjects) {
                if (obstacle.isShown())
                    boats.get(i).checkCollisions(obstacle);
            }

            // check if out of lane
            if (boats.get(i).getSprite().getX() > getLaneCentre(i) + LANE_WIDTH / 2.0f ||
                    boats.get(i).getSprite().getX() < getLaneCentre(i) - LANE_WIDTH / 2.0f)
                boats.get(i).setTimeToAdd(boats.get(i).getTimeToAdd() + PENALTY_PER_FRAME);
        }
        isFinished = !notFinished;
    }

    public boolean isFinished() {
        return isFinished;
    }

    /**
     * Returns a list of all sprites in the PixelBoat game including boats and obstacles.
     *
     * @return List of Sprites A list of all sprites in the PixelBoat game.
     * @author William Walton
     * @author Umer Fakher
     */
    public List<Sprite> getSprites() {
        List<Sprite> sprites = new ArrayList<>();

        for (CollisionObject obs : laneObjects) {
            // All collision objects are game objects (so far)
            if (obs.isShown()) {
                GameObject go = (GameObject) obs;
                sprites.add(go.getSprite());
            }
        }

        for (Boat b : boats) {
            sprites.add(b.getSprite());
        }

        sprites.addAll(player.getUISprites());

        return sprites;
    }

    /**
     * Calculates and displays the Time Elapsed Overlay for player boat from the start of a leg.
     * <p>
     * The displayed time is updated in real-time and the position is consistent with the player hud (i.e. stamina
     * and durability bar positions).
     *
     * @param batch The sprite batch to draw to
     * @author Umer Fakher
     */
    public void draw(SpriteBatch batch) {

        // Retrieves sprites and calls function recursively.
        for (Sprite sp : getSprites())
            sp.draw(batch);

        for (Boat b : boats) {
            //If current boat b is the player's boat then can display hud for this boat
            if (b instanceof PlayerBoat) {
                if (b.hasStartedLeg()) {
                    //Calculate time elapsed from the start in milliseconds
                    long i = (System.currentTimeMillis() - b.getStartTime(false));

                    //Displays and updates the time elapsed overlay and keeps position consistent with player's boat
                    drawTimeDisplay(batch, "", i, -((PlayerBoat) b).getUiBarWidth() * 0.5f,
                            500 + b.getSprite().getY());

                    //Draws a leg time display on the screen when the given boat has completed a leg of the race.
                    drawLegTimeDisplay(batch, b);
                }
            }
        }

        int raceWidth = boats.size() * LANE_WIDTH;
        Texture temp = new Texture("object_placeholder.png");

        for (int i = -1000; i < END_Y + 1000; i += 800)
            batch.draw(bleachersRight, raceWidth / 2.0f + 400, i, 400, 800);
        for (int i = -1000; i < END_Y + 1000; i += 800)
            batch.draw(bleachersLeft, -raceWidth / 2.0f - 800, i, 400, 800);
        for (int i = 0; i < boats.size(); i++)
            batch.draw(startBanner, (getLaneCentre(i)) - (LANE_WIDTH / 2.0f), START_Y, LANE_WIDTH, LANE_WIDTH / 2.0f);
        batch.draw(temp, -raceWidth / 2.0f, END_Y, raceWidth, 5);

        temp.dispose();
    }

    /**
     * Draws the a time display on the screen.
     *
     * @param batch SpriteBatch instance
     * @param text  label for text. If "" empty string passed in then default time display shown.
     * @param time  time to be shown in milliseconds
     * @param x     horizontal position of display
     * @param y     vertical position of display
     * @author Umer Fakher
     */
    public void drawTimeDisplay(SpriteBatch batch, String text, long time, float x, float y) {
        if (text.equals("")) {
            text = "Time (min:sec) = %02d:%02d";
        }
        font.draw(batch, String.format(text, time / 60000, time / 1000 % 60), x, y);
    }

    /**
     * Draws a leg time display on the screen when the given boat has completed a leg of the race.
     * <p>
     * This function gets the leg times list for the given boat instance, gets the last updated leg time
     * and formats a leg time display string which shows which leg was completed and in what time.
     * The function then passes on the drawing of this formatted leg time display to drawTimeDisplay.
     *
     * @param batch SpriteBatch instance
     * @param b     Boat instance
     * @author Umer Fakher
     */
    public void drawLegTimeDisplay(SpriteBatch batch, Boat b) {
        if (b.getEndTime(false) != -1) {
            for (long l : b.getLegTimes()) {
                String label = String.format("Leg Time %d (min:sec) = ", b.getLegTimes().indexOf(l) + 1) + "%02d:%02d";
                drawTimeDisplay(
                        batch, label, l, -((PlayerBoat) b).getUiBarWidth() * 0.5f,
                        500 - ((b.getLegTimes().indexOf(l) + 1) * 20) + b.getSprite().getY()
                );
            }

        }
    }

    public void generateTimesForUnfinishedBoats() {
        for (Boat b : boats) {
            if (!b.hasFinishedLeg()) {
                b.setHasFinishedLeg(true);
                float boatY = b.getSprite().getY();
                float distanceRemaining = END_Y - boatY;
                b.setLegTime(player.getLegTimes().get(0) + (long) (distanceRemaining / b.getMaxSpeed() * 67.0f));
            }
        }
        isFinished = true;
    }
}

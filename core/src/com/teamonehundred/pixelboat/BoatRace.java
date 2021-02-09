package com.teamonehundred.pixelboat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamonehundred.pixelboat.util.CollisionTree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a BoatRace. Call functionality for sprite objects such as boats and obstacles.
 *
 * @author William Walton
 * @author Umer Fakher
 * JavaDoc by Umer Fakher
 */
public class BoatRace {
    public static final int END_Y = 40000;
    public static final float MAX_RACE_TIME = 150.0f;
    // The amount that the remaining distance is multiplied by when estimating finishing times
    private static final float BOAT_TIME_ESTIMATION_BIAS = 1.2f;
    private static final int START_Y = 200;
    private static final float LANE_WIDTH = 400.0f;

    private final List<Boat> boats;
    private final PlayerBoat player;
    private final BitmapFont font; //TimingTest
    private final Texture startBanner;
    private final Texture bleachersLeft;
    private final Texture bleachersRight;
    private final List<CollisionObject> laneObjects;
    private final List<CollisionObject> laneObjectsUpdated;
    private boolean isFinished = false;
    private float totalTime = 0;

    private final CollisionTree collisionTree;
    private final float raceWidth;

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
    BoatRace(List<Boat> boats, PlayerBoat player, int leg) {
        Texture laneSeparator = new Texture("lane_buoy.png");
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
            this.boats.get(i).setCurrentRaceTime(0);
            this.boats.get(i).reset();
            this.boats.get(i).getSprite().setPosition(getLaneCentre(i), 40.0f);  // reset boats y and place in lane
        }

        player.resetCameraPos();

        laneObjects = new ArrayList<>();
        laneObjectsUpdated = new ArrayList<>();

        // Make sure difficulty is initialised
        final Difficulty difficulty = Difficulty.getInstance();

        // Set the width
        raceWidth = boats.size() * LANE_WIDTH;

        // add some random obstacles
        float compoundLegDifficulty = (float) Math.pow(difficulty.getLegObstacleModifier(), leg);
        int obstacleCount = (int)(difficulty.getObstacleCount() * compoundLegDifficulty);
        for (int i = 0; i < obstacleCount; i++) {
            laneObjects.add(new ObstacleBranch(
                    (int) (ThreadLocalRandom.current().nextFloat() * raceWidth),
                    (int) (START_Y + 50 + Math.random() * (END_Y - START_Y - 50)))
            );
            laneObjects.add(new ObstacleFloatingBranch(
                    (int) (ThreadLocalRandom.current().nextFloat() * raceWidth),
                    (int) (START_Y + 50 + Math.random() * (END_Y - START_Y - 50)))
            );
            laneObjects.add(new ObstacleDuck(
                    (int) (ThreadLocalRandom.current().nextFloat() * raceWidth),
                    (int) (START_Y + 50 + Math.random() * (END_Y - START_Y - 50)))
            );
        }

        // add some power-ups
        for (int i = 0; i < difficulty.getPowerUpCount(); ++i)
            laneObjects.add(new Powerup(
                    (int) (-(LANE_WIDTH * this.boats.size() / 2) + Math.random() * (LANE_WIDTH * this.boats.size())),
                    (int) (START_Y + 50 + Math.random() * (END_Y - START_Y - 50)),
                    Powerup.Type.values()[(int) (Math.random() * Powerup.Type.values().length)])
            );

        // add the lane separators
        for (int lane = 0; lane <= this.boats.size(); lane++) {
            float laneCentre = getLaneCentre(lane);
            for (int height = 0; height <= END_Y; height += ObstacleLaneWall.TEXTURE_HEIGHT) {
                laneObjects.add(new ObstacleLaneWall( laneCentre - LANE_WIDTH / 2, height, laneSeparator));
            }
        }

        // Start at the left side of the leftmost lane
        float cTreeX = getLaneCentre(0) - (LANE_WIDTH * 0.5f);
        // Start at the very bottom
        float cTreeY = (float) START_Y;
        // Height...
        float raceHeight = END_Y + CollisionTree.MIN_HEIGHT * 0.5f;

        // Create the collision tree and add all objects to it
        collisionTree = new CollisionTree(raceWidth, raceHeight, cTreeX, cTreeY);
        for (CollisionObject obj : laneObjects) {
            // Assume obj is a GameObject
            GameObject go = (GameObject) obj;
            float goX = go.getSprite().getX();
            float goY = go.getSprite().getY();
            collisionTree.add(goX, goY, obj);
        }

        // Initialise colour of Time Elapsed Overlay
        font = new BitmapFont();
        font.setColor(Color.RED);
    }

    private float getLaneCentre(int index) {
        return LANE_WIDTH * (index) + LANE_WIDTH + 0.5f;
    }

    /** Helper function updates the collision tree **/
    private void updateCollisionTree() {
        // Create a collision tree from all the lane objects
        for (CollisionObject obj : laneObjectsUpdated) {
            // Assume obj is a GameObject
            GameObject go = (GameObject) obj;
            float goX = go.getSprite().getX();
            float goY = go.getSprite().getY();
            // Remove the object then add it back
            collisionTree.remove(goX, goY, obj);
            collisionTree.add(goX, goY, obj);
        }
        laneObjectsUpdated.clear();
    }

    /** Helper function updates lane objects **/
    private void updateLaneObjects(float deltaTime) {
        for (Iterator<CollisionObject> iter = laneObjects.iterator(); iter.hasNext(); ) {
            CollisionObject obj = iter.next();
            if (!obj.isShown()) {
                iter.remove();
            }
            else {
                MovableObject go = (MovableObject) obj;
                if (go.update(deltaTime)) {
                    laneObjectsUpdated.add(obj);
                }

                if (go instanceof ObstacleLaneWall) {
                    go.setAnimationFrame(0);
                }
            }
        }
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
    public void runStep(float deltaTime) {
        totalTime += deltaTime;

        // dnf after 5 minutes
        if (totalTime > MAX_RACE_TIME) {
            isFinished = true;
            for (Boat b : boats) {
                if (!b.hasFinishedLeg()) {
                    b.setLegTime((int) (MAX_RACE_TIME * 1000.0f));
                    b.setHasFinishedLeg(true);
                }
            }
            return;
        }

        updateLaneObjects(deltaTime);
        updateCollisionTree();

        for (Boat boat : boats) {
            // check if any boats have finished
            if (!boat.hasFinishedLeg() && boat.getSprite().getY() > END_Y) {
                // store the leg time in the object
                boat.setLegTime();

                boat.setHasFinishedLeg(true);
            }
            // check if any boats have started
            else if (!boat.hasStartedLeg() && boat.getSprite().getY() > START_Y) {
                boat.setCurrentRaceTime(0);
                boat.setHasStartedLeg(true);
            }
        }

        boolean notFinished = false;

        int i = 0;

        for (Boat b : boats) {
            // all boats
            if (!b.hasFinishedLeg()) notFinished = true;

            // update boat (handles inputs if player, etc)
            if (b instanceof AIBoat) {
                ((AIBoat) b).updatePosition(deltaTime, collisionTree);
            } else if (b instanceof PlayerBoat) {
                b.update(deltaTime);
            }

            // check for collisions
            for (CollisionObject obstacle : collisionTree.get(b.getSprite().getX(), b.getSprite().getY())) {
                boats.get(i).checkCollisions(obstacle);
            }

            // check if out of lane
            if (b.getSprite().getX() > getLaneCentre(i) + LANE_WIDTH / 2.0f ||
                    b.getSprite().getX() < getLaneCentre(i) - LANE_WIDTH / 2.0f)
                b.addPenaltyTime((int) (deltaTime * 1000.0f));

            ++i;
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

        if (player.hasStartedLeg()) {
            //Calculate time elapsed from the start in milliseconds
            long i = player.getCurrentRaceTime();

            //Displays and updates the time elapsed overlay and keeps position consistent with player's boat
            drawTimeDisplay(batch, "", i, -player.getUiBarWidth() * 0.5f,
                    500 + player.getSprite().getY());

            //Draws a leg time display on the screen when the given boat has completed a leg of the race.
            drawLegTimeDisplay(batch, player);
        }

        Texture temp = new Texture("object_placeholder.png");

        for (int i = -1000; i < END_Y + 1000; i += 800) {
            batch.draw(bleachersRight, raceWidth + LANE_WIDTH, i, 400, 800);
        }
        for (int i = -1000; i < END_Y + 1000; i += 800) {
            batch.draw(bleachersLeft, -LANE_WIDTH , i, 400, 800);
        }
        for (int i = 0; i < boats.size(); i++) {
            batch.draw(startBanner, (getLaneCentre(i)) - (LANE_WIDTH * 0.5f), START_Y, LANE_WIDTH, LANE_WIDTH * 0.5f);
        }
        batch.draw(temp, 0.0f, END_Y, raceWidth, 5);

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
        if (b.getCurrentRaceTime() != 0) {
            for (int l : b.getLegTimes()) {
                String label = String.format("Leg Time %d (min:sec) = ", b.getLegTimes().indexOf(l) + 1) + "%02d:%02d";
                drawTimeDisplay(
                        batch, label, l, -((PlayerBoat) b).getUiBarWidth() * 0.5f,
                        500 - ((b.getLegTimes().indexOf(l) + 1) * 20) + b.getSprite().getY()
                );
            }

        }
    }

    /**
     * Generate times for any boats that haven't finished based on the distance left, and their target speed
     */
    public void generateTimesForUnfinishedBoats() {
        // Iterate over every boat
        for (Boat b : boats) {
            // If the boat hasn't finished the leg...
            if (!b.hasFinishedLeg()) {
                // Set the boat as finished
                b.setHasFinishedLeg(true);
                // Calculate the distance to the end of the race
                float boatY = b.getSprite().getY();
                float distanceRemaining = END_Y - boatY;
                // Generate a leg time based on the player's time and the target speed
                int cT = b.getCurrentRaceTime();
                float timeRemaining = distanceRemaining * BOAT_TIME_ESTIMATION_BIAS / Difficulty.getInstance().getBoatTargetSpeed();
                float randomBiasedTimeRemaining = timeRemaining * (1.0f + ThreadLocalRandom.current().nextFloat() * 0.1f);
                b.setLegTime(
                       cT + (int) randomBiasedTimeRemaining
                );
            }
        }
        // Set the race as finished
        isFinished = true;
    }
}

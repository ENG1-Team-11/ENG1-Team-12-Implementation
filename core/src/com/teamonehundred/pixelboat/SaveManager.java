package com.teamonehundred.pixelboat;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
 * Save File Structure:
 * (Yes, it's very lazy.  No, I don't care)
 *
 * First 4 bytes is the current leg of the race
 * Second 4 bytes is number of AI boats
 * Next 12 bytes are the player times (as long)
 * Remaining bytes are times for each boat (also as long)
 * Last 4 bytes is the checksum
 */

/**
 * Allows for the game state to be serialised into a check-summed binary file,
 * and later de-serialised back into the game state
 * Allows users to stop a game and continue later
 */
public class SaveManager {

    private static class SaveData {
        /** Basic c'tor initialises SaveData **/
        SaveData() {
            legNumber = 0;
            boatCount = 0;
            playerTimes = new ArrayList<>();
            aiTimes = new ArrayList<>();
        }

        /** Generate a hash code from the field data **/
        @Override
        public int hashCode() {
            return Objects.hash(legNumber, boatCount, playerTimes, aiTimes);
        }

        public Integer legNumber;
        public Integer boatCount;
        public List<Integer> playerTimes;
        public List<List<Integer>> aiTimes;
    }

    // The name of the save file
    private static final String SAVE_NAME = "state.sav";
    // Reference to main game
    private final SceneMainGame mainGame;

    /** Construct a new Save Manager **/
    SaveManager(SceneMainGame mainGame) {
        this.mainGame = mainGame;
    }

    /** Reads an integer by concatenating 4 bytes **/
    private int readInt(BufferedInputStream fs) throws IOException {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; ++i) {
            int result = fs.read();
            if (result == -1) throw new IOException("The reader tried to read data that wasn't there");
            b[i] = (byte) result;
        }
        return ByteBuffer.wrap(b).getInt();
    }

    /**
     * Loads the game state into the main game
     * @return True if the load is successful, or false otherwise
     */
    boolean loadState() {
        // If the file doesn't exist, error out
        File file = new File(SAVE_NAME);
        if (!file.exists())
        {
            System.out.println("Save file does not exist");
            return false;
        }
        // If the file is too short (current leg + boat count + player time 1 + checksum == 16 bytes), error out
        if (file.length() < 16) {
            return false;
        }
        // Try to do this...
        try {
            // Open the file for reading
            FileInputStream fs = new FileInputStream(SAVE_NAME);
            BufferedInputStream inputStream = new BufferedInputStream(fs);

            // Create a new SaveData struct to hold the data
            SaveData sd = new SaveData();

            // Read in the leg number and boat count
            sd.legNumber = readInt(inputStream);
            sd.boatCount = readInt(inputStream);

            // Get all player times for the number of legs
            for (int i = 0; i < sd.legNumber; ++i) {
                sd.playerTimes.add(readInt(inputStream));
            }

            // Get all ai times for the number of legs and boats
            for (int i = 0; i < sd.boatCount; ++i) {
                sd.aiTimes.add(new ArrayList<>());
                for (int j = 0; j < sd.legNumber; ++j) {
                    sd.aiTimes.get(i).add(readInt(inputStream));
                }
            }

            // Read the checksum
            int checkSum = readInt(inputStream);
            // Compute the checksum and compare it to the one we've just read.  If they don't match error out
            if (checkSum != sd.hashCode()) {
                System.out.println("Save hash does not match computed hash");
                inputStream.close();
                return false;
            }

            // Close the input stream
            inputStream.close();

            // Apply the save data to the main game
            mainGame.setLegNumber(sd.legNumber);
            mainGame.getPlayer().setLegTimes(sd.playerTimes);

            int i = 0;
            for (Boat b : mainGame.getAllBoats()) {
                if (!(b instanceof PlayerBoat)) {
                    b.setLegTimes(sd.aiTimes.get(i));
                    ++i;
                }
            }

            return true;
        }
        catch (IOException ioException) {
            // Oops
            return false;
        }
    }

    /** Write an integer to a file as 4 bytes **/
    private void writeInt(BufferedOutputStream fs, int value) throws IOException {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();
        for (byte x : b) {
            fs.write(x);
        }
    }

    /**
     * Save the state of the game
     * @return True on success or false if it fails
     */
    boolean saveState() {
        // Create a new SaveData struct to hold the data
        SaveData sd = new SaveData();

        // Load up the struct with all the required data
        sd.legNumber = mainGame.getLegNumber() - 1;
        sd.boatCount = mainGame.getAllBoats().size() - 1;
        sd.playerTimes = mainGame.getPlayer().getLegTimes();
        for (Boat b : mainGame.getAllBoats()) {
            if (!(b instanceof PlayerBoat)) {
                sd.aiTimes.add(b.getLegTimes());
            }
        }

        // Compute the hash of the struct
        int checksum = sd.hashCode();

        // Try this...
        try {
            File f = new File(SAVE_NAME);
            // We don't care, as long as the file exists one way or another
            //noinspection ResultOfMethodCallIgnored
            f.createNewFile();
        } catch (IOException ioException) {
            // Probably won't happen
            return false;
        }

        try {
            // Open a new output stream in overwrite mode
            FileOutputStream fs = new FileOutputStream(SAVE_NAME, false);
            BufferedOutputStream outputStream = new BufferedOutputStream(fs);

            // Write the save data in the required order
            writeInt(outputStream, sd.legNumber);
            writeInt(outputStream, sd.boatCount);
            for (int t : sd.playerTimes)
                writeInt(outputStream, t);

            for (List<Integer> l : sd.aiTimes) {
                for (int t : l)
                    writeInt(outputStream, t);
            }

            writeInt(outputStream, checksum);

            // Close the output stream (flushes buffer)
            outputStream.close();

            // All done
            return true;

        } catch (IOException ffException) {
            // This shouldn't happen, but it's handled anyway
            System.out.println("WARNING - The file writer failed to write a save to a file that exists");
            return false;
        }
    }

}

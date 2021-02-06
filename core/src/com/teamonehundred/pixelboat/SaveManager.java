package com.teamonehundred.pixelboat;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Save File Structure
 * (Yes, it's very lazy.  No, I don't care)
 *
 * First 4 bytes is the current leg of the race
 * Second 4 bytes is number of AI boats
 * Next 12 bytes are the player times (as long)
 * Remaining bytes are times for each boat (also as long)
 */


public class SaveManager {

    private static final String SAVE_NAME = "state.sav";
    private final SceneMainGame mainGame;

    SaveManager(SceneMainGame mainGame) {
        this.mainGame = mainGame;
    }

    private int readInt(BufferedInputStream fs) throws IOException {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; ++i) {
            int result = fs.read();
            if (result == -1) throw new IOException("The reader tried to read data that wasn't there");
            b[i] = (byte) result;
        }
        return ByteBuffer.wrap(b).getInt();
    }

    boolean loadState() {
        File file = new File(SAVE_NAME);
        if (!file.exists())
        {
            System.out.println("Save file does not exist");
            return false;
        }
        //
        if (file.length() < 20) {
            return false;
        }
        try {
            FileInputStream fs = new FileInputStream(SAVE_NAME);
            BufferedInputStream inputStream = new BufferedInputStream(fs);

            int legNumber = readInt(inputStream);
            int boatCount = readInt(inputStream);

            List<Integer> playerTimes = new ArrayList<>();
            List<List<Integer>> boatTimes = new ArrayList<>();

            for (int i = 0; i < legNumber; ++i) {
                playerTimes.add(readInt(inputStream));
            }

            for (int i = 0; i < boatCount; ++i) {
                boatTimes.add(new ArrayList<>());
                for (int j = 0; j < legNumber; ++j) {
                    boatTimes.get(i).add(readInt(inputStream));
                }
            }

            inputStream.close();

            mainGame.setLegNumber(legNumber);
            mainGame.getPlayer().setLegTimes(playerTimes);

            int i = 0;
            for (Boat b : mainGame.getAllBoats()) {
                if (!(b instanceof PlayerBoat)) {
                    b.setLegTimes(boatTimes.get(i));
                    ++i;
                }
            }

            return true;
        }
        catch (IOException ioException) {
            return false;
        }
    }

    private void writeInt(BufferedOutputStream fs, int value) throws IOException {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();
        for (byte x : b) {
            fs.write(x);
        }
    }

    boolean saveState() {
        int legNumber = mainGame.getLegNumber() - 1;
        int boatCount = mainGame.getAllBoats().size() - 1;
        List<Integer> playerTimes = mainGame.getPlayer().getLegTimes();
        List<List<Integer>> boatTimes = new ArrayList<>();
        for (Boat b : mainGame.getAllBoats()) {
            if (!(b instanceof PlayerBoat)) {
                boatTimes.add(b.getLegTimes());
            }
        }

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
            FileOutputStream fs = new FileOutputStream(SAVE_NAME, false);
            BufferedOutputStream outputStream = new BufferedOutputStream(fs);

            writeInt(outputStream, legNumber);
            writeInt(outputStream, boatCount);
            for (int t : playerTimes)
                writeInt(outputStream, t);

            for (List<Integer> l : boatTimes) {
                for (int t : l)
                    writeInt(outputStream, t);
            }

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

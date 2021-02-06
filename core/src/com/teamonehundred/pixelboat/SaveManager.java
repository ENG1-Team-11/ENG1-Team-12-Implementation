package com.teamonehundred.pixelboat;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Save File Structure
 * (Yes, it's very lazy.  No, I don't care)
 *
 * First 4 bytes is the current leg of the race
 * Second 4 bytes is number of AI boats
 * Next 12 bytes are the player times (as long)
 * Remaining bytes are times for each boat (also as long)
 * Last 4 bytes is the checksum
 */


public class SaveManager {

    private class SaveData {
        SaveData() {
            legNumber = 0;
            boatCount = 0;
            playerTimes = new ArrayList<>();
            aiTimes = new ArrayList<>();
        }

        @Override
        public int hashCode() {
            return Objects.hash(legNumber, boatCount, playerTimes, aiTimes);
        }

        public Integer legNumber;
        public Integer boatCount;
        public List<Integer> playerTimes;
        public List<List<Integer>> aiTimes;
    }

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

            SaveData sd = new SaveData();

            sd.legNumber = readInt(inputStream);
            sd.boatCount = readInt(inputStream);

            for (int i = 0; i < sd.legNumber; ++i) {
                sd.playerTimes.add(readInt(inputStream));
            }

            for (int i = 0; i < sd.boatCount; ++i) {
                sd.aiTimes.add(new ArrayList<>());
                for (int j = 0; j < sd.legNumber; ++j) {
                    sd.aiTimes.get(i).add(readInt(inputStream));
                }
            }

            int checkSum = readInt(inputStream);
            if (checkSum != sd.hashCode()) {
                System.out.println("Save hash does not match computed hash");
                inputStream.close();
                return false;
            }

            inputStream.close();

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
        SaveData sd = new SaveData();

        sd.legNumber = mainGame.getLegNumber() - 1;
        sd.boatCount = mainGame.getAllBoats().size() - 1;
        sd.playerTimes = mainGame.getPlayer().getLegTimes();
        for (Boat b : mainGame.getAllBoats()) {
            if (!(b instanceof PlayerBoat)) {
                sd.aiTimes.add(b.getLegTimes());
            }
        }

        int checksum = sd.hashCode();

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

            writeInt(outputStream, sd.legNumber);
            writeInt(outputStream, sd.boatCount);
            for (int t : sd.playerTimes)
                writeInt(outputStream, t);

            for (List<Integer> l : sd.aiTimes) {
                for (int t : l)
                    writeInt(outputStream, t);
            }

            writeInt(outputStream, checksum);

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

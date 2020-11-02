package com.teamonehundred.pixelboat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class SceneMainGame implements Scene {
    int scene_id = 1;

    PlayerBoat player;
    Texture bg;

    BoatRace race;

    SceneMainGame() {
        player = new PlayerBoat(-15, 0, 30, 100, "object_placeholder.png");
        bg = new Texture("temp_background.png");
        race = new BoatRace(player);
    }

    // destructor
    protected void finalize() {
        bg.dispose();
    }

    public void draw(SpriteBatch batch) {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.getCamera().update();
        batch.setProjectionMatrix(player.getCamera().combined);

        batch.begin();
        batch.draw(bg, -500, -70, 1000, 1000);
        race.draw(batch);
        batch.end();
    }

    public void update() {
        race.runStep();
    }

    public int getCurrentSceneID() {
        return scene_id;
    }
}

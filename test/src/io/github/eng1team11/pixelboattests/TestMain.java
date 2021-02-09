package io.github.eng1team11.pixelboattests;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.extension.Extension;

import static org.mockito.Mockito.mock;

public class TestMain implements ApplicationListener, Extension {

    private final HeadlessApplication headlessApplication;

    public TestMain() {
        HeadlessApplicationConfiguration headlessApplicationConfiguration = new HeadlessApplicationConfiguration();
        headlessApplication = new HeadlessApplication(this);
        // Lets us make textures and stuff
        Gdx.gl = mock(GL20.class);
    }

    /**
     * Called when the {@link Application} is first created.
     */
    @Override
    public void create() {

    }

    /**
     * Called when the {@link Application} is resized. This can happen at any point during a non-paused state but will never happen
     * before a call to {@link #create()}.
     *
     * @param width  the new width in pixels
     * @param height the new height in pixels
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * Called when the {@link Application} should render itself.
     */
    @Override
    public void render() {

    }

    /**
     * Called when the {@link Application} is paused, usually when it's not active or visible on-screen. An Application is also
     * paused before it is destroyed.
     */
    @Override
    public void pause() {

    }

    /**
     * Called when the {@link Application} is resumed from a paused state, usually when it regains focus.
     */
    @Override
    public void resume() {

    }

    /**
     * Called when the {@link Application} is destroyed. Preceded by a call to {@link #pause()}.
     */
    @Override
    public void dispose() {

    }
}

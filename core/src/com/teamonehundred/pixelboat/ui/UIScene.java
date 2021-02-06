package com.teamonehundred.pixelboat.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;

/**
 * A manager class for UI elements providing a layering system and identifiers for UI elements
 * It can update and draw all elements it contains in two function calls
 */
public class UIScene implements UIElement {

    private boolean mouseReleased = false;
    HashMap<Integer, HashMap<String, UIElement>> _sceneObjects;

    /** Construct a new UIScene **/
    public UIScene() {
        _sceneObjects = new HashMap<>();
    }

    /**
     * Draw all the elements in the scene
     * @param batch The SpriteBatch to draw the element to
     */
    @Override
    public void draw(SpriteBatch batch) {
        for (HashMap<String, UIElement> hm: _sceneObjects.values()) {
           for (UIElement e : hm.values()) {
               e.draw(batch);
           }
        }
    }

    /**
     * Update all elements in the scene
     * @param mouseX The x position of the cursor
     * @param mouseY The y position of the cursor
     */
    @Override
    public void update(final float mouseX, final float mouseY) {
        // Lock input until the cursor is released for the first time
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            mouseReleased = true;
        }
        // Don't update elements until the mouse is released
        if (mouseReleased) {
            for (HashMap<String, UIElement> hm : _sceneObjects.values()) {
                for (UIElement e : hm.values()) {
                    e.update(mouseX, mouseY);
                }
            }
        }
    }

    /**
     * Add an element to the scene
     * @param layer The layer to add the element to
     * @param identifier The identifier for the element
     * @param element The element to add
     */
    public void addElement(int layer, String identifier, UIElement element) {
        _sceneObjects.computeIfAbsent(layer, k -> new HashMap<>());
        _sceneObjects.get(layer).put(identifier, element);
    }

    /** Remove all objects from the scene **/
    public void clear() {
        _sceneObjects.clear();
    }

    /** Lock the scene until the mouse is released **/
    public void lockScene() {
        mouseReleased = false;
    }
}

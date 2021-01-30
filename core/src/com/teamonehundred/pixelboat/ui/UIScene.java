package com.teamonehundred.pixelboat.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;

public class UIScene implements UIElement {

    HashMap<Integer, HashMap<String, UIElement>> _sceneObjects;

    public UIScene() {
        _sceneObjects = new HashMap<>();
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (HashMap<String, UIElement> hm: _sceneObjects.values()) {
           for (UIElement e : hm.values()) {
               e.draw(batch);
           }
        }
    }

    @Override
    public void update(final float mouseX, final float mouseY) {
        for (HashMap<String, UIElement> hm: _sceneObjects.values()) {
            for (UIElement e : hm.values()) {
                e.update(mouseX, mouseY);
            }
        }
    }

    public void addElement(int layer, String identifier, UIElement element) {
        _sceneObjects.computeIfAbsent(layer, k -> new HashMap<>());
        _sceneObjects.get(layer).put(identifier, element);
    }

    public void removeElement(int layer, String identifier) {
        _sceneObjects.get(layer).remove(identifier);
    }

    public UIElement getElement(int layer, String identifier) {
        return _sceneObjects.get(layer).get(identifier);
    }
}

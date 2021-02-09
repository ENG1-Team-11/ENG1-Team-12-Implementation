package com.teamonehundred.pixelboat.util;

import com.teamonehundred.pixelboat.CollisionObject;

import java.util.HashSet;
import java.util.Set;

/** Nodes store CollisionTree data **/
public class Node implements ICollisionTreeNode {

    private final Set<CollisionObject> collisionObjects;

    public Node() {
        collisionObjects = new HashSet<>();
    }

    /** Gets the contents of the node **/
    @Override
    public Set<CollisionObject> get(float x, float y) {
        return collisionObjects;
    }

    /** Adds object v to the quadtree **/
    @Override
    public void add(float x, float y, CollisionObject v) {
        collisionObjects.add(v);
    }

    /** Removes CollisionObject v from the quadtree **/
    @Override
    public void remove(float x, float y, CollisionObject v) {
        collisionObjects.remove(v);
    }

    /**
     * Removes all objects from the quadtree
     **/
    @Override
    public void clear() {
        collisionObjects.clear();
    }
}

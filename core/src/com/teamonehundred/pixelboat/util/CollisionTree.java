package com.teamonehundred.pixelboat.util;


import com.teamonehundred.pixelboat.CollisionObject;

import java.util.Set;

/**
 * QuadTree type allows storing 2D data and efficiently looking it up
 */
public class CollisionTree implements ICollisionTreeNode {

    ICollisionTreeNode topLeft;
    ICollisionTreeNode topRight;
    ICollisionTreeNode bottomLeft;
    ICollisionTreeNode bottomRight;

    public final static float MIN_WIDTH = 200.0f;
    public final static float MIN_HEIGHT = 200.0f;

    private final float halfWidth;
    private final float halfHeight;
    private final float x;
    private final float y;

    public CollisionTree(float width, float height, float x, float y) {
        float w = Math.max(MIN_WIDTH, width);
        float h = Math.max(MIN_HEIGHT, height);

        this.halfWidth = w * 0.5f;
        this.halfHeight = h * 0.5f;

        this.x = x;
        this.y = y;

        if (halfWidth > MIN_WIDTH && halfHeight > MIN_HEIGHT) {
            topLeft = new CollisionTree(halfWidth, halfHeight, x, y + halfHeight);
            topRight = new CollisionTree(halfWidth, halfHeight, x + halfWidth, y + halfHeight );
            bottomLeft = new CollisionTree(halfWidth, halfHeight, x, y);
            bottomRight = new CollisionTree(halfWidth, halfHeight, x + halfWidth, y);
        }
        else {
            topLeft = new Node();
            topRight = new Node();
            bottomLeft = new Node();
            bottomRight = new Node();
        }
    }

    private ICollisionTreeNode resolveObjectPosition(float x, float y) {
        if (x > this.x + halfWidth) {
            if (y > this.y + halfHeight) {
                return topRight;
            }
            return bottomRight;
        }
        else if (y > y + halfHeight) {
            return topLeft;
        }
        return bottomLeft;
    }

    /** Gets the contents of the node at (x,y) **/
    @Override
    public Set<CollisionObject> get(float x, float y) {
        return resolveObjectPosition(x,y).get(x,y);
    }

    /** Adds object v to the quadtree at (x,y) **/
    @Override
    public void add(float x, float y, CollisionObject v) {
        resolveObjectPosition(x,y).add(x,y,v);
    }

    /** Removes object v from the quadtree **/
    @Override
    public void remove(float x, float y, CollisionObject v) {
        resolveObjectPosition(x,y).remove(x,y,v);
    }

    /**
     * Removes all objects from the quadtree
     **/
    @Override
    public void clear() {
        topLeft.clear();
        topRight.clear();
        bottomLeft.clear();
        bottomRight.clear();
    }
}

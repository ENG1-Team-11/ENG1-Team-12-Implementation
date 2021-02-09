package com.teamonehundred.pixelboat.util;

import com.teamonehundred.pixelboat.CollisionObject;

import java.util.Set;

/** Interface for implementing a quadtree **/
public interface ICollisionTreeNode {

    /** Gets the contents of the node at (x,y) **/
    Set<CollisionObject> get(float x, float y);

    /** Adds object v to the quadtree at (x,y) **/
    void add (float x, float y, CollisionObject v);

    /** Removes object v from the quadtree **/
    void remove(float x, float y, CollisionObject v);

    /** Removes all objects from the quadtree **/
    void clear();

}

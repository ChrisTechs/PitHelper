/*
 * Original Clay Library Copyright (c) 2024 Nic Barker
 * Licensed under the zlib/libpng license.
 *
 * See the LICENSE.md file in the root of this repository for the
 * full zlib/libpng license text.
 *
 * Note: This source file has been altered from the original Clay
 * distribution. The modifications are released into the public domain.
 */
package io.github.christechs.clayj.core;

import io.github.christechs.clayj.math.BoundingBox;
import io.github.christechs.clayj.math.Dimensions;
import io.github.christechs.clayj.math.Vector2;

public class ScrollContainerDataInternal {
    public BoundingBox boundingBox = new BoundingBox(0f, 0f, 0f, 0f);
    public Dimensions contentSize = new Dimensions(0f, 0f);
    public Vector2 scrollOrigin = new Vector2(0f, 0f);
    public Vector2 pointerOrigin = new Vector2(0f, 0f);
    public Vector2 scrollMomentum = new Vector2(0f, 0f);
    public Vector2 scrollPosition = new Vector2(0f, 0f);
    public Vector2 previousDelta = new Vector2(0f, 0f);
    public LayoutElement layoutElement;
    public float momentumTime = 0f;
    public int elementId = 0;
    public boolean openThisFrame = false;
    public boolean pointerScrollActive = false;

    public void reset() {
        layoutElement = null;
        boundingBox = new BoundingBox(0f, 0f, 0f, 0f);
        contentSize = new Dimensions(0f, 0f);
        scrollOrigin = new Vector2(0f, 0f);
        pointerOrigin = new Vector2(0f, 0f);
        scrollMomentum = new Vector2(0f, 0f);
        scrollPosition = new Vector2(0f, 0f);
        previousDelta = new Vector2(0f, 0f);
        momentumTime = 0f;
        elementId = 0;
        openThisFrame = false;
        pointerScrollActive = false;
    }
}
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

public class LayoutElementHashMapItem {
    public final ElementId elementId = new ElementId();
    public BoundingBox boundingBox = new BoundingBox(0f, 0f, 0f, 0f);
    public LayoutElement layoutElement;
    public int generation;
    public int idAlias;
    public int nextIndex = -1;
    public int elementIndex;

    public Runnable onHoverFunction;

    public void reset() {
        boundingBox = new BoundingBox(0f, 0f, 0f, 0f);
        elementId.reset();
        layoutElement = null;
        generation = 0;
        idAlias = 0;
        nextIndex = -1;
        onHoverFunction = null;
        elementIndex = -1;
    }
}
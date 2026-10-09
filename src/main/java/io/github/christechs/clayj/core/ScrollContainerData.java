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

import io.github.christechs.clayj.config.ScrollConfigBuilder;
import io.github.christechs.clayj.math.Dimensions;
import io.github.christechs.clayj.math.Vector2;

public class ScrollContainerData {
    public Dimensions scrollContainerDimensions = new Dimensions(0f, 0f);
    public Dimensions contentDimensions = new Dimensions(0f, 0f);
    public Vector2 scrollPosition = new Vector2(0f, 0f);
    public ScrollConfigBuilder config;
    public boolean found;

    public void set(ScrollContainerData other) {
        this.scrollContainerDimensions = other.scrollContainerDimensions;
        this.contentDimensions = other.contentDimensions;
        this.scrollPosition = other.scrollPosition;
        this.config = other.config;
        this.found = other.found;
    }

    public void reset() {
        scrollPosition = new Vector2(0f, 0f);
        scrollContainerDimensions = new Dimensions(0f, 0f);
        contentDimensions = new Dimensions(0f, 0f);
        config = null;
        found = false;
    }
}
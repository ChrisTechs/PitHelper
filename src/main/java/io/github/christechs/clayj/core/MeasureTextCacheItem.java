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

import io.github.christechs.clayj.math.Dimensions;

public class MeasureTextCacheItem {
    public Dimensions unwrappedDimensions = new Dimensions(0f, 0f);
    public int measureWordsStartIndex = 0;
    public boolean containsNewlines = false;

    public int id = 0;
    public int nextIndex = 0;
    public int generation = 0;

    public void set(MeasureTextCacheItem other) {
        this.unwrappedDimensions = other.unwrappedDimensions;
        this.measureWordsStartIndex = other.measureWordsStartIndex;
        this.containsNewlines = other.containsNewlines;
        this.id = other.id;
        this.nextIndex = other.nextIndex;
        this.generation = other.generation;
    }

    public void reset() {
        unwrappedDimensions = new Dimensions(0f, 0f);
        measureWordsStartIndex = 0;
        containsNewlines = false;
        id = 0;
        nextIndex = 0;
        generation = 0;
    }
}
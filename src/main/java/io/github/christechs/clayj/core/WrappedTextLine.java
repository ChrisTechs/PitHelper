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

public class WrappedTextLine {
    public Dimensions dimensions = new Dimensions(0f, 0f);
    public int lineStart;
    public int lineLength;

    public void reset() {
        dimensions = new Dimensions(0f, 0f);
        lineStart = 0;
        lineLength = 0;
    }
}
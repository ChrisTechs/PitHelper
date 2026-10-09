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

public class TextElementData {
    public Dimensions preferredDimensions = new Dimensions(0f, 0f);
    public CharSequence text;
    public int elementIndex = -1;
    public int wrappedLinesStart = 0;
    public int wrappedLinesLength = 0;

    public void reset() {
        text = null;
        preferredDimensions = new Dimensions(0f, 0f);
        elementIndex = -1;
        wrappedLinesStart = 0;
        wrappedLinesLength = 0;
    }
}
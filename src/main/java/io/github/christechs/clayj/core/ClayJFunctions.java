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

import io.github.christechs.clayj.config.TextConfigBuilder;
import io.github.christechs.clayj.enums.ClayJError;
import io.github.christechs.clayj.math.Dimensions;
import io.github.christechs.clayj.math.Vector2;

public interface ClayJFunctions {
    @FunctionalInterface
    interface MeasureTextFunction {
        Dimensions measure(CharSequence text, int startOffset, int length, TextConfigBuilder config);
    }

    @FunctionalInterface
    interface ErrorHandler {
        void handleError(ClayJError errorType);
    }

    @FunctionalInterface
    interface QueryScrollOffsetFunction {
        Vector2 query(int elementId);
    }
}
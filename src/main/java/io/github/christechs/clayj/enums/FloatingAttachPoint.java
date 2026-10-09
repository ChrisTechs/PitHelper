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

package io.github.christechs.clayj.enums;

public enum FloatingAttachPoint {
    LEFT_TOP,
    LEFT_CENTER,
    LEFT_BOTTOM,
    CENTER_TOP,
    CENTER_CENTER,
    CENTER_BOTTOM,
    RIGHT_TOP,
    RIGHT_CENTER,
    RIGHT_BOTTOM;

    public boolean attachLeft() {
        return this == LEFT_TOP || this == LEFT_CENTER || this == LEFT_BOTTOM;
    }

    public boolean attachRight() {
        return this == RIGHT_TOP || this == RIGHT_CENTER || this == RIGHT_BOTTOM;
    }

    public boolean attachHorizontalCenter() {
        return this == CENTER_TOP || this == CENTER_CENTER || this == CENTER_BOTTOM;
    }

    public boolean attachTop() {
        return this == LEFT_TOP || this == CENTER_TOP || this == RIGHT_TOP;
    }

    public boolean attachBottom() {
        return this == LEFT_BOTTOM || this == CENTER_BOTTOM || this == RIGHT_BOTTOM;
    }

    public boolean attachVerticalCenter() {
        return this == LEFT_CENTER || this == CENTER_CENTER || this == RIGHT_CENTER;
    }
}
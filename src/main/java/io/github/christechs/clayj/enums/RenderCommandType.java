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

public enum RenderCommandType {
    NONE,
    RECTANGLE,
    BORDER,
    TEXT,
    IMAGE,
    SCISSOR_START,
    SCISSOR_END,
    OVERLAY_COLOR_START,
    OVERLAY_COLOR_END,
    CUSTOM
}
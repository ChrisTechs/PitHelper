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

import io.github.christechs.clayj.math.Vector2;

public class LayoutElementTreeNode {
    public Vector2 position = new Vector2(0f, 0f);
    public Vector2 nextChildOffset = new Vector2(0f, 0f);
    public LayoutElement layoutElement;
}
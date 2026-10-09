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

import io.github.christechs.clayj.enums.RenderCommandType;
import io.github.christechs.clayj.math.BoundingBox;

public class RenderCommand {
    public final RenderData renderData = new RenderData();
    public BoundingBox boundingBox = new BoundingBox(0f, 0f, 0f, 0f);
    public Object userData;
    public int id;
    public short zIndex;
    public RenderCommandType commandType = RenderCommandType.NONE;

    public void set(RenderCommand other) {
        this.boundingBox = other.boundingBox;
        this.renderData.set(other.renderData);
        this.userData = other.userData;
        this.id = other.id;
        this.zIndex = other.zIndex;
        this.commandType = other.commandType;
    }

    public void reset() {
        boundingBox = new BoundingBox(0f, 0f, 0f, 0f);
        renderData.reset();
        userData = null;
        id = 0;
        zIndex = 0;
        commandType = RenderCommandType.NONE;
    }
}
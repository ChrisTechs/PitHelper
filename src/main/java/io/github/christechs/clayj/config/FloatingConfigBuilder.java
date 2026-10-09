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
package io.github.christechs.clayj.config;

import io.github.christechs.clayj.enums.AttachToElement;
import io.github.christechs.clayj.enums.FloatingAttachPoint;
import io.github.christechs.clayj.enums.PointerCaptureMode;
import io.github.christechs.clayj.math.Dimensions;
import io.github.christechs.clayj.math.Vector2;

public final class FloatingConfigBuilder implements ConfigBuilder {
    public Vector2 offset = new Vector2(0f, 0f);
    public Dimensions expand = new Dimensions(0f, 0f);
    public int parentId = 0;
    public short zIndex = 0;
    public FloatingAttachPoint attachElement = FloatingAttachPoint.LEFT_TOP;
    public FloatingAttachPoint attachParent = FloatingAttachPoint.LEFT_TOP;
    public PointerCaptureMode captureMode = PointerCaptureMode.CAPTURE;
    public AttachToElement attachTo = AttachToElement.NONE;

    public FloatingConfigBuilder set(FloatingConfigBuilder other) {
        this.offset = other.offset;
        this.expand = other.expand;
        this.parentId = other.parentId;
        this.zIndex = other.zIndex;
        this.attachElement = other.attachElement;
        this.attachParent = other.attachParent;
        this.captureMode = other.captureMode;
        this.attachTo = other.attachTo;
        return this;
    }

    public FloatingConfigBuilder offset(float x, float y) {
        this.offset = new Vector2(x, y);
        return this;
    }

    public FloatingConfigBuilder expand(float width, float height) {
        this.expand = new Dimensions(width, height);
        return this;
    }

    public FloatingConfigBuilder parentId(int parentId) {
        this.parentId = parentId;
        return this;
    }

    public FloatingConfigBuilder zIndex(short zIndex) {
        this.zIndex = zIndex;
        return this;
    }

    public FloatingConfigBuilder attach(FloatingAttachPoint element, FloatingAttachPoint parent) {
        this.attachElement = element;
        this.attachParent = parent;
        return this;
    }

    public FloatingConfigBuilder captureMode(PointerCaptureMode mode) {
        this.captureMode = mode;
        return this;
    }

    public FloatingConfigBuilder attachTo(AttachToElement target) {
        this.attachTo = target;
        return this;
    }

    public FloatingConfigBuilder attachTo(AttachToElement target, int parentId) {
        this.attachTo = target;
        this.parentId = parentId;
        return this;
    }

    public void reset() {
        this.offset = new Vector2(0f, 0f);
        this.expand = new Dimensions(0f, 0f);
        this.parentId = 0;
        this.zIndex = 0;
        this.attachElement = FloatingAttachPoint.LEFT_TOP;
        this.attachParent = FloatingAttachPoint.LEFT_TOP;
        this.captureMode = PointerCaptureMode.CAPTURE;
        this.attachTo = AttachToElement.NONE;
    }
}
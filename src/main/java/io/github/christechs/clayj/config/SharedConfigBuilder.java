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

import io.github.christechs.clayj.math.Color;
import io.github.christechs.clayj.math.CornerRadius;

public final class SharedConfigBuilder implements ConfigBuilder {
    public Color backgroundColor = new Color(0f, 0f, 0f, 0f);
    public CornerRadius cornerRadius = new CornerRadius(0f, 0f, 0f, 0f);
    public Color overlayColor = new Color(0f, 0f, 0f, 0f);
    public boolean hasBackgroundColor = false;
    public boolean hasCornerRadius = false;
    public Object userData;
    public boolean hasOverlayColor = false;

    public float aspectRatio;

    public SharedConfigBuilder set(SharedConfigBuilder other) {
        this.hasBackgroundColor = other.hasBackgroundColor;
        if (other.hasBackgroundColor) this.backgroundColor = other.backgroundColor;

        this.hasOverlayColor = other.hasOverlayColor;
        if (other.hasOverlayColor) this.overlayColor = other.overlayColor;

        this.hasCornerRadius = other.hasCornerRadius;
        if (other.hasCornerRadius) this.cornerRadius = other.cornerRadius;

        this.aspectRatio = other.aspectRatio;
        this.userData = other.userData;
        return this;
    }

    public SharedConfigBuilder bg(Color color) {
        this.backgroundColor = color;
        this.hasBackgroundColor = true;
        return this;
    }

    public SharedConfigBuilder radius(CornerRadius radius) {
        this.cornerRadius = radius;
        this.hasCornerRadius = true;
        return this;
    }

    public SharedConfigBuilder radius(float all) {
        this.cornerRadius = new CornerRadius(all, all, all, all);
        this.hasCornerRadius = true;
        return this;
    }

    public SharedConfigBuilder userData(Object data) {
        this.userData = data;
        return this;
    }

    public void reset() {
        hasBackgroundColor = false;
        hasCornerRadius = false;
        hasOverlayColor = false;
        userData = null;
        aspectRatio = 0f;
        backgroundColor = new Color(0f, 0f, 0f, 0f);
        cornerRadius = new CornerRadius(0f, 0f, 0f, 0f);
        overlayColor = new Color(0f, 0f, 0f, 0f);
    }
}
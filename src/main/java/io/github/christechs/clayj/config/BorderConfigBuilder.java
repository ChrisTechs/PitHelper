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

import io.github.christechs.clayj.math.BorderWidth;
import io.github.christechs.clayj.math.Color;

public final class BorderConfigBuilder implements ConfigBuilder {
    public Color color = new Color(255f, 255f, 255f, 255f);
    public BorderWidth width = new BorderWidth(0, 0, 0, 0, 0);

    public BorderConfigBuilder set(BorderConfigBuilder other) {
        this.color = other.color;
        this.width = other.width;
        return this;
    }

    public BorderConfigBuilder color(Color color) {
        this.color = color;
        return this;
    }

    public BorderConfigBuilder color(float rgb) {
        this.color = new Color(rgb, rgb, rgb, this.color.a());
        return this;
    }

    public BorderConfigBuilder color(float r, float g, float b) {
        this.color = new Color(r, g, b, this.color.a());
        return this;
    }

    public BorderConfigBuilder color(float r, float g, float b, float a) {
        this.color = new Color(r, g, b, a);
        return this;
    }

    public BorderConfigBuilder r(float r) {
        this.color = new Color(r, this.color.g(), this.color.b(), this.color.a());
        return this;
    }

    public BorderConfigBuilder g(float g) {
        this.color = new Color(this.color.r(), g, this.color.b(), this.color.a());
        return this;
    }

    public BorderConfigBuilder b(float b) {
        this.color = new Color(this.color.r(), this.color.g(), b, this.color.a());
        return this;
    }

    public BorderConfigBuilder a(float a) {
        this.color = new Color(this.color.r(), this.color.g(), this.color.b(), a);
        return this;
    }

    public BorderConfigBuilder width(int all) {
        this.width = new BorderWidth(all, all, all, all, this.width.betweenChildren());
        return this;
    }

    public BorderConfigBuilder width(int x, int y) {
        this.width = new BorderWidth(x, x, y, y, this.width.betweenChildren());
        return this;
    }

    public BorderConfigBuilder width(int left, int right, int top, int bottom) {
        this.width = new BorderWidth(left, right, top, bottom, this.width.betweenChildren());
        return this;
    }

    public BorderConfigBuilder width(int left, int right, int top, int bottom, int betweenChildren) {
        this.width = new BorderWidth(left, right, top, bottom, betweenChildren);
        return this;
    }

    public BorderConfigBuilder left(int left) {
        this.width = new BorderWidth(left, this.width.right(), this.width.top(), this.width.bottom(), this.width.betweenChildren());
        return this;
    }

    public BorderConfigBuilder right(int right) {
        this.width = new BorderWidth(this.width.left(), right, this.width.top(), this.width.bottom(), this.width.betweenChildren());
        return this;
    }

    public BorderConfigBuilder top(int top) {
        this.width = new BorderWidth(this.width.left(), this.width.right(), top, this.width.bottom(), this.width.betweenChildren());
        return this;
    }

    public BorderConfigBuilder bottom(int bottom) {
        this.width = new BorderWidth(this.width.left(), this.width.right(), this.width.top(), bottom, this.width.betweenChildren());
        return this;
    }

    public BorderConfigBuilder betweenChildren(int betweenChildren) {
        this.width = new BorderWidth(this.width.left(), this.width.right(), this.width.top(), this.width.bottom(), betweenChildren);
        return this;
    }

    public void reset() {
        this.color = new Color(255f, 255f, 255f, 255f);
        this.width = new BorderWidth(0, 0, 0, 0, 0);
    }
}
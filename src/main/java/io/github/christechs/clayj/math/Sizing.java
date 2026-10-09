/*
 * This is free and unencumbered software released into the public domain.
 *
 * See the LICENSE.md file for more information, or visit <https://unlicense.org/>
 */
package io.github.christechs.clayj.math;

public class Sizing {

    public SizingAxis width, height;

    public Sizing(SizingAxis width, SizingAxis height) {
        this.width = width;
        this.height = height;
    }

    public SizingAxis width() {
        return width;
    }

    public SizingAxis height() {
        return height;
    }

    public Sizing set(Sizing other) {
        return new Sizing(other.width(), other.height());
    }

    public Sizing set(SizingAxis width, SizingAxis height) {
        return new Sizing(width, height);
    }

    public SizingAxis sizingAxis(boolean xAxis) {
        return xAxis ? width : height;
    }

    public Dimensions clamp(Dimensions d) {
        return new Dimensions(clampWidth(d.width()), clampHeight(d.height()));
    }

    public float clampWidth(float w) {
        float min = width.minMax().min();
        float max = width.minMax().max();
        if (max <= 0f) max = Float.MAX_VALUE;
        return Math.max(min, Math.min(w, max));
    }

    public float clampHeight(float h) {
        float min = height.minMax().min();
        float max = height.minMax().max();
        if (max <= 0f) max = Float.MAX_VALUE;
        return Math.max(min, Math.min(h, max));
    }
}
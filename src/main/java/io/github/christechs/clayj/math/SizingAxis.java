/*
 * This is free and unencumbered software released into the public domain.
 *
 * See the LICENSE.md file for more information, or visit <https://unlicense.org/>
 */
package io.github.christechs.clayj.math;

import io.github.christechs.clayj.enums.SizingType;

public class SizingAxis {

    public SizingAxis(SizingMinMax minMax, float percent, SizingType type) {
        this.minMax = minMax;
        this.percent = percent;
        this.type = type;
    }

    public SizingMinMax minMax() {
        return minMax;
    }

    public float percent() {
        return percent;
    }

    public SizingType type() {
        return type;
    }

    public SizingMinMax minMax;
    public float percent;
    public SizingType type;

    public SizingAxis(SizingType type) {
        this(new SizingMinMax(0f, 0f), 0f, type);
    }

    public SizingAxis(SizingType type, float percent) {
        this(new SizingMinMax(0f, 0f), percent, type);
    }

    public SizingAxis(SizingType type, float min, float max) {
        this(new SizingMinMax(min, max), 0f, type);
    }

    public SizingAxis set(SizingAxis other) {
        return new SizingAxis(other.minMax(), other.percent(), other.type());
    }

    public SizingAxis type(SizingType type) {
        return new SizingAxis(this.minMax, this.percent, type);
    }

    public SizingAxis percent(float percent) {
        return new SizingAxis(this.minMax, percent, this.type);
    }

    public SizingAxis minMax(float min, float max) {
        return new SizingAxis(new SizingMinMax(min, max), this.percent, this.type);
    }
}
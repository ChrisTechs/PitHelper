/*
 * This is free and unencumbered software released into the public domain.
 *
 * See the LICENSE.md file for more information, or visit <https://unlicense.org/>
 */
package io.github.christechs.clayj.math;

public class Dimensions {

    public float width, height;

    public Dimensions(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float width() {
        return width;
    }

    public float height() {
        return height;
    }

    public Dimensions set(float width, float height) {
        return new Dimensions(width, height);
    }

    public Dimensions add(float w, float h) {
        return new Dimensions(this.width + w, this.height + h);
    }

    public Dimensions scale(float scalar) {
        return new Dimensions(this.width * scalar, this.height * scalar);
    }

    public float sizeAxis(boolean xAxis) {
        return xAxis ? width : height;
    }

    public float aspect() {
        if (height == 0) return 0;
        return width / height;
    }
}
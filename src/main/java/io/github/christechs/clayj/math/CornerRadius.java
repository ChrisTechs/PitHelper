/*
 * This is free and unencumbered software released into the public domain.
 *
 * See the LICENSE.md file for more information, or visit <https://unlicense.org/>
 */
package io.github.christechs.clayj.math;

public class CornerRadius {

    public float topLeft, topRight, bottomLeft, bottomRight;

    public CornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }

    public float topLeft() {
        return topLeft;
    }

    public float topRight() {
        return topRight;
    }

    public float bottomLeft() {
        return bottomLeft;
    }

    public float bottomRight() {
        return bottomRight;
    }

    public CornerRadius(float radius) {
        this(radius, radius, radius, radius);
    }

    public CornerRadius set(CornerRadius other) {
        return new CornerRadius(other.topLeft(), other.topRight(), other.bottomLeft(), other.bottomRight());
    }

    public CornerRadius set(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        return new CornerRadius(topLeft, topRight, bottomLeft, bottomRight);
    }

    public CornerRadius all(float radius) {
        return new CornerRadius(radius, radius, radius, radius);
    }
}
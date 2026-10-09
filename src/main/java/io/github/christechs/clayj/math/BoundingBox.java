/*
 * This is free and unencumbered software released into the public domain.
 *
 * See the LICENSE.md file for more information, or visit <https://unlicense.org/>
 */
package io.github.christechs.clayj.math;

public class BoundingBox {
    public BoundingBox(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float y() {
        return y;
    }

    public float x() {
        return x;
    }

    public float width() {
        return width;
    }

    public float height() {
        return height;
    }

    public float x, y, width, height;

    public BoundingBox set(BoundingBox other) {
        return new BoundingBox(other.x(), other.y(), other.width(), other.height());
    }

    public BoundingBox set(float x, float y, float width, float height) {
        return new BoundingBox(x, y, width, height);
    }

    public boolean contains(float px, float py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    public boolean contains(Vector2 point) {
        return contains(point.x(), point.y());
    }

    public boolean intersects(BoundingBox other) {
        return this.x < other.x() + other.width() &&
                this.x + this.width > other.x() &&
                this.y < other.y() + other.height() &&
                this.y + this.height > other.y();
    }
}
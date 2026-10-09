/*
 * This is free and unencumbered software released into the public domain.
 *
 * See the LICENSE.md file for more information, or visit <https://unlicense.org/>
 */
package io.github.christechs.clayj.math;

public class Vector2 {

    public float x, y;


    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public Vector2 set(Vector2 other) {
        return new Vector2(other.x(), other.y());
    }

    public Vector2 set(float x, float y) {
        return new Vector2(x, y);
    }

    public Vector2 add(float x, float y) {
        return new Vector2(this.x + x, this.y + y);
    }

    public Vector2 add(Vector2 other) {
        return add(other.x(), other.y());
    }

    public Vector2 sub(float x, float y) {
        return new Vector2(this.x - x, this.y - y);
    }

    public Vector2 sub(Vector2 other) {
        return sub(other.x(), other.y());
    }

    public Vector2 scale(float scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
    }

    public float distanceTo(Vector2 other) {
        float dx = this.x - other.x();
        float dy = this.y - other.y();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
/*
 * This is free and unencumbered software released into the public domain.
 *
 * See the LICENSE.md file for more information, or visit <https://unlicense.org/>
 */
package io.github.christechs.clayj.math;

public class Padding {

    public int left, right, top, bottom;


    public Padding(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public int left() {
        return left;
    }

    public int right() {
        return right;
    }

    public int top() {
        return top;
    }

    public int bottom() {
        return bottom;
    }

    public Padding(int padding) {
        this(padding, padding, padding, padding);
    }

    public Padding set(Padding other) {
        return new Padding(other.left(), other.right(), other.top(), other.bottom());
    }

    public Padding set(int left, int right, int top, int bottom) {
        return new Padding(left, right, top, bottom);
    }

    public Padding all(int padding) {
        return new Padding(padding, padding, padding, padding);
    }

    public Padding x(int horizontalPadding) {
        return new Padding(horizontalPadding, horizontalPadding, this.top, this.bottom);
    }

    public Padding y(int verticalPadding) {
        return new Padding(this.left, this.right, verticalPadding, verticalPadding);
    }

    public int vertical() {
        return top + bottom;
    }

    public int horizontal() {
        return left + right;
    }

    public float sizeAxis(boolean xAxis) {
        return xAxis ? (float) horizontal() : (float) vertical();
    }
}
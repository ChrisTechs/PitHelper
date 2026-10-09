/*
 * This is free and unencumbered software released into the public domain.
 *
 * See the LICENSE.md file for more information, or visit <https://unlicense.org/>
 */
package io.github.christechs.clayj.math;

public class BorderWidth {

    public int left;
    public int right;
    public int top;
    public int bottom;
    public int betweenChildren;

    public BorderWidth(int betweenChildren, int bottom, int top, int right, int left) {
        this.betweenChildren = betweenChildren;
        this.bottom = bottom;
        this.top = top;
        this.right = right;
        this.left = left;
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

    public int betweenChildren() {
        return betweenChildren;
    }

    public BorderWidth set(BorderWidth other) {
        return new BorderWidth(other.left(), other.right(), other.top(), other.bottom(), other.betweenChildren());
    }

    public BorderWidth set(int left, int right, int top, int bottom, int betweenChildren) {
        return new BorderWidth(left, right, top, bottom, betweenChildren);
    }

    public BorderWidth all(int width) {
        return new BorderWidth(width, width, width, width, this.betweenChildren);
    }

    public BorderWidth between(int between) {
        return new BorderWidth(this.left, this.right, this.top, this.bottom, between);
    }
}
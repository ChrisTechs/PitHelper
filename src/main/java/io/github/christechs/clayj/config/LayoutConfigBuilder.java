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

import io.github.christechs.clayj.enums.LayoutAlignmentX;
import io.github.christechs.clayj.enums.LayoutAlignmentY;
import io.github.christechs.clayj.enums.LayoutDirection;
import io.github.christechs.clayj.enums.SizingType;
import io.github.christechs.clayj.math.Padding;
import io.github.christechs.clayj.math.Sizing;
import io.github.christechs.clayj.math.SizingAxis;

public final class LayoutConfigBuilder implements ConfigBuilder {

    public Sizing sizing = new Sizing(new SizingAxis(SizingType.FIT), new SizingAxis(SizingType.FIT));
    public Padding padding = new Padding(0);

    public int childGap = 0;
    public LayoutAlignmentX alignX = LayoutAlignmentX.LEFT;
    public LayoutAlignmentY alignY = LayoutAlignmentY.TOP;
    public LayoutDirection direction = LayoutDirection.LEFT_TO_RIGHT;

    public LayoutConfigBuilder() {
    }

    public LayoutConfigBuilder set(LayoutConfigBuilder other) {
        this.sizing = other.sizing;
        this.padding = other.padding;
        this.childGap = other.childGap;
        this.alignX = other.alignX;
        this.alignY = other.alignY;
        this.direction = other.direction;
        return this;
    }

    public LayoutConfigBuilder sizing(SizingAxis width, SizingAxis height) {
        this.sizing = new Sizing(width, height);
        return this;
    }

    public LayoutConfigBuilder widthGrow() {
        this.sizing = new Sizing(this.sizing.width().type(SizingType.GROW), this.sizing.height());
        return this;
    }

    public LayoutConfigBuilder widthFixed(float width) {
        this.sizing = new Sizing(new SizingAxis(SizingType.FIXED, width, width), this.sizing.height());
        return this;
    }

    public LayoutConfigBuilder widthPercent(float percent) {
        this.sizing = new Sizing(new SizingAxis(SizingType.PERCENT, percent), this.sizing.height());
        return this;
    }

    public LayoutConfigBuilder widthFit() {
        this.sizing = new Sizing(this.sizing.width().type(SizingType.FIT), this.sizing.height());
        return this;
    }

    public LayoutConfigBuilder heightGrow() {
        this.sizing = new Sizing(this.sizing.width(), this.sizing.height().type(SizingType.GROW));
        return this;
    }

    public LayoutConfigBuilder heightFixed(float height) {
        this.sizing = new Sizing(this.sizing.width(), new SizingAxis(SizingType.FIXED, height, height));
        return this;
    }

    public LayoutConfigBuilder heightPercent(float percent) {
        this.sizing = new Sizing(this.sizing.width(), new SizingAxis(SizingType.PERCENT, percent));
        return this;
    }

    public LayoutConfigBuilder heightFit() {
        this.sizing = new Sizing(this.sizing.width(), this.sizing.height().type(SizingType.FIT));
        return this;
    }

    public LayoutConfigBuilder alignCenterX() {
        this.alignX = LayoutAlignmentX.CENTER;
        return this;
    }

    public LayoutConfigBuilder alignCenterY() {
        this.alignY = LayoutAlignmentY.CENTER;
        return this;
    }

    public LayoutConfigBuilder alignCenter() {
        this.alignX = LayoutAlignmentX.CENTER;
        this.alignY = LayoutAlignmentY.CENTER;
        return this;
    }

    public LayoutConfigBuilder alignRight() {
        this.alignX = LayoutAlignmentX.RIGHT;
        return this;
    }

    public LayoutConfigBuilder alignBottom() {
        this.alignY = LayoutAlignmentY.BOTTOM;
        return this;
    }

    public LayoutConfigBuilder alignLeft() {
        this.alignX = LayoutAlignmentX.LEFT;
        return this;
    }

    public LayoutConfigBuilder alignTop() {
        this.alignY = LayoutAlignmentY.TOP;
        return this;
    }

    public LayoutConfigBuilder dirTopToBottom() {
        this.direction = LayoutDirection.TOP_TO_BOTTOM;
        return this;
    }

    public LayoutConfigBuilder dirLeftToRight() {
        this.direction = LayoutDirection.LEFT_TO_RIGHT;
        return this;
    }

    public LayoutConfigBuilder padding(int all) {
        this.padding = new Padding(all);
        return this;
    }

    public LayoutConfigBuilder padding(int x, int y) {
        this.padding = new Padding(x, x, y, y);
        return this;
    }

    public LayoutConfigBuilder padding(int left, int right, int top, int bottom) {
        this.padding = new Padding(left, right, top, bottom);
        return this;
    }

    public LayoutConfigBuilder gap(int gap) {
        this.childGap = gap;
        return this;
    }

    public void reset() {
        this.sizing = new Sizing(new SizingAxis(SizingType.FIT), new SizingAxis(SizingType.FIT));
        this.padding = new Padding(0);
        this.childGap = 0;
        this.alignX = LayoutAlignmentX.LEFT;
        this.alignY = LayoutAlignmentY.TOP;
        this.direction = LayoutDirection.LEFT_TO_RIGHT;
    }
}
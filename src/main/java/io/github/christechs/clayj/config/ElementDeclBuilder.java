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

import io.github.christechs.clayj.core.ElementId;
import io.github.christechs.clayj.enums.AttachToElement;
import io.github.christechs.clayj.enums.FloatingAttachPoint;
import io.github.christechs.clayj.math.Color;
import io.github.christechs.clayj.math.CornerRadius;
import io.github.christechs.clayj.math.Vector2;
import io.github.christechs.clayj.util.HashUtil;

public final class ElementDeclBuilder implements ConfigBuilder {
    private final LayoutConfigBuilder _layout = new LayoutConfigBuilder();
    private final ImageConfigBuilder _image = new ImageConfigBuilder();
    private final FloatingConfigBuilder _floating = new FloatingConfigBuilder();
    private final ScrollConfigBuilder _scroll = new ScrollConfigBuilder();
    private final BorderConfigBuilder _border = new BorderConfigBuilder();
    private final CustomConfigBuilder _custom = new CustomConfigBuilder();

    public ElementId id = new ElementId();
    public LayoutConfigBuilder layout;
    public Color backgroundColor;
    public Color overlayColor;
    public CornerRadius cornerRadius;
    public float aspectRatio;
    public ImageConfigBuilder image;
    public FloatingConfigBuilder floating;
    public ScrollConfigBuilder scroll;
    public BorderConfigBuilder border;
    public Object userData;
    public CustomConfigBuilder custom;

    public ElementDeclBuilder() {
    }

    public ElementDeclBuilder set(ElementDeclBuilder other) {
        this.id.set(other.id);

        if (other.layout != null) {
            this.layout = _layout;
            this.layout.set(other.layout);
        } else this.layout = null;

        this.backgroundColor = other.backgroundColor;
        this.overlayColor = other.overlayColor;
        this.cornerRadius = other.cornerRadius;

        if (other.image != null) {
            this.image = _image;
            this.image.set(other.image);
        } else this.image = null;

        if (other.floating != null) {
            this.floating = _floating;
            this.floating.set(other.floating);
        } else this.floating = null;

        if (other.scroll != null) {
            this.scroll = _scroll;
            this.scroll.set(other.scroll);
        } else this.scroll = null;

        if (other.border != null) {
            this.border = _border;
            this.border.set(other.border);
        } else this.border = null;

        if (other.custom != null) {
            this.custom = _custom;
            this.custom.set(other.custom);
        } else this.custom = null;

        this.aspectRatio = other.aspectRatio;
        this.userData = other.userData;
        return this;
    }

    private LayoutConfigBuilder safeLayout() {
        if (this.layout == null) this.layout = _layout;
        return this.layout;
    }

    public ElementDeclBuilder id(CharSequence idString) {
        HashUtil.hashString(idString, 0, 0, this.id);
        return this;
    }

    public ElementDeclBuilder id(ElementId id) {
        this.id.set(id);
        return this;
    }

    public ElementDeclBuilder layout(LayoutConfigBuilder layout) {
        this.layout = _layout;
        this.layout.set(layout);
        return this;
    }

    public ElementDeclBuilder widthGrow() {
        safeLayout().widthGrow();
        return this;
    }

    public ElementDeclBuilder widthFixed(float w) {
        safeLayout().widthFixed(w);
        return this;
    }

    public ElementDeclBuilder widthPercent(float p) {
        safeLayout().widthPercent(p);
        return this;
    }

    public ElementDeclBuilder widthFit() {
        safeLayout().widthFit();
        return this;
    }

    public ElementDeclBuilder heightGrow() {
        safeLayout().heightGrow();
        return this;
    }

    public ElementDeclBuilder heightFixed(float h) {
        safeLayout().heightFixed(h);
        return this;
    }

    public ElementDeclBuilder heightPercent(float p) {
        safeLayout().heightPercent(p);
        return this;
    }

    public ElementDeclBuilder heightFit() {
        safeLayout().heightFit();
        return this;
    }

    public ElementDeclBuilder sizeFixed(float w, float h) {
        safeLayout().widthFixed(w).heightFixed(h);
        return this;
    }

    public ElementDeclBuilder sizeGrow() {
        safeLayout().widthGrow().heightGrow();
        return this;
    }

    public ElementDeclBuilder padding(int all) {
        safeLayout().padding(all);
        return this;
    }

    public ElementDeclBuilder padding(int x, int y) {
        safeLayout().padding(x, y);
        return this;
    }

    public ElementDeclBuilder gap(int gap) {
        safeLayout().gap(gap);
        return this;
    }

    public ElementDeclBuilder dirLeftToRight() {
        safeLayout().dirLeftToRight();
        return this;
    }

    public ElementDeclBuilder dirTopToBottom() {
        safeLayout().dirTopToBottom();
        return this;
    }

    public ElementDeclBuilder alignCenter() {
        safeLayout().alignCenter();
        return this;
    }

    public ElementDeclBuilder alignCenterX() {
        safeLayout().alignCenterX();
        return this;
    }

    public ElementDeclBuilder alignCenterY() {
        safeLayout().alignCenterY();
        return this;
    }

    public ElementDeclBuilder alignRight() {
        safeLayout().alignRight();
        return this;
    }

    public ElementDeclBuilder alignBottom() {
        safeLayout().alignBottom();
        return this;
    }

    public ElementDeclBuilder alignLeft() {
        safeLayout().alignLeft();
        return this;
    }

    public ElementDeclBuilder alignTop() {
        safeLayout().alignTop();
        return this;
    }

    public ElementDeclBuilder bg(Color color) {
        this.backgroundColor = color;
        return this;
    }

    public ElementDeclBuilder bg(int rgb) {
        return bg(rgb, rgb, rgb, 255);
    }

    public ElementDeclBuilder bg(int r, int g, int b) {
        return bg(r, g, b, 255);
    }

    public ElementDeclBuilder bg(int r, int g, int b, int a) {
        this.backgroundColor = new Color(r, g, b, a);
        return this;
    }

    public ElementDeclBuilder overlay(Color color) {
        this.overlayColor = color;
        return this;
    }

    public ElementDeclBuilder overlay(int r, int g, int b, int a) {
        this.overlayColor = new Color(r, g, b, a);
        return this;
    }

    public ElementDeclBuilder aspectRatio(float ratio) {
        this.aspectRatio = ratio;
        return this;
    }

    public ElementDeclBuilder radius(CornerRadius cornerRadius) {
        this.cornerRadius = cornerRadius;
        return this;
    }

    public ElementDeclBuilder radius(float all) {
        this.cornerRadius = new CornerRadius(all, all, all, all);
        return this;
    }

    public ElementDeclBuilder scroll(ScrollConfigBuilder scroll) {
        this.scroll = _scroll;
        this.scroll.set(scroll);
        return this;
    }

    public ElementDeclBuilder scrollV() {
        this.scroll = _scroll;
        this.scroll.vertical(true);
        return this;
    }

    public ElementDeclBuilder scrollH() {
        this.scroll = _scroll;
        this.scroll.horizontal(true);
        return this;
    }

    public ElementDeclBuilder scrollBoth() {
        this.scroll = _scroll;
        this.scroll.both();
        return this;
    }

    public ElementDeclBuilder border(BorderConfigBuilder border) {
        this.border = _border;
        this.border.set(border);
        return this;
    }

    public ElementDeclBuilder border(Color color, int width) {
        this.border = _border;
        this.border.color(color).width(width);
        return this;
    }

    public ElementDeclBuilder border(Color color, int left, int right, int top, int bottom, int between) {
        this.border = _border;
        this.border.color(color).width(left, right, top, bottom, between);
        return this;
    }

    public ElementDeclBuilder image(ImageConfigBuilder image) {
        this.image = _image;
        this.image.set(image);
        return this;
    }

    public ElementDeclBuilder floating(FloatingConfigBuilder floating) {
        this.floating = _floating;
        this.floating.set(floating);
        return this;
    }

    public ElementDeclBuilder floating(AttachToElement attachTo, FloatingAttachPoint attachElement, FloatingAttachPoint attachParent, Vector2 offset, int zIndex) {
        return floating(attachTo, 0, attachElement, attachParent, offset, zIndex);
    }

    public ElementDeclBuilder floating(AttachToElement attachTo, int parentId, FloatingAttachPoint attachElement, FloatingAttachPoint attachParent, Vector2 offset, int zIndex) {
        this.floating = _floating;
        this.floating.attachTo(attachTo, parentId)
                .attach(attachElement, attachParent)
                .offset(offset.x(), offset.y())
                .zIndex((short) zIndex);
        return this;
    }

    public ElementDeclBuilder userData(Object userData) {
        this.userData = userData;
        return this;
    }

    public ElementDeclBuilder custom(Object customData) {
        this.custom = _custom;
        this.custom.customData = customData;
        return this;
    }

    public void reset() {
        this.id.reset();
        this.layout = null;
        this.backgroundColor = null;
        this.overlayColor = null;
        this.cornerRadius = null;
        this.aspectRatio = 0f;
        this.image = null;
        this.floating = null;
        this.scroll = null;
        this.border = null;
        this.userData = null;
        this.custom = null;

        _layout.reset();
        _image.reset();
        _floating.reset();
        _scroll.reset();
        _border.reset();
        _custom.reset();
    }
}
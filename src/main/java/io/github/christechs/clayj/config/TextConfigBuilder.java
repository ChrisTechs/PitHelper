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

import io.github.christechs.clayj.enums.TextAlignment;
import io.github.christechs.clayj.enums.TextWrapMode;
import io.github.christechs.clayj.math.Color;

public final class TextConfigBuilder implements ConfigBuilder {
    public Color textColor = new Color(255f, 255f, 255f, 255f);
    public int fontId = 0;
    public int fontSize = 16;
    public int letterSpacing = 0;
    public int lineHeight = 0;
    public TextWrapMode wrapMode = TextWrapMode.WORDS;
    public TextAlignment textAlignment = TextAlignment.LEFT;
    public boolean hashStringContents = true;

    public TextConfigBuilder set(TextConfigBuilder other) {
        this.textColor = other.textColor;
        this.fontId = other.fontId;
        this.fontSize = other.fontSize;
        this.letterSpacing = other.letterSpacing;
        this.lineHeight = other.lineHeight;
        this.wrapMode = other.wrapMode;
        this.textAlignment = other.textAlignment;
        this.hashStringContents = other.hashStringContents;
        return this;
    }

    public TextConfigBuilder color(Color textColor) {
        this.textColor = textColor;
        return this;
    }

    public TextConfigBuilder color(float rgb) {
        this.textColor = new Color(rgb, rgb, rgb, this.textColor.a());
        return this;
    }

    public TextConfigBuilder color(float r, float g, float b) {
        this.textColor = new Color(r, g, b, this.textColor.a());
        return this;
    }

    public TextConfigBuilder color(float r, float g, float b, float a) {
        this.textColor = new Color(r, g, b, a);
        return this;
    }

    public TextConfigBuilder r(float r) {
        this.textColor = new Color(r, this.textColor.g(), this.textColor.b(), this.textColor.a());
        return this;
    }

    public TextConfigBuilder g(float g) {
        this.textColor = new Color(this.textColor.r(), g, this.textColor.b(), this.textColor.a());
        return this;
    }

    public TextConfigBuilder b(float b) {
        this.textColor = new Color(this.textColor.r(), this.textColor.g(), b, this.textColor.a());
        return this;
    }

    public TextConfigBuilder a(float a) {
        this.textColor = new Color(this.textColor.r(), this.textColor.g(), this.textColor.b(), a);
        return this;
    }

    public TextConfigBuilder fontId(int fontId) {
        this.fontId = fontId;
        return this;
    }

    public TextConfigBuilder size(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public TextConfigBuilder letterSpacing(int letterSpacing) {
        this.letterSpacing = letterSpacing;
        return this;
    }

    public TextConfigBuilder lineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
        return this;
    }

    public TextConfigBuilder wrap(TextWrapMode wrapMode) {
        this.wrapMode = wrapMode;
        return this;
    }

    public TextConfigBuilder wrapWords() {
        this.wrapMode = TextWrapMode.WORDS;
        return this;
    }

    public TextConfigBuilder wrapNone() {
        this.wrapMode = TextWrapMode.NONE;
        return this;
    }

    public TextConfigBuilder align(TextAlignment alignment) {
        this.textAlignment = alignment;
        return this;
    }

    public TextConfigBuilder alignLeft() {
        this.textAlignment = TextAlignment.LEFT;
        return this;
    }

    public TextConfigBuilder alignCenter() {
        this.textAlignment = TextAlignment.CENTER;
        return this;
    }

    public TextConfigBuilder alignRight() {
        this.textAlignment = TextAlignment.RIGHT;
        return this;
    }

    public void reset() {
        this.textColor = new Color(255f, 255f, 255f, 255f);
        this.fontId = 0;
        this.fontSize = 16;
        this.letterSpacing = 0;
        this.lineHeight = 0;
        this.wrapMode = TextWrapMode.WORDS;
        this.textAlignment = TextAlignment.LEFT;
        this.hashStringContents = true;
    }
}
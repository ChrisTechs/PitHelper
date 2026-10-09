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
package io.github.christechs.clayj.core;

import io.github.christechs.clayj.math.BorderWidth;
import io.github.christechs.clayj.math.Color;
import io.github.christechs.clayj.math.CornerRadius;
import io.github.christechs.clayj.math.Dimensions;

public class RenderData {
    public Dimensions sourceDimensions = new Dimensions(0f, 0f);
    public Color backgroundColor = new Color(0f, 0f, 0f, 0f);
    public CornerRadius cornerRadius = new CornerRadius(0f, 0f, 0f, 0f);
    public Color textColor = new Color(0f, 0f, 0f, 0f);
    public Color overlayColor = new Color(0f, 0f, 0f, 0f);
    public Color borderColor = new Color(0f, 0f, 0f, 0f);
    public BorderWidth borderWidth = new BorderWidth(0, 0, 0, 0, 0);
    public CharSequence text;
    public int textStart;
    public int textLength;
    public int fontId;
    public int fontSize;
    public int letterSpacing;
    public int lineHeight;
    public Object imageData;
    public boolean scrollHorizontal;
    public boolean scrollVertical;
    public Object customData;

    public void set(RenderData other) {
        this.backgroundColor = other.backgroundColor;
        this.cornerRadius = other.cornerRadius;
        this.text = other.text;
        this.textStart = other.textStart;
        this.textLength = other.textLength;
        this.textColor = other.textColor;
        this.fontId = other.fontId;
        this.fontSize = other.fontSize;
        this.letterSpacing = other.letterSpacing;
        this.lineHeight = other.lineHeight;
        this.sourceDimensions = other.sourceDimensions;
        this.imageData = other.imageData;
        this.scrollHorizontal = other.scrollHorizontal;
        this.scrollVertical = other.scrollVertical;
        this.borderColor = other.borderColor;
        this.borderWidth = other.borderWidth;
        this.customData = other.customData;
    }

    public void reset() {
        backgroundColor = new Color(0f, 0f, 0f, 0f);
        cornerRadius = new CornerRadius(0f, 0f, 0f, 0f);
        text = null;
        textStart = 0;
        textLength = 0;
        textColor = new Color(0f, 0f, 0f, 0f);
        fontId = 0;
        fontSize = 0;
        letterSpacing = 0;
        lineHeight = 0;
        sourceDimensions = new Dimensions(0f, 0f);
        imageData = null;
        scrollHorizontal = false;
        scrollVertical = false;
        borderColor = new Color(0f, 0f, 0f, 0f);
        borderWidth = new BorderWidth(0, 0, 0, 0, 0);
        customData = null;
    }
}
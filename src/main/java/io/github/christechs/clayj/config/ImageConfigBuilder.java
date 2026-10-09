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

import io.github.christechs.clayj.math.Dimensions;

public final class ImageConfigBuilder implements ConfigBuilder {
    public Object imageData;
    public Dimensions sourceDimensions = new Dimensions(0f, 0f);

    public ImageConfigBuilder data(Object data) {
        this.imageData = data;
        return this;
    }

    public ImageConfigBuilder sourceDim(float width, float height) {
        this.sourceDimensions = new Dimensions(width, height);
        return this;
    }

    public ImageConfigBuilder sourceDim(Dimensions dim) {
        this.sourceDimensions = dim;
        return this;
    }

    public ImageConfigBuilder set(ImageConfigBuilder other) {
        return set(other.imageData, other.sourceDimensions);
    }

    public ImageConfigBuilder set(Object imageData, Dimensions sourceDimensions) {
        this.imageData = imageData;
        this.sourceDimensions = sourceDimensions;
        return this;
    }

    public void reset() {
        this.imageData = null;
        this.sourceDimensions = new Dimensions(0f, 0f);
    }
}
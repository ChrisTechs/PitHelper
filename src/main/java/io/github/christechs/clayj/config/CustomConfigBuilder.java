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

public final class CustomConfigBuilder implements ConfigBuilder {
    public Object customData;

    public CustomConfigBuilder set(CustomConfigBuilder other) {
        this.customData = other.customData;
        return this;
    }

    public CustomConfigBuilder customData(Object customData) {
        this.customData = customData;
        return this;
    }

    public void reset() {
        customData = null;
    }
}
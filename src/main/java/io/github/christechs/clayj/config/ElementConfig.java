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

import io.github.christechs.clayj.enums.ElementConfigType;

public class ElementConfig {
    public ElementConfigType type = ElementConfigType.NONE;
    public ConfigBuilder config;

    public ElementConfig set(ElementConfig other) {
        this.type = other.type;
        this.config = other.config;
        return this;
    }

    public void reset() {
        type = ElementConfigType.NONE;
        config = null;
    }
}
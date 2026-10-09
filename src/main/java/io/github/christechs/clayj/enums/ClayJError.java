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

package io.github.christechs.clayj.enums;

public enum ClayJError {
    TEXT_MEASUREMENT_FUNCTION_NOT_PROVIDED("A text measurement function wasn't provided using setMeasureTextFunction(), or the provided function was null."),

    PERCENTAGE_OVER_1("An element was configured with PERCENT sizing, but the provided percentage value was over 1.0. ClayJ expects a value between 0 and 1, i.e. 20% is 0.2."),

    FLOATING_CONTAINER_PARENT_NOT_FOUND("A floating element was declared with a parentId, but no element with that ID was found."),

    ELEMENTS_CAPACITY_EXCEEDED("ClayJ ran out of capacity while attempting to create elements. Try using initialize() with a higher maxElementCount."),

    ARENA_CAPACITY_EXCEEDED("ClayJ attempted to allocate memory in its internal pools, but ran out of capacity."),

    TEXT_MEASUREMENT_CAPACITY_EXCEEDED("ClayJ ran out of space in the internal text measurement cache."),

    DUPLICATE_ID("An element with this ID already existed on the same frame. All element IDs must be unique per frame."),

    UNBALANCED_OPEN_CLOSE("Unbalanced el() open/close calls detected. Ensure every openElement has a corresponding closeElement."),

    INTERNAL_ERROR("ClayJ encountered an internal error."),

    DUPLICATE_CONFIG("ClayJ Error: An element already has a config of this type attached."),

    CONFIG_CAPACITY_EXCEEDED("ClayJ Error: The maximum number of configs for an element has been exceeded."),

    NOT_INITIALIZED("ClayJ Error: ClayJ has not been initialized. Call ClayJ.initialize() before beginning layout.");

    private final String defaultMessage;

    ClayJError(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
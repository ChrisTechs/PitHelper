/*
 * This is free and unencumbered software released into the public domain.
 *
 * See the LICENSE.md file for more information, or visit <https://unlicense.org/>
 */
package io.github.christechs.clayj;

import io.github.christechs.clayj.core.RenderCommand;
import io.github.christechs.clayj.enums.ClayJError;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class LayoutResults implements Iterable<RenderCommand> {

    private static final RenderCommand FALLBACK_COMMAND = new RenderCommand();

    private final RenderCommand[] commands;
    private final int length;

    LayoutResults(RenderCommand[] commands, int length) {
        this.commands = commands;
        this.length = length;
    }

    public int length() {
        return length;
    }

    public RenderCommand get(int index) {
        if (index < 0 || index >= length) {
            ClayJContext context = ClayJ.getContext();
            if (context != null && context.errorHandler != null) {
                context.errorHandler.handleError(ClayJError.INTERNAL_ERROR);
            }
            return FALLBACK_COMMAND;
        }
        return commands[index];
    }

    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public Iterator<RenderCommand> iterator() {
        return new Iterator<RenderCommand>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < length;
            }

            @Override
            public RenderCommand next() {
                if (!hasNext()) throw new NoSuchElementException();
                return commands[cursor++];
            }
        };
    }

    @Override
    public String toString() {
        return "LayoutResults{length=" + length + "}";
    }
}
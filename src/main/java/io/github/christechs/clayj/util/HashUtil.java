/*
 * Original Clay Library Copyright (c) 2024 Nic Barker
 * Licensed under the zlib/libpng license.
 *
 * See the LICENSE file in the root of this repository for the
 * full zlib/libpng license text.
 *
 * Note: This source file has been altered from the original Clay
 * distribution. The modifications are released into the public domain.
 */

package io.github.christechs.clayj.util;

import io.github.christechs.clayj.core.ElementId;

public class HashUtil {

    public static void hashNumber(int key, int offset, ElementId outId) {
        int h = hashNumber(key, offset);
        outId.set(h, offset, key, "");
    }

    public static int hashNumber(int key, int offset) {
        int hash = key;
        hash += (offset + 48);
        hash += (hash << 10);
        hash ^= (hash >>> 6);

        hash += (hash << 3);
        hash ^= (hash >>> 11);
        hash += (hash << 15);
        return hash + 1;
    }

    public static void hashString(CharSequence key, int seed, ElementId outId) {
        int h = hashString(key, seed);
        outId.set(h, 0, h, key.toString());
    }

    public static int hashString(CharSequence key, int seed) {
        int hash = seed;

        for (int i = 0; i < key.length(); i++) {
            hash += key.charAt(i);
            hash += (hash << 10);
            hash ^= (hash >>> 6);
        }

        hash += (hash << 3);
        hash ^= (hash >>> 11);
        hash += (hash << 15);
        return hash + 1;
    }

    public static void hashStringWithOffset(CharSequence key, int offset, int seed, ElementId outId) {
        int h = hashStringWithOffset(key, offset, seed);

        int base = seed;
        for (int i = 0; i < key.length(); i++) {
            base += key.charAt(i);
            base += (base << 10);
            base ^= (base >>> 6);
        }
        base += (base << 3);
        base ^= (base >>> 11);
        base += (base << 15);

        outId.set(h, offset, base + 1, key.toString());
    }

    public static int hashStringWithOffset(CharSequence key, int offset, int seed) {
        int hash = 0;
        int base = seed;

        for (int i = 0; i < key.length(); i++) {
            base += key.charAt(i);
            base += (base << 10);
            base ^= (base >>> 6);
        }
        hash = base;
        hash += offset;
        hash += (hash << 10);
        hash ^= (hash >>> 6);

        hash += (hash << 3);
        hash ^= (hash >>> 11);
        hash += (hash << 15);
        return hash + 1;
    }

    public static int hashTextConfig(CharSequence text, int fontId, int fontSize, int letterSpacing) {
        int hash = 0;
        for (int i = 0; i < text.length(); i++) {
            hash += text.charAt(i);
            hash += (hash << 10);
            hash ^= (hash >>> 6);
        }
        hash += fontId;
        hash += (hash << 10);
        hash ^= (hash >>> 6);

        hash += fontSize;
        hash += (hash << 10);
        hash ^= (hash >>> 6);

        hash += letterSpacing;
        hash += (hash << 10);
        hash ^= (hash >>> 6);

        hash += (hash << 3);
        hash ^= (hash >>> 11);
        hash += (hash << 15);
        return hash + 1;
    }

    public static void hashString(CharSequence key, int offset, int seed, ElementId outId) {
        if (offset == 0) {
            hashString(key, seed, outId);
        } else {
            hashStringWithOffset(key, offset, seed, outId);
        }
    }

    public static int hashString(CharSequence key, int offset, int seed) {
        if (offset == 0) {
            return hashString(key, seed);
        } else {
            return hashStringWithOffset(key, offset, seed);
        }
    }
}
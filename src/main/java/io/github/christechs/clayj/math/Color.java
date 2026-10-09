/*
 * This is free and unencumbered software released into the public domain.
 *
 * See the LICENSE.md file for more information, or visit <https://unlicense.org/>
 */
package io.github.christechs.clayj.math;

public class Color {

    public float r() {
        return r;
    }

    public float g() {
        return g;
    }

    public float b() {
        return b;
    }

    public float a() {
        return a;
    }

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public float r, g, b, a;

    public Color set(Color other) {
        return new Color(other.r(), other.g(), other.b(), other.a());
    }

    public Color set(float r, float g, float b, float a) {
        return new Color(r, g, b, a);
    }

    public Color r(float r) {
        return new Color(r, this.g, this.b, this.a);
    }

    public Color g(float g) {
        return new Color(this.r, g, this.b, this.a);
    }

    public Color b(float b) {
        return new Color(this.r, this.g, b, this.a);
    }

    public Color a(float a) {
        return new Color(this.r, this.g, this.b, a);
    }

    public Color parseHex(String hex) {
        if (hex == null || hex.isEmpty()) return this;

        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        try {
            if (hex.length() == 6) {
                return new Color(
                        Integer.parseInt(hex.substring(0, 2), 16),
                        Integer.parseInt(hex.substring(2, 4), 16),
                        Integer.parseInt(hex.substring(4, 6), 16),
                        255f
                );
            } else if (hex.length() == 8) {
                return new Color(
                        Integer.parseInt(hex.substring(0, 2), 16),
                        Integer.parseInt(hex.substring(2, 4), 16),
                        Integer.parseInt(hex.substring(4, 6), 16),
                        Integer.parseInt(hex.substring(6, 8), 16)
                );
            }
        } catch (NumberFormatException ignored) {
        }

        return this;
    }
}
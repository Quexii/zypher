package me.eldodebug.soar.types;

public class Color {
    private float r, g, b, a;

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(float r, float g, float b) {
        this(r, g, b, 1f);
    }

    public Color(int r, int g, int b, int a) {
        this(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public Color(int argb) {
        this((argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF, (argb >> 24) & 0xFF);
    }

    public void setAlpha(float alpha) {
        a = alpha;
    }

    public void setAlpha(int alpha) {
        a = alpha / 255f;
    }

    public void setR(float r) {
        this.r = r;
    }

    public void setG(float g) {
        this.g = g;
    }

    public void setB(float b) {
        this.b = b;
    }

    public int toARGB() {
        int a = Math.round(this.a * 255);
        int r = Math.round(this.r * 255);
        int g = Math.round(this.g * 255);
        int b = Math.round(this.b * 255);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static Color Interpolate(Color from, Color to, float p, Color out) {
        out.r = from.r + (to.r - from.r) * p;
        out.g = from.g + (to.g - from.g) * p;
        out.b = from.b + (to.b - from.b) * p;
        out.a = from.a + (to.a - from.a) * p;
        return out;
    }
}

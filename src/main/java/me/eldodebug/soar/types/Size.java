package me.eldodebug.soar.types;

public final class Size {
    public float width;
    public float height;

    public Size() {
        this(0f, 0f);
    }

    public Size(float width, float height) {
        set(width, height);
    }

    public Size set(float w, float h) {
        this.width = w;
        this.height = h;
        return this;
    }

    public Size set(Size s) {
        this.width = s.width;
        this.height = s.height;
        return this;
    }

    public float aspect() {
        return this.height == 0f ? 0f : this.width / this.height;
    }

    public Size scale(float factor) {
        this.width *= factor;
        this.height *= factor;
        return this;
    }

    @Override
    public String toString() {
        return "Size[w=" + width + ", h=" + height + "]";
    }
}


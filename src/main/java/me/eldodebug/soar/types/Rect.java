package me.eldodebug.soar.types;

public final class Rect {
    public float x;
    public float y;
    public float width;
    public float height;

    public Rect() {
        this(0f, 0f, 0f, 0f);
    }

    public Rect(float x, float y, float width, float height) {
        set(x, y, width, height);
    }

    public Rect set(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        return this;
    }

    public Rect set(Rect r) {
        this.x = r.x;
        this.y = r.y;
        this.width = r.width;
        this.height = r.height;
        return this;
    }

    public Rect setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Rect setSize(float w, float h) {
        this.width = w;
        this.height = h;
        return this;
    }

    public Rect translate(float dx, float dy) {
        this.x += dx;
        this.y += dy;
        return this;
    }

    public Rect inflate(float dw, float dh) {
        this.width += dw;
        this.height += dh;
        return this;
    }

    public boolean contains(float px, float py) {
        return px >= x && py >= y && px < x + width && py < y + height;
    }

    public boolean intersects(Rect r) {
        return this.x < r.x + r.width && this.x + this.width > r.x && this.y < r.y + r.height && this.y + this.height > r.y;
    }

    public boolean intersection(Rect r, Rect dest) {
        float nx = Math.max(this.x, r.x);
        float ny = Math.max(this.y, r.y);
        float nx2 = Math.min(this.x + this.width, r.x + r.width);
        float ny2 = Math.min(this.y + this.height, r.y + r.height);
        float nw = nx2 - nx;
        float nh = ny2 - ny;
        if (nw > 0f && nh > 0f) {
            dest.x = nx;
            dest.y = ny;
            dest.width = nw;
            dest.height = nh;
            return true;
        }

        dest.x = nx;
        dest.y = ny;
        dest.width = 0f;
        dest.height = 0f;
        return false;
    }

    public Rect expandToInclude(float px, float py) {
        if (width <= 0f || height <= 0f) {
            this.x = px;
            this.y = py;
            this.width = 0f;
            this.height = 0f;
            return this;
        }
        float minX = Math.min(this.x, px);
        float minY = Math.min(this.y, py);
        float maxX = Math.max(this.x + this.width, px);
        float maxY = Math.max(this.y + this.height, py);
        this.x = minX;
        this.y = minY;
        this.width = maxX - minX;
        this.height = maxY - minY;
        return this;
    }

    public boolean isEmpty() {
        return this.width <= 0f || this.height <= 0f;
    }

    public void copyTo(Rect dest) {
        dest.x = this.x;
        dest.y = this.y;
        dest.width = this.width;
        dest.height = this.height;
    }

    public static Rect cover(Size src, Rect target, Rect dest) {
        if (src == null || target == null || dest == null) {
            throw new IllegalArgumentException("src, target and dest must be non-null");
        }
        float sw = src.width;
        float sh = src.height;
        if (sw <= 0f || sh <= 0f) {
            dest.x = target.x;
            dest.y = target.y;
            dest.width = 0f;
            dest.height = 0f;
            return dest;
        }

        float scaleX = target.width / sw;
        float scaleY = target.height / sh;
        float scale = Math.max(scaleX, scaleY);
        float rw = sw * scale;
        float rh = sh * scale;

        float rx = target.x + (target.width - rw) * 0.5f;
        float ry = target.y + (target.height - rh) * 0.5f;
        dest.x = rx;
        dest.y = ry;
        dest.width = rw;
        dest.height = rh;
        return dest;
    }

    @Override
    public String toString() {
        return "Rect[x=" + x + ", y=" + y + ", w=" + width + ", h=" + height + "]";
    }
}
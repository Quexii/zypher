package me.eldodebug.soar.gui.widget;

import me.eldodebug.soar.management.nanovg.NvRenderer;
import me.eldodebug.soar.types.Rect;
import me.eldodebug.soar.types.Size;

public abstract class Widget {
    private float x, y, width, height;
    private final Rect bounds = new Rect();

    public Widget(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        updateBounds();
    }

    public void updateBounds() {
        bounds.set(x, y, width, height);
    }

    public abstract void render(NvRenderer renderer, float mouseX, float mouseY);
    public abstract boolean mouseClicked(float mouseX, float mouseY, int button);
    public abstract boolean mouseReleased(float mouseX, float mouseY, int button);

    public boolean isHovered(float mouseX, float mouseY) {
        return bounds.contains(mouseX, mouseY);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        updateBounds();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        updateBounds();
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
        updateBounds();
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
        updateBounds();
    }

    public Rect getBounds() {
        return bounds;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBounds();
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        updateBounds();
    }

    public void setSize(Size size) {
        setSize(size.width, size.height);
    }
}

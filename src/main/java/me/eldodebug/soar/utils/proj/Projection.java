package me.eldodebug.soar.utils.proj;

import net.minecraft.util.Vec3;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Projection {
    static FloatBuffer MODELVIEW;
    static FloatBuffer PROJECTION;
    static IntBuffer VIEWPORT;
    static FloatBuffer SCREEN_COORDS = BufferUtils.createFloatBuffer(3);

    public static void update(FloatBuffer modelView, FloatBuffer projection, IntBuffer viewport) {
        MODELVIEW = modelView;
        PROJECTION = projection;
        VIEWPORT = viewport;
    }

    public static Vec3 w2s(float x, float y, float z) {
        SCREEN_COORDS.clear();

        boolean result = GLU.gluProject(
                x, y, z,
                MODELVIEW, PROJECTION, VIEWPORT, SCREEN_COORDS
        );

        if (result) {
            return new Vec3(SCREEN_COORDS.get(0), VIEWPORT.get(3) - SCREEN_COORDS.get(1), SCREEN_COORDS.get(2));
        } else {
            return new Vec3(0f, 0f, 1f);
        }
    }
}

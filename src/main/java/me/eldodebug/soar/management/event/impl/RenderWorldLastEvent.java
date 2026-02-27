package me.eldodebug.soar.management.event.impl;

import me.eldodebug.soar.management.event.Event;

public class RenderWorldLastEvent extends Event {
    public float partialTicks;

    public RenderWorldLastEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}

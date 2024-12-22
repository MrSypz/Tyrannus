package sypztep.tyrannus.common.util;

import net.minecraft.client.gui.DrawContext;

public class DrawContextUtils {
    private static void renderVerticalLine(DrawContext context, int x, int y, int height, int z, int color) {
        context.fill(x, y, x + 1, y + height, z, color);
    }

    private static void renderVerticalLine(DrawContext context, int x, int y, int height, int z, int startColor, int endColor) {
        context.fillGradient(x, y, x + 1, y + height, z, startColor, endColor);
    }

    private static void renderHorizontalLine(DrawContext context, int x, int y, int width, int z, int startColor) {
        context.fill(x, y, x + width, y + 1, z, startColor);
    }

    private static void renderRectangleBackground(DrawContext context, int x, int y, int width, int height, int z, int startColor, int endColor) {
        context.fillGradient(x, y, x + width, y + height, z, startColor,endColor);
    }
    public static void renderHorizontalLineWithCenterGradient(DrawContext context, int x, int y, int width, int height, int z, int centerColor, int edgeColor) {
        int centerX = x + width / 2;
        for (int dy = 0; dy < height; dy++) {
            for (int dx = 0; dx < width; dx++) {
                int pixelX = x + dx;
                int pixelY = y + dy;
                float distance = (float) Math.abs(pixelX - centerX);
                float normalizedDistance = Math.min(distance / ((float) width / 2), 1.0f); // Normalize distance to [0, 1]
                int color = ColorUtils.interpolateColor(centerColor, edgeColor, normalizedDistance);
                context.fill(pixelX, pixelY, pixelX + 1, pixelY + 1, z, color);
            }
        }
    }
}

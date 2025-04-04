package sypztep.tyrannus.client.screen.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sypztep.tyrannus.client.screen.panel.UIPanel;

import java.util.function.Consumer;

/**
 * A stylized button component with hover and click animations.
 */
public class Button extends UIPanel {
    private static final int DEFAULT_BUTTON_HEIGHT = 20;

    private static final int BUTTON_BG_NORMAL = 0xFF2A2A2A;
    private static final int BUTTON_BG_HOVER = 0xFF3A3A3A;
    private static final int BUTTON_BG_PRESSED = 0xFF1A1A1A;

    private static final int BUTTON_TEXT_NORMAL = 0xFFAAAAAA;
    private static final int BUTTON_TEXT_HOVER = 0xFFFFFFFF;

    private final Consumer<Button> onClick;
    private final Identifier icon;
    private boolean isPressed = false;
    private float pressAnimation = 0.0f;

    public Button(int x, int y, int width, Text label, Consumer<Button> onClick) {
        this(x, y, width, DEFAULT_BUTTON_HEIGHT, label, null, onClick);
    }

    public Button(int x, int y, int width, int height, Text label, Consumer<Button> onClick) {
        this(x, y, width, height, label, null, onClick);
    }

    public Button(int x, int y, int width, int height, Text label, Identifier icon, Consumer<Button> onClick) {
        super(x, y, width, height, label);
        this.icon = icon;
        this.onClick = onClick;
        setDrawHeader(false);
        setPadding(0);
        setDrawBorder(false);
    }

    @Override
    protected void renderContents(DrawContext context, int mouseX, int mouseY, float delta) {
        // Update press animation
        if (isPressed) {
            pressAnimation = Math.min(1.0f, pressAnimation + 0.2f);
        } else {
            pressAnimation = Math.max(0.0f, pressAnimation - 0.1f);
        }

        // Interpolate between normal, hover and pressed states
        int bgColor = interpolateColor(
                interpolateColor(BUTTON_BG_NORMAL, BUTTON_BG_HOVER, hoverAnimation),
                BUTTON_BG_PRESSED,
                pressAnimation
        );

        int textColor = interpolateColor(BUTTON_TEXT_NORMAL, BUTTON_TEXT_HOVER, hoverAnimation);

        // Draw button background with rounded corners effect
        context.fill(x, y, x + width, y + height, bgColor);

        // Add subtle gradient for 3D effect
        int topGradient = lightenColor(bgColor, 0.2f);
        int bottomGradient = darkenColor(bgColor, 0.2f);
        context.fill(x, y, x + width, y + 2, topGradient);
        context.fill(x, y + height - 2, x + width, y + height, bottomGradient);

        // Draw label
        int textWidth = textRenderer.getWidth(title);
        int textX;
        int textY = y + (height - textRenderer.fontHeight) / 2;
        int iconSize = 16;

        // Handle icon if present
        if (icon != null) {
            int totalWidth = textWidth + iconSize + 5;
            int startX = x + (width - totalWidth) / 2;

            int iconY = y + (height - iconSize) / 2;
            context.drawGuiTexture(icon, startX, iconY, iconSize, iconSize);

            textX = startX + iconSize + 5;
        } else {
            textX = x + (width - textWidth) / 2;
        }

        // Apply press animation offset
        textY += pressAnimation * 1.5f;

        // Draw text with shadow
        context.drawTextWithShadow(textRenderer, title, textX, textY, textColor);

        // Add glow effect when hovered
        if (hoverAnimation > 0.3f) {
            int glowAlpha = (int)(40 * hoverAnimation);
            int glowColor = (glowAlpha << 24) | 0xFFFFFF;

            context.fill(x - 1, y - 1, x + width + 1, y, glowColor); // Top
            context.fill(x - 1, y + height, x + width + 1, y + height + 1, glowColor); // Bottom
            context.fill(x - 1, y, x, y + height, glowColor); // Left
            context.fill(x + width, y, x + width + 1, y + height, glowColor); // Right
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY) && button == 0) {
            isPressed = true;
            if (onClick != null) {
                onClick.accept(this);
            }
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (!isMouseOver(mouseX, mouseY)) {
            isPressed = false;
        }
    }

    /**
     * Lighten a color by a factor.
     */
    private int lightenColor(int color, float factor) {
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        r = Math.min(255, (int)(r + (255 - r) * factor));
        g = Math.min(255, (int)(g + (255 - g) * factor));
        b = Math.min(255, (int)(b + (255 - b) * factor));

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Darken a color by a factor.
     */
    private int darkenColor(int color, float factor) {
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        r = Math.max(0, (int)(r * (1 - factor)));
        g = Math.max(0, (int)(g * (1 - factor)));
        b = Math.max(0, (int)(b * (1 - factor)));

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
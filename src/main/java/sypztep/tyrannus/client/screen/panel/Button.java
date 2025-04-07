package sypztep.tyrannus.client.screen.panel;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

/**
 * A stylized button component with hover and click animations.
 */
public class Button extends UIPanel {
    private static final int DEFAULT_BUTTON_HEIGHT = 20;

    // Visual states
    private static final int BUTTON_BG_NORMAL = 0xFF2A2A2A;
    private static final int BUTTON_BG_HOVER = 0xFF3A3A3A;
    private static final int BUTTON_BG_PRESSED = 0xFF1A1A1A;
    private static final int BUTTON_BG_DISABLED = 0xFF1A1A1A;

    private static final int BUTTON_TEXT_NORMAL = 0xFFAAAAAA;
    private static final int BUTTON_TEXT_HOVER = 0xFFFFFFFF;
    private static final int BUTTON_TEXT_DISABLED = 0xFF666666;

    // Button state
    private final Consumer<Button> onClick;
    private final Identifier icon;
    private boolean isPressed = false;
    private boolean isEnabled = true;
    private boolean playHoverSound = true;
    private boolean playClickSound = true;
    private boolean wasHovered = false; // Track previous hover state

    // Animations
    private float pressAnimation = 0.0f;
    private float scaleAnimation = 1.0f;

    // Visual properties
    private float glowIntensity = 1.0f;
    private float bounceIntensity = 1.0f;
    private boolean useRoundedCorners = true;
    private int cornerRadius = 4;
    private float shadowIntensity = 1.0f;

    // Constructor chain remains the same
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
        // Update animations
        updateAnimations(delta);

        // Handle hover sound
        handleHoverSound();

        // Calculate colors
        int bgColor = calculateBackgroundColor();
        int textColor = calculateTextColor();

        // Apply scale animation
        context.getMatrices().push();
        float scale = 1.0f + ((scaleAnimation - 1.0f) * bounceIntensity);
        context.getMatrices().translate(x + width/2, y + height/2, 0);
        context.getMatrices().scale(scale, scale, 1.0f);
        context.getMatrices().translate(-(x + width/2), -(y + height/2), 0);

        // Draw button background
        if (useRoundedCorners) {
            drawRoundedBackground(context, bgColor);
        } else {
            context.fill(x, y, x + width, y + height, bgColor);
        }

        // Draw gradient effect
        drawGradientEffect(context, bgColor);

        // Draw shadow
        if (shadowIntensity > 0) {
            drawShadow(context);
        }

        // Draw content (icon and text)
        drawContent(context, textColor);

        // Draw glow effect
        if (hoverAnimation > 0.3f && isEnabled) {
            drawGlowEffect(context);
        }

        context.getMatrices().pop();
    }

    private void updateAnimations(float delta) {
        if (isPressed) {
            pressAnimation = Math.min(1.0f, pressAnimation + 0.2f);
            scaleAnimation = Math.max(0.95f, 1.0f - (pressAnimation * 0.05f));
        } else {
            pressAnimation = Math.max(0.0f, pressAnimation - 0.1f);
            scaleAnimation = Math.min(1.0f, scaleAnimation + 0.1f);
        }

        // Hover scale animation
        if (isHovered && isEnabled) {
            scaleAnimation = Math.min(1.05f, scaleAnimation + 0.01f);
        } else {
            scaleAnimation = Math.max(1.0f, scaleAnimation - 0.01f);
        }
    }

    protected void handleHoverSound() {
        boolean isNowHovered = isHovered && isEnabled;
        if (isNowHovered && !wasHovered && playHoverSound) {
            client.getSoundManager().play(
                    PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_HAT, 1.8F)
            );
        }
        wasHovered = isNowHovered;
    }

    protected void drawRoundedBackground(DrawContext context, int color) {
        // Simple rounded corners implementation
        // Top-left corner
        context.fill(x + cornerRadius, y, x + width - cornerRadius, y + height, color);
        context.fill(x, y + cornerRadius, x + width, y + height - cornerRadius, color);
    }

    private void drawGradientEffect(DrawContext context, int baseColor) {
        int topGradient = lightenColor(baseColor, 0.2f);
        int bottomGradient = darkenColor(baseColor, 0.2f);

        float gradientHeight = height * 0.15f;
        context.fill(x, y, x + width, y + (int)gradientHeight, topGradient);
        context.fill(x, y + height - (int)gradientHeight, x + width, y + height, bottomGradient);
    }

    private void drawShadow(DrawContext context) {
        int shadowColor = 0x66000000;
        int shadowOffset = 2;
        context.fill(x + shadowOffset, y + shadowOffset,
                x + width + shadowOffset, y + height + shadowOffset,
                shadowColor);
    }

    private void drawContent(DrawContext context, int textColor) {
        int textWidth = textRenderer.getWidth(title);
        int textX;
        int textY = y + (height - textRenderer.fontHeight) / 2;
        int iconSize = 16;

        // Handle icon
        if (icon != null) {
            int totalWidth = textWidth + iconSize + 5;
            int startX = x + (width - totalWidth) / 2;
            int iconY = y + (height - iconSize) / 2;

            // Apply press animation to icon
            iconY += pressAnimation * 1.5f;
            context.drawGuiTexture(icon, startX, iconY, iconSize, iconSize);
            textX = startX + iconSize + 5;
        } else {
            textX = x + (width - textWidth) / 2;
        }

        // Apply press animation to text
        textY += pressAnimation * 1.5f;
        context.drawTextWithShadow(textRenderer, title, textX, textY, textColor);
    }

    private void drawGlowEffect(DrawContext context) {
        int glowAlpha = (int)(40 * hoverAnimation * glowIntensity);
        int glowColor = (glowAlpha << 24) | 0xFFFFFF;
        int glowSize = 1;

        // Draw glow border
        context.fill(x - glowSize, y - glowSize, x + width + glowSize, y, glowColor);
        context.fill(x - glowSize, y + height, x + width + glowSize, y + height + glowSize, glowColor);
        context.fill(x - glowSize, y, x, y + height, glowColor);
        context.fill(x + width, y, x + width + glowSize, y + height, glowColor);
    }

    private int calculateBackgroundColor() {
        if (!isEnabled) return BUTTON_BG_DISABLED;
        return interpolateColor(
                interpolateColor(BUTTON_BG_NORMAL, BUTTON_BG_HOVER, hoverAnimation),
                BUTTON_BG_PRESSED,
                pressAnimation
        );
    }

    private int calculateTextColor() {
        if (!isEnabled) return BUTTON_TEXT_DISABLED;
        return interpolateColor(BUTTON_TEXT_NORMAL, BUTTON_TEXT_HOVER, hoverAnimation);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY) && button == 0 && isEnabled) {
            isPressed = true;
            if (playClickSound) {
                client.getSoundManager().play(
                        PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F)
                );
            }
            if (onClick != null) {
                onClick.accept(this);
            }
            return true;
        }
        return false;
    }

    // Setter methods for customization
    public Button setEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    public Button setPlaySounds(boolean hover, boolean click) {
        this.playHoverSound = hover;
        this.playClickSound = click;
        return this;
    }

    public Button setGlowIntensity(float intensity) {
        this.glowIntensity = intensity;
        return this;
    }

    public Button setBounceIntensity(float intensity) {
        this.bounceIntensity = intensity;
        return this;
    }

    public Button setRoundedCorners(boolean rounded, int radius) {
        this.useRoundedCorners = rounded;
        this.cornerRadius = radius;
        return this;
    }

    public Button setShadowIntensity(float intensity) {
        this.shadowIntensity = intensity;
        return this;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public boolean isPlayHoverSound() {
        return playHoverSound;
    }

    public boolean isPlayClickSound() {
        return playClickSound;
    }

    public boolean isWasHovered() {
        return wasHovered;
    }

    public float getPressAnimation() {
        return pressAnimation;
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
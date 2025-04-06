package sypztep.tyrannus.client.screen.panel.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import sypztep.tyrannus.client.screen.panel.UIPanel;

import java.util.function.Consumer;

/**
 * A toggle checkbox component with animation.
 */
public class Checkbox extends UIPanel {
    private static final int CHECKBOX_SIZE = 16;
    private static final int CHECKBOX_OFF = 0xFF333333;
    private static final int CHECKBOX_ON = 0xFF5889F5;
    private static final int CHECKBOX_BORDER = 0xFF555555;
    private static final int CHECKBOX_HOVER_BORDER = 0xFF777777;

    private boolean checked = false;
    private float checkAnimation = 0.0f;
    private final Consumer<Boolean> onToggle;

    public Checkbox(int x, int y, int width, Text label, Consumer<Boolean> onToggle) {
        super(x, y, width, CHECKBOX_SIZE + 4, label);
        this.onToggle = onToggle;
        setDrawHeader(false);
        setPadding(2);
    }

    public Checkbox(int x, int y, int width, Text label, boolean initialState, Consumer<Boolean> onToggle) {
        this(x, y, width, label, onToggle);
        this.checked = initialState;
        this.checkAnimation = initialState ? 1.0f : 0.0f;
    }

    @Override
    protected void renderContents(DrawContext context, int mouseX, int mouseY, float delta) {
        // Update check animation
        if (checked) {
            checkAnimation = Math.min(1.0f, checkAnimation + 0.15f);
        } else {
            checkAnimation = Math.max(0.0f, checkAnimation - 0.15f);
        }

        // Draw checkbox box
        int boxX = getContentX();
        int boxY = getContentY() + (getContentHeight() - CHECKBOX_SIZE) / 2;

        // Background with animation
        int bgColor = interpolateColor(CHECKBOX_OFF, CHECKBOX_ON, checkAnimation);
        context.fill(boxX, boxY, boxX + CHECKBOX_SIZE, boxY + CHECKBOX_SIZE, bgColor);

        // Border with hover effect
        int borderColor = interpolateColor(CHECKBOX_BORDER, CHECKBOX_HOVER_BORDER, hoverAnimation);
        context.fill(boxX, boxY, boxX + CHECKBOX_SIZE, boxY + 1, borderColor); // Top
        context.fill(boxX, boxY + CHECKBOX_SIZE - 1, boxX + CHECKBOX_SIZE, boxY + CHECKBOX_SIZE, borderColor); // Bottom
        context.fill(boxX, boxY, boxX + 1, boxY + CHECKBOX_SIZE, borderColor); // Left
        context.fill(boxX + CHECKBOX_SIZE - 1, boxY, boxX + CHECKBOX_SIZE, boxY + CHECKBOX_SIZE, borderColor); // Right

        // Draw check mark with animation
        if (checkAnimation > 0.0f) {
            int checkmarkColor = 0xFFFFFFFF;
            int alpha = (int)(255 * checkAnimation);
            checkmarkColor = (alpha << 24) | (checkmarkColor & 0xFFFFFF);

            // Draw checkmark lines
            int padding = 3;
            int checkSize = CHECKBOX_SIZE - (padding * 2);

            // Create scale/transform matrix for checkmark animation
            context.getMatrices().push();
            float scale = 0.7f + (0.3f * checkAnimation);
            context.getMatrices().translate(
                    boxX + CHECKBOX_SIZE/2f,
                    boxY + CHECKBOX_SIZE/2f,
                    0
            );
            context.getMatrices().scale(scale, scale, 1.0f);
            context.getMatrices().translate(
                    -(boxX + CHECKBOX_SIZE/2f),
                    -(boxY + CHECKBOX_SIZE/2f),
                    0
            );

            // Short line (from left to center)
            int shortLineStartX = boxX + padding;
            int shortLineStartY = boxY + CHECKBOX_SIZE / 2 + 2;
            int shortLineEndX = boxX + CHECKBOX_SIZE / 2 - 1;
            int shortLineEndY = boxY + CHECKBOX_SIZE - padding;

            // Long line (from center to right)
            int longLineStartX = shortLineEndX;
            int longLineStartY = shortLineEndY;
            int longLineEndX = boxX + CHECKBOX_SIZE - padding;
            int longLineEndY = boxY + padding + 2;

            // Draw checkmark lines with thickness
            for (int t = 0; t < 2; t++) {
                context.fill(shortLineStartX, shortLineStartY + t, shortLineEndX, shortLineEndY + t, checkmarkColor);
                context.fill(longLineStartX, longLineStartY + t, longLineEndX, longLineEndY + t, checkmarkColor);
            }

            context.getMatrices().pop();
        }

        // Draw label
        int labelX = boxX + CHECKBOX_SIZE + 6;
        int labelY = getContentY() + (getContentHeight() - textRenderer.fontHeight) / 2;
        int textColor = interpolateColor(0xFFAAAAAA, 0xFFFFFFFF, hoverAnimation);
        context.drawTextWithShadow(textRenderer, title, labelX, labelY, textColor);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY) && button == 0) {
            checked = !checked;

            if (onToggle != null) {
                onToggle.accept(checked);
            }
            return true;
        }
        return false;
    }

    /**
     * Set the checked state.
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * Get the current checked state.
     */
    public boolean isChecked() {
        return checked;
    }
}
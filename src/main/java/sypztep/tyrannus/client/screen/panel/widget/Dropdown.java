package sypztep.tyrannus.client.screen.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sypztep.tyrannus.client.screen.panel.UIPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A dropdown select component with animation and hover effects.
 */
public class Dropdown extends UIPanel {
    private static final int DROPDOWN_BG = 0xFF2A2A2A;
    private static final int DROPDOWN_HOVER = 0xFF3A3A3A;
    private static final int DROPDOWN_TEXT = 0xFFAAAAAA;
    private static final int DROPDOWN_TEXT_HOVER = 0xFFFFFFFF;
    private static final int DROPDOWN_SELECTED = 0xFF404040;
    private static final int DROPDOWN_ACTIVE = 0xFF555555;
    private static final int DROPDOWN_BORDER = 0xFF444444;

    private final List<DropdownItem> items = new ArrayList<>();
    private boolean isOpen = false;
    private int selectedIndex = -1;
    private float openAnimation = 0.0f;
    private float itemHoverAnimation = 0.0f;
    private int hoveredItemIndex = -1;
    private int maxDropHeight = 200;
    private int itemHeight = 20;
    private Consumer<DropdownItem> onSelect;

    public Dropdown(int x, int y, int width, int height, Text title) {
        super(x, y, width, height, title);
        setDrawHeader(false);
        setPadding(0);
    }

    /**
     * Add an item to the dropdown.
     */
    public Dropdown addItem(String id, Text label, Identifier icon) {
        items.add(new DropdownItem(id, label, icon));
        if (selectedIndex == -1) {
            selectedIndex = 0;
        }
        return this;
    }

    /**
     * Add an item without an icon.
     */
    public Dropdown addItem(String id, Text label) {
        return addItem(id, label, null);
    }

    /**
     * Set the callback for when an item is selected.
     */
    public Dropdown setOnSelect(Consumer<DropdownItem> onSelect) {
        this.onSelect = onSelect;
        return this;
    }

    /**
     * Set the selected item by ID.
     */
    public void setSelected(String id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id.equals(id)) {
                selectedIndex = i;
                break;
            }
        }
    }

    /**
     * Get the currently selected item.
     */
    public DropdownItem getSelectedItem() {
        return selectedIndex >= 0 && selectedIndex < items.size() ? items.get(selectedIndex) : null;
    }

    @Override
    protected void renderContents(DrawContext context, int mouseX, int mouseY, float delta) {
        // Update open animation
        if (isOpen) {
            openAnimation = Math.min(1.0f, openAnimation + 0.15f);
        } else {
            openAnimation = Math.max(0.0f, openAnimation - 0.2f);
        }

        // Draw main dropdown button
        int bgColor = isOpen
                ? interpolateColor(DROPDOWN_BG, DROPDOWN_ACTIVE, openAnimation)
                : interpolateColor(DROPDOWN_BG, DROPDOWN_HOVER, hoverAnimation);

        // Main dropdown box
        context.fill(x, y, x + width, y + height, bgColor);
        context.fill(x, y, x + width, y + 1, DROPDOWN_BORDER); // Top
        context.fill(x, y + height - 1, x + width, y + height, DROPDOWN_BORDER); // Bottom
        context.fill(x, y, x + 1, y + height, DROPDOWN_BORDER); // Left
        context.fill(x + width - 1, y, x + width, y + height, DROPDOWN_BORDER); // Right

        // Draw selected item
        if (selectedIndex >= 0 && selectedIndex < items.size()) {
            DropdownItem item = items.get(selectedIndex);
            drawItem(context, item, x, y, width, height, false, 1.0f);
        }

        // Draw dropdown arrow
        int arrowSize = 8;
        int arrowX = x + width - arrowSize - 8;
        int arrowY = y + (height - arrowSize) / 2;

        // Rotate arrow based on open state
        float arrowRotation = openAnimation * 180;
        context.getMatrices().push();
        context.getMatrices().translate(arrowX + arrowSize/2f, arrowY + arrowSize/2f, 0);
        context.getMatrices().multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Z.rotationDegrees(arrowRotation));
        context.getMatrices().translate(-(arrowX + arrowSize/2f), -(arrowY + arrowSize/2f), 0);

        // Draw triangle arrow
        int arrowColor = interpolateColor(DROPDOWN_TEXT, DROPDOWN_TEXT_HOVER, hoverAnimation);
        drawTriangle(context, arrowX, arrowY, arrowSize, arrowColor);

        context.getMatrices().pop();

        // Draw dropdown list if open or animating
        if (openAnimation > 0) {
            int listHeight = Math.min(maxDropHeight, items.size() * itemHeight);
            int dropHeight = (int)(listHeight * openAnimation);

            if (dropHeight > 0) {
                int dropY = y + height;

                // Draw dropdown background
                context.fill(x, dropY, x + width, dropY + dropHeight, DROPDOWN_BG);
                context.fill(x, dropY + dropHeight - 1, x + width, dropY + dropHeight, DROPDOWN_BORDER); // Bottom
                context.fill(x, dropY, x + 1, dropY + dropHeight, DROPDOWN_BORDER); // Left
                context.fill(x + width - 1, dropY, x + width, dropY + dropHeight, DROPDOWN_BORDER); // Right

                // Setup scissor for dropdown list clipping
                context.enableScissor(x, dropY, x + width, dropY + dropHeight);

                // Draw items
                for (int i = 0; i < items.size(); i++) {
                    int itemY = dropY + (i * itemHeight);

                    // Skip if item is outside visible area
                    if (itemY > dropY + dropHeight) {
                        continue;
                    }

                    DropdownItem item = items.get(i);
                    boolean isSelected = i == selectedIndex;
                    boolean isHovered = mouseX >= x && mouseX < x + width &&
                            mouseY >= itemY && mouseY < itemY + itemHeight;

                    // Update hovered item index
                    if (isHovered) {
                        if (hoveredItemIndex != i) {
                            hoveredItemIndex = i;
                            itemHoverAnimation = 0.0f;
                        }
                    }

                    // Update hover animation for this item
                    float itemHover = 0.0f;
                    if (i == hoveredItemIndex) {
                        itemHoverAnimation = Math.min(1.0f, itemHoverAnimation + 0.15f);
                        itemHover = itemHoverAnimation;
                    }

                    // Draw item background
                    drawItem(context, item, x, itemY, width, itemHeight, isSelected, itemHover);
                }

                context.disableScissor();
            }
        }
    }

    /**
     * Draw a single dropdown item.
     */
    private void drawItem(DrawContext context, DropdownItem item, int x, int y, int width, int height,
                          boolean isSelected, float hoverAmount) {
        // Background color based on state
        int bgColor = isSelected
                ? interpolateColor(DROPDOWN_SELECTED, DROPDOWN_ACTIVE, hoverAmount)
                : interpolateColor(DROPDOWN_BG, DROPDOWN_HOVER, hoverAmount);

        context.fill(x + 1, y, x + width - 1, y + height, bgColor);

        // Text color with hover animation
        int textColor = interpolateColor(DROPDOWN_TEXT, DROPDOWN_TEXT_HOVER, hoverAmount);

        // Draw item content
        int textY = y + (height - textRenderer.fontHeight) / 2;
        int iconSize = 16;
        int iconPadding = 5;
        int textX;

        // Handle icon if present
        if (item.icon != null) {
            int iconY = y + (height - iconSize) / 2;
            textX = x + iconSize + (iconPadding * 2);
            context.drawGuiTexture(item.icon, x + iconPadding, iconY, iconSize, iconSize);
        } else {
            textX = x + iconPadding;
        }

        // Draw item text
        context.drawTextWithShadow(textRenderer, item.label, textX, textY, textColor);
    }

    /**
     * Draw a triangle arrow indicator.
     */
    private void drawTriangle(DrawContext context, int x, int y, int size, int color) {
        for (int i = 0; i < size; i++) {
            int lineWidth = i * 2 + 1;
            int startX = x + (size - lineWidth) / 2;
            context.fill(startX, y + i, startX + lineWidth, y + i + 1, color);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            // Check if clicking on main dropdown button
            if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
                isOpen = !isOpen;
                return true;
            }

            // Check if clicking on dropdown item
            if (isOpen) {
                int dropY = y + height;
                int listHeight = Math.min(maxDropHeight, items.size() * itemHeight);

                if (mouseX >= x && mouseX < x + width && mouseY >= dropY && mouseY < dropY + listHeight) {
                    int clickedIndex = (int)((mouseY - dropY) / itemHeight);

                    if (clickedIndex >= 0 && clickedIndex < items.size()) {
                        selectedIndex = clickedIndex;
                        isOpen = false;

                        // Call onSelect if provided
                        if (onSelect != null) {
                            onSelect.accept(items.get(selectedIndex));
                        }
                    }
                    return true;
                }
            }

            // Close dropdown if clicking outside
            if (isOpen) {
                isOpen = false;
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Set the maximum height of the dropdown when open.
     */
    public void setMaxDropHeight(int height) {
        this.maxDropHeight = height;
    }

    /**
     * Set the height of each item in the dropdown.
     */
    public void setItemHeight(int height) {
        this.itemHeight = height;
    }

    /**
     * Class representing a dropdown item.
     */
    public record DropdownItem(String id, Text label, Identifier icon) {}
}
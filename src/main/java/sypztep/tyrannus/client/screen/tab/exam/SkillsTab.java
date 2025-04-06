package sypztep.tyrannus.client.screen.tab.exam;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import sypztep.tyrannus.client.screen.panel.Button;
import sypztep.tyrannus.client.screen.panel.ScrollablePanel;
import sypztep.tyrannus.client.screen.tab.Tab;

import java.util.HashMap;
import java.util.Map;

/**
 * Tab for displaying and managing player skills in a Dark Souls-like stat allocation system.
 */
public class SkillsTab extends Tab {

    // Available skill points
    private int availablePoints = 5;

    // Skills data
    private final Map<String, SkillAttribute> attributes = new HashMap<>();

    // Reference to the scrollable panel that contains skills
    private SkillsScrollPanel skillsPanel;

    // Tooltip data
    private String hoverTooltip = null;

    public SkillsTab() {
        super("skills", Text.of("Skills"));

        // Initialize example attributes
        attributes.put("strength", new SkillAttribute("Strength", 10,
                "Increases physical damage and carrying capacity"));
        attributes.put("dexterity", new SkillAttribute("Dexterity", 8,
                "Improves attack speed and evasion"));
        attributes.put("vitality", new SkillAttribute("Vitality", 12,
                "Increases health points and stamina recovery"));
        attributes.put("intelligence", new SkillAttribute("Intelligence", 7,
                "Improves magic damage and mana capacity"));
        attributes.put("faith", new SkillAttribute("Faith", 5,
                "Enhances healing abilities and light magic"));
        attributes.put("endurance", new SkillAttribute("Endurance", 9,
                "Increases stamina and equipment load"));
        attributes.put("luck", new SkillAttribute("Luck", 6,
                "Improves item drop rates and critical hit chance"));
    }

    @Override
    protected void initPanels() {
        int panelX = 10;
        int panelY = 65; // Below nav bar
        int panelWidth = parentScreen.getWidth() - 20;
        int panelHeight = parentScreen.getHeight() - 100; // Space for nav and bottom UI

        // Create scrollable skills panel
        skillsPanel = new SkillsScrollPanel(panelX, panelY, panelWidth, panelHeight,
                Text.of("Character Attributes"));
        addPanel(skillsPanel);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Draw tooltip if hovering over an attribute
        if (hoverTooltip != null) {
            int tooltipWidth = client.textRenderer.getWidth(hoverTooltip);
            int tooltipX = Math.min(mouseX + 12, parentScreen.getWidth() - tooltipWidth - 12);
            int tooltipY = mouseY + 12;

            // Draw tooltip background
            context.fill(tooltipX - 4, tooltipY - 4,
                    tooltipX + tooltipWidth + 4, tooltipY + client.textRenderer.fontHeight + 4,
                    0xE0000000);

            // Draw tooltip text
            context.drawText(client.textRenderer, hoverTooltip, tooltipX, tooltipY, 0xFFFFFFFF, false);

            // Reset tooltip for next frame
            hoverTooltip = null;
        }
    }

    /**
     * Attempt to upgrade an attribute.
     */
    private void upgradeAttribute(String attributeId) {
        if (availablePoints > 0 && attributes.containsKey(attributeId)) {
            SkillAttribute attribute = attributes.get(attributeId);
            attribute.level++;
            availablePoints--;

            // Here you would normally save the attribute change
            // For now we just update the UI
            skillsPanel.refreshContent();
        }
    }

    /**
     * Custom scrollable panel for skills.
     */
    private class SkillsScrollPanel extends ScrollablePanel {
        private final Map<String, Button> upgradeButtons = new HashMap<>();

        public SkillsScrollPanel(int x, int y, int width, int height, Text title) {
            super(x, y, width, height, title);
            initContent();
        }

        private void initContent() {
            int itemHeight = 40;
            int headerHeight = 60;
            int contentHeight = headerHeight + (attributes.size() * itemHeight) + 20; // Extra padding
            setContentHeight(contentHeight);

            refreshContent();
        }

        public void refreshContent() {
            // Clear existing buttons
            upgradeButtons.clear();

            // Create new buttons for each attribute
            int y = 80; // Start of attributes
            int attributeHeight = 40;
            int availableWidth = getContentWidth() - (enableScrollbar ? scrollbarWidth + scrollbarPadding + 10 : 5);

            int index = 0;
            for (Map.Entry<String, SkillAttribute> entry : attributes.entrySet()) {
                String attrId = entry.getKey();

                // Create a small "+" button
                int buttonX = getContentX() + availableWidth - 40;
                int buttonY = getContentY() + y + (index * attributeHeight) + (attributeHeight - 20) / 2;

                Button upgradeButton = new Button(
                        buttonX,
                        buttonY,
                        20,
                        20,
                        Text.of("+"),
                        button -> upgradeAttribute(attrId)
                );

                // Customize button appearance
                upgradeButton.setEnabled(availablePoints > 0);
                upgradeButton.setGlowIntensity(1.5f);
                upgradeButton.setBounceIntensity(0.8f);

                upgradeButtons.put(attrId, upgradeButton);
                index++;
            }
        }

        @Override
        protected void renderScrollableContent(DrawContext context, int mouseX, int mouseY, float delta) {
            int x = getContentX();
            int y = getContentY() - (int)scrollAmount;
            int availableWidth = getContentWidth() - (enableScrollbar ? scrollbarWidth + scrollbarPadding + 10 : 5);

            // Draw header with available points
            int headerY = y + 10;
            String headerText = "CHARACTER ATTRIBUTES";
            context.drawTextWithShadow(
                    textRenderer,
                    headerText,
                    x + (availableWidth - textRenderer.getWidth(headerText)) / 2,
                    headerY,
                    0xFFFFD700
            );

            // Draw points info
            String pointsText = "Available Points: " + availablePoints;
            context.drawTextWithShadow(
                    textRenderer,
                    pointsText,
                    x + (availableWidth - textRenderer.getWidth(pointsText)) / 2,
                    headerY + 20,
                    availablePoints > 0 ? 0xFF55FF55 : 0xFFAAAAAA
            );

            // Draw attributes section
            y += 60;
            int attributeHeight = 40;

            // Draw attribute name column header
            context.drawTextWithShadow(
                    textRenderer,
                    "ATTRIBUTE",
                    x + 45,
                    y,
                    0xFFAAAAAA
            );

            // Draw level column header
            context.drawTextWithShadow(
                    textRenderer,
                    "LEVEL",
                    x + availableWidth - 100,
                    y,
                    0xFFAAAAAA
            );

            y += 20;

            // Draw separator line
            context.fill(x, y - 5, x + availableWidth, y - 4, 0x80FFFFFF);

            // Draw attributes
            int index = 0;
            for (Map.Entry<String, SkillAttribute> entry : attributes.entrySet()) {
                String attrId = entry.getKey();
                SkillAttribute attribute = entry.getValue();

                // Alternate row backgrounds
                boolean isEven = (index % 2 == 0);
                int rowBgColor = isEven ? 0x10FFFFFF : 0x20FFFFFF;
                context.fill(x, y, x + availableWidth, y + attributeHeight, rowBgColor);

                // Draw attribute name
                context.drawTextWithShadow(
                        textRenderer,
                        attribute.name,
                        x + 35,
                        y + (attributeHeight - textRenderer.fontHeight) / 2,
                        0xFFFFFFFF
                );

                // Draw attribute level
                String levelText = String.valueOf(attribute.level);
                context.drawTextWithShadow(
                        textRenderer,
                        levelText,
                        x + availableWidth - 100 + (40 - textRenderer.getWidth(levelText)) / 2,
                        y + (attributeHeight - textRenderer.fontHeight) / 2,
                        0xFFFFCC00
                );

                // Render upgrade button if points are available
                Button button = upgradeButtons.get(attrId);
                if (button != null) {
                    // Update button position based on scroll position
                    button.setX(x + availableWidth - 40);
                    button.setY(y + (attributeHeight - 20) / 2);
                    button.setEnabled(availablePoints > 0);
                    button.render(context, mouseX, mouseY, delta);
                }

                // Check if hovering over attribute name for tooltip
                if (mouseX >= x + 35 && mouseX <= x + availableWidth - 150 &&
                        mouseY >= y && mouseY <= y + attributeHeight) {
                    hoverTooltip = attribute.description;
                }

                y += attributeHeight;
                index++;
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            // Check if any of our buttons was clicked
            if (isMouseOver(mouseX, mouseY)) {
                for (Button upgradeButton : upgradeButtons.values()) {
                    if (upgradeButton.isEnabled() && upgradeButton.mouseClicked(mouseX, mouseY, button)) {
                        return true;
                    }
                }
            }

            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    /**
     * Class to store attribute data.
     */
    private static class SkillAttribute {
        String name;
        int level;
        String description;

        SkillAttribute(String name, int level, String description) {
            this.name = name;
            this.level = level;
            this.description = description;
        }
    }
}
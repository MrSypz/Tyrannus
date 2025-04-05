package sypztep.tyrannus.client.screen.base;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import sypztep.tyrannus.client.screen.panel.ScrollablePanel;
import sypztep.tyrannus.client.screen.panel.UIPanel;
import sypztep.tyrannus.client.screen.tab.Tab;
import sypztep.tyrannus.client.screen.tab.TabManager;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ScreenBase {
    protected static final int BACKGROUND_COLOR = 0xF0121212;
    protected static final int PANEL_BACKGROUND = 0xFF1A1A1A;
    protected static final int PANEL_BORDER = 0xFF424242;
    protected static final int HEADER_COLOR = 0xFFFFD700;
    protected static final int TEXT_COLOR = 0xFFFFFFFF;

    protected final List<UIPanel> panels = new ArrayList<>();
    protected final Screen screen;
    protected final IScreenBase screenBase;
    private TabManager tabManager;

    public ScreenBase(Screen screen) {
        this.screen = screen;
        this.screenBase = (IScreenBase) screen; // Cast screen to IScreenBase since all our screens implement it
        this.tabManager = new TabManager(screenBase);
    }

    public void init(int width, int height) {
        panels.clear();
        if (tabManager != null) {
            tabManager.init(10, 25, width - 20);
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render background
        context.fillGradient(0, 0, screen.width, screen.height, BACKGROUND_COLOR, BACKGROUND_COLOR);

        // Render panels
        for (UIPanel panel : panels) {
            panel.render(context, mouseX, mouseY, delta);
        }

        // Render tab content
        if (tabManager != null) {
            tabManager.render(context, mouseX, mouseY, delta);
        }
    }

    public void addPanel(UIPanel panel) {
        panels.add(panel);
    }

    public void removePanel(UIPanel panel) {
        panels.remove(panel);
    }

    public TabManager getTabManager() {
        return tabManager;
    }

    public void registerTab(Tab tab) {
        if (tabManager != null) {
            tabManager.registerTab(tab);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (UIPanel panel : panels) {
            if (panel.isMouseOver(mouseX, mouseY) && panel.handleScrolling(horizontalAmount, verticalAmount)) {
                return true;
            }
        }
        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (UIPanel panel : panels) {
            if (panel.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        for (UIPanel panel : panels) {
            if (panel instanceof ScrollablePanel scrollablePanel) {
                if (scrollablePanel.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (UIPanel panel : panels) {
            if (panel instanceof ScrollablePanel scrollablePanel) {
                scrollablePanel.mouseReleased(mouseX, mouseY, button);
            }
        }
        return false;
    }

    // Helper method to get screen width
    public int getWidth() {
        return screen.width;
    }

    // Helper method to get screen height
    public int getHeight() {
        return screen.height;
    }

    // Helper method to check if screen implements IScreenBase
    public static boolean isValidScreen(Screen screen) {
        return screen instanceof IScreenBase;
    }
}
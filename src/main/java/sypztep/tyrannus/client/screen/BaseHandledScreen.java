package sypztep.tyrannus.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import sypztep.tyrannus.client.screen.base.IScreenBase;
import sypztep.tyrannus.client.screen.base.ScreenBase;
import sypztep.tyrannus.client.screen.panel.UIPanel;
import sypztep.tyrannus.client.screen.tab.Tab;
import sypztep.tyrannus.client.screen.tab.TabManager;

@Environment(EnvType.CLIENT)
public abstract class BaseHandledScreen<T extends ScreenHandler> extends HandledScreen<T> implements IScreenBase {
    private final ScreenBase base;

    public BaseHandledScreen(T handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.base = new ScreenBase(this);
    }

    @Override
    protected void init() {
        super.init();
        base.init(width, height);
        initPanels();
    }

    protected abstract void initPanels();

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        base.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }
    @Override
    public int getHeight() {
        return height;
    }
    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public <t extends UIPanel> void addPanel(t panel) {
        base.addPanel(panel);
    }

    @Override
    public void removePanel(UIPanel panel) {
        base.removePanel(panel);
    }

    @Override
    public TabManager getTabManager() {
        return base.getTabManager();
    }

    @Override
    public void registerTab(Tab tab) {
        base.registerTab(tab);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return base.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount) ||
                super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return base.mouseClicked(mouseX, mouseY, button) ||
                super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return base.mouseDragged(mouseX, mouseY, button, dragX, dragY) ||
                super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return base.mouseReleased(mouseX, mouseY, button) ||
                super.mouseReleased(mouseX, mouseY, button);
    }
}
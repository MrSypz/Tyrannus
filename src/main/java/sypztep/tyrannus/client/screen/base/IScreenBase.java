package sypztep.tyrannus.client.screen.base;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import sypztep.tyrannus.client.screen.panel.UIPanel;
import sypztep.tyrannus.client.screen.tab.Tab;
import sypztep.tyrannus.client.screen.tab.TabManager;

@Environment(EnvType.CLIENT)
public interface IScreenBase {
    <T extends UIPanel> void addPanel(T panel);
    void removePanel(UIPanel panel);
    TabManager getTabManager();
    void registerTab(Tab tab);
    int getWidth();
    int getHeight();
}
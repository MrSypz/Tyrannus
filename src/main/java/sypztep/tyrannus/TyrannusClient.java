package sypztep.tyrannus;

import net.fabricmc.api.ClientModInitializer;
import sypztep.tyrannus.client.widget.TabWidgetRegistry;

public class TyrannusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TabWidgetRegistry.clearTabs();
    }
}

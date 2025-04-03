package sypztep.tyrannus.client.widget;

import java.util.ArrayList;
import java.util.List;

public class TabWidgetRegistry {
    private static final List<TabWidgetButton> registeredTabs = new ArrayList<>();

    public static void registerTab(TabWidgetButton tab) {
        registeredTabs.add(tab);
    }

    public static TabWidgetButton[] getRegisteredTabs() {
        return registeredTabs.toArray(new TabWidgetButton[0]);
    }

    public static void clearTabs() {
        registeredTabs.clear();
    }
}
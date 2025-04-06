package sypztep.tyrannus.client.screen;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sypztep.tyrannus.client.screen.panel.Button;
import sypztep.tyrannus.client.screen.tab.TabManager;
import sypztep.tyrannus.client.screen.tab.exam.QuestsTab;
import sypztep.tyrannus.client.screen.tab.exam.SkillsTab;

public class TestScreen extends BaseScreen {
    public TestScreen() {
        super(Text.of("Test"));
    }

    @Override
    protected void initPanels() {
        tabManager = new TabManager(this);
        tabManager.registerTab(new SkillsTab());
        tabManager.registerTab(new QuestsTab());
    }
}
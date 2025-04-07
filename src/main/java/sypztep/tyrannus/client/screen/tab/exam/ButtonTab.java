package sypztep.tyrannus.client.screen.tab.exam;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sypztep.tyrannus.client.screen.panel.Button;
import sypztep.tyrannus.client.screen.tab.Tab;

public class ButtonTab extends Tab {
    public ButtonTab() {
        super("ButtonTab", Text.literal("Button"));
    }

    @Override
    protected void initPanels() {
        Button fancyButton = new Button(
                parentScreen.width/2 - 60, parentScreen.height/2 - 10,
                120, 30,
                Text.literal("Click Me!"),
                button -> System.out.println("Clicked!")
        )
                .setGlowIntensity(1.5f)        // Stronger glow effect
                .setBounceIntensity(1.2f)      // More bounce on hover
                .setRoundedCorners(true, 4)    // Rounded corners
                .setShadowIntensity(1.0f)      // Drop shadow
                .setPlaySounds(true, true);    // Enable hover and click sounds

        panels.add(fancyButton);

        // Create a disabled button
        Button disabledButton = new Button(
                parentScreen.width/2 - 60, parentScreen.height/2 + 30,
                120, 30,
                Text.literal("Disabled"),
                null
        )
                .setEnabled(false);

        panels.add(disabledButton);

        // Create a button with an icon
        Button iconButton = new Button(
                parentScreen.width/2 - 60, parentScreen.height/2 + 70,
                120, 30,
                Text.literal("Settings"),
                Identifier.ofVanilla("icon/info"),
                button -> System.out.println("Settings clicked!")
        );

        panels.add(iconButton);
    }
}

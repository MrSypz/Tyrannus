package sypztep.tyrannus.client.screen;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sypztep.tyrannus.client.screen.panel.Button;

public class TestScreen extends BaseScreen {
    public TestScreen() {
        super(Text.of("Test"));
    }

    @Override
    protected void initPanels() {
        // Create a fancy button with all effects
        Button fancyButton = new Button(
                width/2 - 60, height/2 - 10,
                120, 30,
                Text.literal("Click Me!"),
                button -> System.out.println("Clicked!")
        )
                .setGlowIntensity(1.5f)        // Stronger glow effect
                .setBounceIntensity(1.2f)      // More bounce on hover
                .setRoundedCorners(true, 4)    // Rounded corners
                .setShadowIntensity(1.0f)      // Drop shadow
                .setPlaySounds(true, true);    // Enable hover and click sounds

        addPanel(fancyButton);

        // Create a disabled button
        Button disabledButton = new Button(
                width/2 - 60, height/2 + 30,
                120, 30,
                Text.literal("Disabled"),
                null
        )
                .setEnabled(false);

        addPanel(disabledButton);

        // Create a button with an icon
        Button iconButton = new Button(
                width/2 - 60, height/2 + 70,
                120, 30,
                Text.literal("Settings"),
               Identifier.ofVanilla("textures/item/compass_16.png"),
                button -> System.out.println("Settings clicked!")
        );

        addPanel(iconButton);
    }
}
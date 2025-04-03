package sypztep.tyrannus.mixin.core.registry.tabs.inventory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.tyrannus.client.widget.TabWidgetButton;
import sypztep.tyrannus.client.widget.TabWidgetRegistry;

@Mixin(InventoryScreen.class)
@Environment(EnvType.CLIENT)
public abstract class InventoryScreenMixin extends HandledScreen<PlayerScreenHandler> {
    @Unique
    private TabWidgetButton[] tabButtons;

    public InventoryScreenMixin(PlayerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        tabButtons = TabWidgetRegistry.getRegisteredTabs();

        if (tabButtons.length > 0) {
            setButtonCoordinates();
            for (TabWidgetButton button : tabButtons) this.addDrawableChild(button);
        }
    }


    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (tabButtons != null) for (TabWidgetButton button : tabButtons) button.render(context, mouseX, mouseY, delta);
    }


    @Unique
    private void setButtonCoordinates() {
        int baseY = this.y + 4;

        for (int index = 0; index < tabButtons.length; index++) {
            TabWidgetButton button = tabButtons[index];
            button.setX(this.x - button.getWidth());
            button.setY(baseY + index * (button.getHeight() + 1));
        }
    }
}

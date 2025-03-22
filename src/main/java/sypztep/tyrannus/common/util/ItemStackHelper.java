package sypztep.tyrannus.common.util;

import net.minecraft.item.ItemStack;

public class ItemStackHelper {
    public static boolean shouldBreak(ItemStack itemStack) {
        return itemStack.isDamageable() && itemStack.getDamage() >= itemStack.getMaxDamage();
    }

    public static boolean willBreakNextUse(ItemStack itemStack) {
        return itemStack.isDamageable() && itemStack.getDamage() >= itemStack.getMaxDamage() - 1;
    }
}

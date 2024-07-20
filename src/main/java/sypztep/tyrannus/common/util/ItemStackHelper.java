package sypztep.tyrannus.common.util;

import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public final class ItemStackHelper {
    public static NbtCompound getNbtCompound(ItemStack stack, ComponentType<NbtComponent> type) {
        NbtComponent data = stack.getOrDefault(type, NbtComponent.DEFAULT);
        return data.copyNbt();
    }
    public static NbtCompound getNbtCompound(ItemStack stack) {
        NbtComponent data = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
        return data.copyNbt();
    }
}

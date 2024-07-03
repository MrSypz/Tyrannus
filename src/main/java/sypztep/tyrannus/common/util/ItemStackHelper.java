package sypztep.tyrannus.common.util;

import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

public class ItemStackHelper {
    public static NbtCompound getNbtCompound(ItemStack stack, ComponentType<NbtComponent> type) {
        NbtCompound value = new NbtCompound();
        NbtComponent data = stack.getOrDefault(type, NbtComponent.DEFAULT);
        value = data.copyNbt();
        return value;
    }

    public NbtCompound getNbtCompound(ItemStack stack) {
        NbtCompound value = new NbtCompound();
        NbtComponent data = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
        value = data.copyNbt();
        return value;
    }
}

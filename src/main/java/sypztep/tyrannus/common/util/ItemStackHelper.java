package sypztep.tyrannus.common.util;

import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import sypztep.tyrannus.Tyrannus;

public class ItemStackHelper {
    public static NbtCompound getNbtCompound(ItemStack stack, ComponentType<NbtComponent> type) {
        NbtComponent data = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
        return data.copyNbt();
    }

    public static NbtCompound getNullableNbtCompound(ItemStack stack, ComponentType<NbtComponent> type) {
        NbtComponent data = stack.get(type);
        if (data == null) {
            getError(stack);
            throw new NullPointerException("NbtComponent is null for stack: " + stack);
        }
        return data.copyNbt();
    }

    public static NbtCompound getNbtCompound(ItemStack stack) {
        NbtComponent data = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
        return data.copyNbt();
    }

    public static NbtCompound getNullableNbtCompound(ItemStack stack) {
        NbtComponent data = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (data == null) {
            getError(stack);
            throw new NullPointerException("NbtComponent is null for stack: " + stack);
        }
        return data.copyNbt();
    }

    private static void getError(ItemStack stack) {
        Tyrannus.LOGGER.error("NbtComponent is null for stack: {}", stack);
    }
}

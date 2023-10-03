package me.letscode.minecraft.tools.nbt.utils;

import org.jnbt.Tag;

public record NBTTagInfo(String name, String imagePath, int nbtTypeId, Class<? extends Tag> tagClass) {
}

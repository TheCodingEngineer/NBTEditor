package me.letscode.minecraft.tools.nbt.utils;

import org.jnbt.NBTConstants;
import org.jnbt.Tag;

public record NBTTagInfo(String name, String imagePath, int flags, int nbtTypeId, Class<? extends Tag> tagClass) {

    public boolean isArray() {
        return (this.flags & NBTConstants.FLAG_ARRAY) == NBTConstants.FLAG_ARRAY;
    }

    public boolean hasChildren() {
        return (this.flags & NBTConstants.FLAG_CHILDREN) == NBTConstants.FLAG_CHILDREN;
    }

}

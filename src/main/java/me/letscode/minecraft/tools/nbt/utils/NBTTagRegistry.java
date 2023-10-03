package me.letscode.minecraft.tools.nbt.utils;

import org.jnbt.*;

import java.util.*;

public final class NBTTagRegistry {

    private static final Map<Integer, NBTTagInfo> registry = new HashMap<>();

    private static final List<NBTTagInfo> sorted = new ArrayList<>();

    private NBTTagRegistry() { }

    private static void register(NBTTagInfo info) {
        if (registry.put(info.nbtTypeId(), info) == null) {
            sorted.add(info);
        }
    }

    public static Collection<NBTTagInfo> getValues() {
        return registry.values();
    }

    public static List<NBTTagInfo> getSorted() {
        return sorted;
    }

    public static NBTTagInfo get(int id) {
        return registry.get(id);
    }

    public static boolean has(int id) {
        return registry.containsKey(id);
    }

    static {
        register(new NBTTagInfo("Byte", "/images/byte.png", NBTConstants.TYPE_BYTE, ByteTag.class));
        register(new NBTTagInfo("Short", "/images/short.png", NBTConstants.TYPE_SHORT, ShortTag.class));
        register(new NBTTagInfo("Int", "/images/integer.png", NBTConstants.TYPE_INT, IntTag.class));
        register(new NBTTagInfo("Long", "/images/long.png", NBTConstants.TYPE_LONG, LongTag.class));
        register(new NBTTagInfo("Float", "/images/float.png", NBTConstants.TYPE_FLOAT, FloatTag.class));
        register(new NBTTagInfo("Double", "/images/double.png", NBTConstants.TYPE_DOUBLE, DoubleTag.class));

        register(new NBTTagInfo("ByteArray", "/images/byte_array.png", NBTConstants.TYPE_BYTE_ARRAY, ByteArrayTag.class));
        register(new NBTTagInfo("IntArray", "/images/int_array.png", NBTConstants.TYPE_INT_ARRAY, IntArrayTag.class));
        register(new NBTTagInfo("LongArray", "/images/long_array.png", NBTConstants.TYPE_LONG_ARRAY, LongArrayTag.class));

        register(new NBTTagInfo("Compound", "/images/compound.png", NBTConstants.TYPE_COMPOUND, CompoundTag.class));
        register(new NBTTagInfo("List", "/images/list.png", NBTConstants.TYPE_LIST, ListTag.class));
        register(new NBTTagInfo("String", "/images/string.png", NBTConstants.TYPE_STRING, StringTag.class));
    }

}

package me.letscode.minecraft.tools.nbt.gui;

import org.jnbt.*;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.Map;

public class NbtTreeCellRenderer extends DefaultTreeCellRenderer {


    private static final String LIST_FORMAT = "<span style='font-weight:bold;'>%s</span>&nbsp;<span style='font-style:italic;'>[%d entries]</span>";

    private static final String EMPTY_LIST_FORMAT = "<span style='font-style:italic;'>root</span>&nbsp;<span style='font-style:italic;'>[%d entries]</span>";

    private static final String TAG_FORMAT = "<span style='font-weight:bold;'>%s</span>:&nbsp;<span>%s</span>";

    private static final String VALUE_FORMAT = "<span>%s</span>";

    private final Map<Integer, ImageIcon> icons;

    public NbtTreeCellRenderer(Map<Integer, ImageIcon> icons) {
        this.icons = icons;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (value instanceof NbtTreeNode nbtNode) {
            var tag = nbtNode.getNbtTag();
            this.setText(formatTagText(tag));
            this.setIcon(icons.get(NBTUtils.getTypeCode(tag.getClass())));
            this.setIconTextGap(5);
        }
        return this;
    }

    private String formatTagText(Tag tag) {
        String text;
        if (tag instanceof CompoundTag compoundTag) {
            if (compoundTag.getName().isEmpty()) {
                text = String.format(EMPTY_LIST_FORMAT, compoundTag.getValue().size());
            } else {
                text = String.format(LIST_FORMAT, compoundTag.getName(), compoundTag.getValue().size());
            }
        } else if (tag instanceof ListTag listTag) {
            text = String.format(LIST_FORMAT, listTag.getName(), listTag.getValue().size());
        } else if (tag instanceof IntArrayTag arrayTag){
            text = String.format(TAG_FORMAT, arrayTag.getName(), "int[" + arrayTag.getValue().length + "]");
        } else if (tag instanceof LongArrayTag arrayTag){
            text = String.format(TAG_FORMAT, arrayTag.getName(), "long[" + arrayTag.getValue().length + "]");
        } else if (tag instanceof ByteArrayTag arrayTag){
            text = String.format(TAG_FORMAT, arrayTag.getName(), "byte[" + arrayTag.getValue().length + "]");
        } else {
            if (tag.getName().isEmpty()) {
                text = String.format(VALUE_FORMAT, tag.getValue().toString());
            } else {
                text = String.format(TAG_FORMAT, tag.getName(), tag.getValue().toString());
            }
        }
        return "<html>" + text + "</html>";
    }
}

package me.letscode.minecraft.tools.nbt.gui;

import me.letscode.minecraft.tools.nbt.utils.NBTTagInfo;
import me.letscode.minecraft.tools.nbt.utils.Resources;

import javax.swing.*;
import java.awt.*;

public class NBTSelectorRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof NBTTagInfo info) {
            JLabel label = (JLabel) component;

            var iconImage = Resources.getImage(info.imagePath());
            var icon = Resources.getScaledImage(iconImage, 16, 16);

            label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            label.setText(info.name());
            label.setIconTextGap(10);
            label.setIcon(icon);
        }
        return component;
    }
}

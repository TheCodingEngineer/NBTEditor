package me.letscode.minecraft.tools.nbt.utils;

import javax.swing.*;

public final class ComponentUtils {

    public static JMenuItem search(JMenu menu, String actionCommand) {
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item instanceof JMenu) {
                return search((JMenu) item, actionCommand);
            } else {
                if (item.getActionCommand() != null && item.getActionCommand().equals(actionCommand)) {
                    return item;
                }
            }
        }
        return null;
    }
}

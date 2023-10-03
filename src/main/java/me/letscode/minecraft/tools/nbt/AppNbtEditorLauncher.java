package me.letscode.minecraft.tools.nbt;

import me.letscode.minecraft.tools.nbt.gui.GuiNbtEditor;

import javax.swing.*;

public class AppNbtEditorLauncher {

    public static void main(String[] args) {
        GuiNbtEditor editor = new GuiNbtEditor();

        SwingUtilities.invokeLater(() -> {
            editor.setLocationRelativeTo(null);
            editor.setVisible(true);
        });
    }

}

package me.letscode.minecraft.tools.nbt.gui;

import me.letscode.minecraft.tools.nbt.utils.NBTTagInfo;
import me.letscode.minecraft.tools.nbt.utils.NBTTagRegistry;
import me.letscode.minecraft.tools.nbt.utils.Resources;
import me.letscode.minecraft.tools.nbt.utils.StringHelper;
import org.jnbt.*;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.*;
import java.util.HashMap;

public class NbtPanel extends JPanel {

    private JTree nbtTree;

    private final NbtTabPanel parent;

    private final File file;

    public NbtPanel(NbtTabPanel parent, File file) {
        this.parent = parent;
        this.file = file;

        this.init();
        this.loadFile();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        nbtTree = new JTree();
        nbtTree.setModel(null);
        nbtTree.setEditable(false);
        nbtTree.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        nbtTree.setCellRenderer(new NbtTreeCellRenderer(parent.getNbtIcons()));

        add(nbtTree, BorderLayout.CENTER);
    }

    public void loadFile() {
        try (NBTInputStream nbtInput = new NBTInputStream(new FileInputStream(this.file))) {
            Tag rootTag = nbtInput.readTag();
            NbtTreeNode node = new NbtTreeNode(rootTag);

            nbtTree.setModel(new DefaultTreeModel(node));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred: \n"
                            + StringHelper.toString(e) + "\n\nCould not open NBT file!", "Error on reading file",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}

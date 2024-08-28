package me.letscode.minecraft.tools.nbt.gui;

import me.letscode.minecraft.tools.nbt.gui.menu.MenuBuilder;
import me.letscode.minecraft.tools.nbt.gui.menu.MenuListener;
import me.letscode.minecraft.tools.nbt.gui.menu.MenuPopupBuilder;
import me.letscode.minecraft.tools.nbt.utils.NBTTagInfo;
import me.letscode.minecraft.tools.nbt.utils.NBTTagRegistry;
import me.letscode.minecraft.tools.nbt.utils.Resources;
import me.letscode.minecraft.tools.nbt.utils.StringHelper;
import org.jnbt.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        nbtTree.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                JTree tree = (JTree)e.getSource();
                int row = tree.getClosestRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if (selPath == null)
                    return;


                NbtTreeNode node = (NbtTreeNode) selPath.getLastPathComponent();
                if (SwingUtilities.isRightMouseButton(e)) {
                    tree.setSelectionRow(row);
                    openPopupMenu(node, x, y);
                } else if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    Tag tag = node.getNbtTag();

                    JOptionPane.showMessageDialog(parent.getWindow(), String.format("%s: %s", tag.getName(), tag.getValue()), "NBT Node", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        /*nbtTree.addTreeSelectionListener(e -> {
            JTree tree = (JTree)e.getSource();
            if (tree.getLastSelectedPathComponent() instanceof NbtTreeNode node) {

                System.out.println(node.getNbtTag().getName());
            }
        });*/

        add(nbtTree, BorderLayout.CENTER);
    }

    private void openPopupMenu(NbtTreeNode node, int x, int y) {
        MenuListener menuListener = (handler, item) -> {
            if (item.getActionCommand() == null) {
                return;
            }
            NbtTreeNode parentNode = (NbtTreeNode) node.getParent();
            switch (item.getActionCommand()) {
                case "node.delete":
                    node.removeFromParent();
                    nbtTree.updateUI();
                    break;

                case "node.rename":
                    var oldName = node.getNbtTag().getName();
                    var newName = JOptionPane.showInputDialog(parent.getWindow(), "Enter new name for NBT node:", node.getNbtTag().getName());
                    if (newName != null && !newName.trim().isBlank() && !newName.equals(oldName)) {
                        node.getNbtTag().setName(newName);
                        if (parentNode != null && parentNode.getNbtTag() instanceof CompoundTag compoundTag) {
                            compoundTag.getValue().remove(oldName);
                            compoundTag.getValue().put(newName, node.getNbtTag());
                        }
                        nbtTree.updateUI();
                    }
                    break;
                case "node.edit":
                    var currentTag = node.getNbtTag();
                    var tagInfo = NBTTagRegistry.get(NBTUtils.getTypeCode(currentTag.getClass()));
                    var updatedTag = parent.getWindow().showEditDialog(tagInfo, currentTag);
                    if (updatedTag != null) {
                        currentTag.setValue(updatedTag.getValue());
                        nbtTree.updateUI();
                    }
                    break;
                case "node.view":
                    JOptionPane.showMessageDialog(parent.getWindow(), String.format("%s: %s", node.getNbtTag().getName(), node.getNbtTag().getValue()), "NBT Node", JOptionPane.PLAIN_MESSAGE);
                    break;
            }


            if (item.getActionCommand().startsWith("node.insert.id.")) {
                var typeId = Integer.parseInt(item.getActionCommand().split("\\.")[3]);

                var tagInfo = NBTTagRegistry.get(typeId);
                var newTag = parent.getWindow().showNewDialog(tagInfo);
                if (newTag != null) {
                    node.add(new NbtTreeNode(newTag));
                    nbtTree.updateUI();
                }
            }
        };

        boolean allowsChildren = node.getNbtTag() instanceof CompoundTag || node.getNbtTag() instanceof ListTag;

        var builder = new MenuPopupBuilder("menu.node", menuListener)
                .addMenuItem("menu.node.delete", "node.delete", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0))
                .addMenuItem("menu.node.edit", "node.edit")
                .addMenuItem("menu.node.rename", "node.rename", KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));

        if (allowsChildren) {
            var tagBuilder = new MenuBuilder("menu.node.insert", menuListener);
            for (NBTTagInfo info : NBTTagRegistry.getSorted()) {
                var image = Resources.getImage(info.imagePath());
                var tagIcon = Resources.getScaledImage(image, 16, 16);

                boolean enabled = true;
                // List Tag only allows one Type
                if (node.getNbtTag() instanceof ListTag listTag) {
                    enabled = listTag.getType().equals(info.tagClass());
                }
                tagBuilder.addNamedMenuItem(info.name(), "node.insert.id." + info.nbtTypeId(), tagIcon, enabled);
                if (info.nbtTypeId() == NBTConstants.TYPE_DOUBLE || info.nbtTypeId() == NBTConstants.TYPE_LONG_ARRAY) {
                    tagBuilder.addSeperator();
                }
            }

            builder.addMenu(tagBuilder);
        }
        builder.addSeparator();
        builder.addMenuItem("menu.node.view", "node.view");

        builder.getJPopupMenu().show(nbtTree, x, y);
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
